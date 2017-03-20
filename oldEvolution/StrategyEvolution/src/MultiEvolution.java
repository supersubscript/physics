import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Random;
import java.util.Set;

public class MultiEvolution
{
	//	@formatter:off
	static int												SIMULATIONS					= 100;
	static int												NUMBER_OF_GENERATIONS	= 100000;
	static int												POPULATION_SIZE			= 40;
	static int												GENOME_LENGTH				= 100;
	static int												GENE_LENGTH					= 10;
	static double											MUTATE_PROB					= 1. / GENOME_LENGTH;
	static double											CROSS_PROB					= 0.01;
	static double											PENALTY						= 100.;
	static Encoding										encoding						= Encoding.BINARY;
	static ScaleFunction									scaleFunction				= ScaleFunction.LINEAR;
	static CrossoverOperator							none							= CrossoverOperator.none();

	static MutationOperator								bitMutator;					
	static FitnessFunction<Bitstring>				fitness;
	static DoubleUnaryOperator							scale;
	static SelectionOperator							crossPick;
	static SelectionOperator							survivalPick;
	static BitstringComparator							bc;						
	static DistancePairComparator						dpc;					
	static Random											rand;					
	static String 											name; 				
	static ArrayList<ArrayList<Bitstring>>			targetGenes;
	static ArrayList<Bitstring>						target;
	static HashMap<String, PrintWriter>				writers;				
	static ArrayList<Population<Bitstring>> 		simulations; 	
	
	//	@formatter:on

	public static void main(String[] args) throws IOException
	{
		init(args);
		// All data needed for stat.dat
		double totalFitness = 0;
		double sumOfSquaredFitnesses = 0;
		double mutationAverage = 0;
		double positiveMutationAverage = 0;
		double hammingAverage = 0;
		double reachedSolution = 0;
		double numberOfPositiveMutations = 0;

		// Evolution process
		for (int t = 0; t < NUMBER_OF_GENERATIONS; t++)
		{
			if (t % 10 == 0)
			{
				writers.get("fitness").print(t + "\t");
				writers.get("mutation").print("\n" + t + "\t");
				writers.get("pos_mut").print("\n" + t + "\t");
				totalFitness = 0;
				sumOfSquaredFitnesses = 0;
				mutationAverage = 0;
				positiveMutationAverage = 0;
				hammingAverage = 0;
				reachedSolution = 0;
				numberOfPositiveMutations = 0;
			}

			for (int s = 0; s < SIMULATIONS; s++)
			{
				Population<Bitstring> pop = simulations.get(s);
				target = targetGenes.get(s);

				// Mutate
				Population<Bitstring> mutStrings = new Population<Bitstring>();
				for (Bitstring b : pop)
				{
					Bitstring bb = bitMutator.mutate(b);
					mutStrings.add(bb);
					double before = fitness.applyDirectly(b);
					double after = fitness.applyDirectly(bb);

					writers.get("mutation").printf("%1.2f\t", before / after);

					// writers.get("pos_mut").print(
					// before - after > 0 ? before - after + "\t" : "NaN" + "\t");

					mutationAverage += before - after;
					if (before - after > 0)
					{
						positiveMutationAverage += before - after;
						numberOfPositiveMutations++;
					}
				}

				// Select
				pop.addAll(mutStrings);
				pop = survivalPick.select(pop);
				simulations.set(s, pop);

				double bestFitness = fitness
						.applyDirectly(pop.stream().min(new Comparator<Bitstring>()
						{
							@Override
							public int compare(Bitstring o1, Bitstring o2)
							{
								return (int) (fitness.applyDirectly(o1)
										- fitness.applyDirectly(o2));
							}
						}).get());

				totalFitness += bestFitness;
				sumOfSquaredFitnesses += bestFitness * bestFitness;
				reachedSolution = bestFitness == 0 ? reachedSolution + 1
						: reachedSolution;

				if (t % 10 == 0)
					writers.get("fitness").printf("%1.2f\t", bestFitness);

			}

			if (t % 10 == 0)
			{
				writers.get("fitness").println();
				writers.get("stat")
						.println(t + "\t" + totalFitness / SIMULATIONS + "\t"
								+ Math.sqrt((sumOfSquaredFitnesses
										- totalFitness * totalFitness / SIMULATIONS)
										/ (SIMULATIONS - 1))
								+ "\t" + mutationAverage / SIMULATIONS + "\t"
								+ positiveMutationAverage / numberOfPositiveMutations
								+ "\t" + hammingAverage / SIMULATIONS + "\t"
								+ reachedSolution / SIMULATIONS);
			}
		}
		// Close printer streams
		writers.entrySet().stream().forEach(s -> s.getValue().close());
	}

