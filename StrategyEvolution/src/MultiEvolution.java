import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.DoubleUnaryOperator;
import java.util.Random;
import java.util.Set;

public class MultiEvolution
{
	//	@formatter:off
	static int												SIMULATIONS					= 100;
	static int												NUMBER_OF_GENERATIONS	= 100000;
	static int												POPULATION_SIZE			= 50;
	static int												GENOME_LENGTH				= 100;
	static int												GENE_LENGTH					= 10;
	static double											MUTATE_PROB					= 1. / GENOME_LENGTH;
	static double											CROSS_PROB					= 0.01;
	static double											PENALTY						= 100.;
	static Encoding										encoding						= Encoding.BINARY;
	static ScaleFunction									scaleFunction				= ScaleFunction.LINEAR;
	static CrossoverOperator							none							= CrossoverOperator.none();
	static MutationOperator								bitMutator					= MutationOperator.probFlip(MUTATE_PROB);

	static FitnessFunction<Bitstring>				fitness;
	static DoubleUnaryOperator							scale;
	static SelectionOperator							crossPick;
	static SelectionOperator							survivalPick;
	static BitstringComparator							bc;						
	static DistancePairComparator						dpc;					
	static Random											rand;					
	static String 											name; 				
	static ArrayList<Bitstring>						targetGenes;
	static HashMap<String, PrintWriter>				writers;				
	static ArrayList<Population<Bitstring>> 		simulations; 		
	//	@formatter:on

	public static void main(String[] args) throws IOException
	{
		// All data needed for stat.dat
		double totalFitness = 0;
		double sumOfSquaredFitnesses = 0;
		double mutationAverage = 0;
		double positiveMutationAverage = 0;
		double hammingAverage = 0;
		double reachedSolution = 0;
		double numberOfPositiveMutations = 0;

		init(args);

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

			// Mutate
			Population<Bitstring> mutStrings = new Population<Bitstring>();
			for (Bitstring b : pop)
			{
				Bitstring bb = bitMutator.mutate(b);
				mutStrings.add(bb);
				double before = fitness.applyDirectly(b);
				double after = fitness.applyDirectly(bb);

				writers.get("mutation").print(after / before + "\t");

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

			double bestFitness = fitness.applyDirectly(pop.get(0));
			totalFitness += bestFitness;
			sumOfSquaredFitnesses += bestFitness * bestFitness;
			reachedSolution = bestFitness == 0 ? reachedSolution + 1
					: reachedSolution;

			if (t % 10 == 0)
				writers.get("fitness").print(bestFitness + "\t");
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
							+ positiveMutationAverage
									/ numberOfPositiveMutations
							+ "\t" + hammingAverage / SIMULATIONS + "\t"
							+ reachedSolution / SIMULATIONS);
			}
		}
		// Close printer streams
		for (Iterator<Entry<String, PrintWriter>> iterator = writers.entrySet()
			.iterator(); iterator.hasNext();)
			iterator.next().getValue().close();
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
		ArrayList<DistancePair> pairs = new ArrayList<DistancePair>();

		int j = 0;
		for (int i = 0; i < seekers.size(); i++)
		{
			Bitstring seeker = seekers.get(i);

			for (; j < targets.size();)
			{
			Bitstring target = targets.get(j);
			double dist = encoding.distance(seekers.get(i), targets.get(j));

			if (j == targets.size() - 1)
			{
				pairs.add(new DistancePair(seeker, target, dist));
				break;
			} else if (dist == encoding.distance(seeker, targets.get(j + 1)))
			{
				pairs.add(new DistancePair(seeker, target, dist));
				j++;
			} else if (dist < encoding.distance(seeker, targets.get(j + 1)))
			{
				pairs.add(new DistancePair(seeker, target, dist));
				break;
			} else j++;
			}
		}

		// Sum the total distance for all the pairs. Add @param PENALTY if
		// target
		// is double tagged.
		Set<Bitstring> alreadyTagged = new HashSet<Bitstring>();
		pairs.sort(dpc);
		for (DistancePair p : pairs)
		{
			Bitstring b = p.second;
			if (alreadyTagged.contains(b))
			total += p.distance + PENALTY;
			else
			{
			total += p.distance;
			alreadyTagged.add(b);
			}
		}
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
		rand = new Random();
		dpc = new DistancePairComparator();
		bc = new BitstringComparator(encoding);
		name = encoding.toString() + "_" + scaleFunction.toString() + "_"
			+ MUTATE_PROB;
		writers = new HashMap<String, PrintWriter>();
		simulations = new ArrayList<Population<Bitstring>>();

		// Define fitness function
		scale = scaleFunction.getFunction();
		fitness = (o) ->
		{
			double td = 0;
			ArrayList<Bitstring> seekerGenes = o.split(GENE_LENGTH);

			td = totalDistance(targetGenes, seekerGenes);
			double val = seekerGenes.size() > targetGenes.size()
					? td - PENALTY * (seekerGenes.size() - targetGenes.size())
					: td;
			return scale.applyAsDouble(val);
		};

		// Setup print devices
		initDataFiles();

		targetGenes = new Bitstring(GENOME_LENGTH).randomize()
			.split(GENE_LENGTH);
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