	/*
	 * Returns the distance between two Bitstrings split up into genes (Lists of
	 * Bitstrings).
	 */
	public static double totalDistance(ArrayList<Bitstring> targets,
			ArrayList<Bitstring> seekers)
	{
		double total = 0;
		targets.sort(bc);
		seekers.sort(bc);
		final int tSize = targets.size();
		final int sSize = seekers.size();

		ArrayList<DistancePair> pairs = new ArrayList<DistancePair>();
		ArrayList<Bitstring> newTargets = new ArrayList<Bitstring>();
		ArrayList<Bitstring> newSeekers = new ArrayList<Bitstring>();
		boolean[] taggedSeekers = new boolean[sSize];

		int j = 0;
		for (int i = 0; i < tSize; i++)
		{
			Bitstring t = targets.get(i);
			for (; j < sSize;)
			{
				Bitstring s = seekers.get(j);
				double dist = encoding.distance(s, t);

				if (j == sSize - 1
						|| dist < encoding.distance(seekers.get(j + 1), t))
				{
					pairs.add(new DistancePair(s, t, dist));
					taggedSeekers[j] = true;
					break;
				} else
				{
					if (!taggedSeekers[j])
						newSeekers.add(s);
					j++;
				}
			}
		}
		for (int i = j; i < sSize; i++)
			if (!taggedSeekers[i])
				newSeekers.add(seekers.get(i));

		System.out.println("s \t t \t d");
		Set<Bitstring> alreadyTagged = new HashSet<Bitstring>();
		pairs.sort(dpc);
		for (DistancePair p : pairs)
		{
			Bitstring pairSeeker = p.first;
			System.out.println(p);

			if (alreadyTagged.stream().anyMatch(s -> s.isSame(pairSeeker)))
				newTargets.add(p.second);
			else
			{
				total += p.distance;
				alreadyTagged.add(pairSeeker);
			}
		}
		System.out.println();
		if (newSeekers.size() == 0 || newTargets.size() == 0)
			return total;
		else total += totalDistance(newTargets, newSeekers);
		return total;
	}

	public static void initDataFiles() throws IOException
	{
		File path = new File(
				System.getProperty("user.home") + "/evo_out/" + name);
		path.mkdir();

		//@formatter:off
		writers.put("fitness", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/fitness.dat", 	true))));
		writers.put("mutation", new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/mutation.dat", 	true))));
		writers.put("pos_mut", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/pos_mut.dat", 	true))));
		writers.put("hamming", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/hamming.dat", 	true))));
		writers.put("stat", 		new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/stat.dat", 		true))));
		writers.get("stat").println(
						  "#generation" + "\t" 
						+ "fitness" + "\t" 
						+ "stdevfitness" + "\t"
						+ "mutation" + "\t"
						+ "positiveMutation" + "\t" 
						+ "hamming" + "\t"
						+ "reachedSolution");
		//@formatter:on
	}

	public static void init(String[] args) throws IOException
	{
		// Modify default settings
		if (args.length > 0)
		{
			encoding = Encoding.valueOf(args[0]);
			scaleFunction = ScaleFunction.valueOf(args[1]);
			MUTATE_PROB = Double.parseDouble(args[2]);
		}

		if (encoding.equals(Encoding.CONSENSUS_GRAY)
				|| encoding.equals(Encoding.CONSENSUS_BINARY))
		{
			GENOME_LENGTH *= 100;
			GENE_LENGTH *= 100;
		}

		bitMutator = MutationOperator.probFlip(MUTATE_PROB);
		rand = new Random();
		dpc = new DistancePairComparator();
		bc = new BitstringComparator(encoding);
		name = encoding.toString() + "_" + scaleFunction.toString() + "_"
				+ MUTATE_PROB + "_test";
		writers = new HashMap<String, PrintWriter>();
		simulations = new ArrayList<Population<Bitstring>>();

		// Define fitness function
		scale = scaleFunction.getFunction();
		fitness = (o) ->
		{
			double td = 0;
			ArrayList<Bitstring> seekerGenes = o.split(GENE_LENGTH);

			td = totalDistance(target, seekerGenes);
			double val = seekerGenes.size() > target.size()
					? td - PENALTY * (seekerGenes.size() - target.size()) : td;
			return scale.applyAsDouble(val);
		};

		// Setup print devices
		initDataFiles();
		targetGenes = new ArrayList<ArrayList<Bitstring>>();

		for (int i = 0; i < SIMULATIONS; i++)
		{
			targetGenes.add(new ArrayList<Bitstring>());
			targetGenes.set(i,
					new Bitstring(GENOME_LENGTH).randomize().split(GENE_LENGTH));
		}
		crossPick = SelectionOperator.rouletteWheel(fitness, 2);
		survivalPick = SelectionOperator.tournament(fitness, POPULATION_SIZE, 2,
				1.);

		for (int i = 0; i < SIMULATIONS; i++)
		{
			simulations.add(new Population<Bitstring>());
			Population<Bitstring> p = simulations.get(i);
			for (int j = 0; j < POPULATION_SIZE; j++)
				p.add(new Bitstring(GENOME_LENGTH).randomize());
		}
	}
}
