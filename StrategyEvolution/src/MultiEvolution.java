import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Random;
import java.util.Set;

/* Perform a single evolution. @author henrikahl */
public class MultiEvolution
{
	//	@formatter:off
	static int												SIMULATIONS					= 100;
	static int												NUMBER_OF_GENERATIONS	= 100000;
	static int												POPULATION_SIZE			= 20;
	static int												GENOME_LENGTH				= 100;
	static int												GENE_LENGTH					= 10;
	static double											MUTATE_PROB					= 1. / GENOME_LENGTH;
	static double											CROSS_PROB					= 0.01;
	static double											PENALTY						= 100.;
	static Encoding										e								= Encoding.BINARY;
	static CrossoverOperator							none							= CrossoverOperator.none();
	static MutationOperator								bitMutator					= MutationOperator.probFlip(MUTATE_PROB);

	static SelectionOperator							crossPick;
	static SelectionOperator							survivalPick;
	static ArrayList<Bitstring>						targetGenes;
	static FitnessFunction<Bitstring>				distanceMeasure;
	static DoubleUnaryOperator 						scale;
	static FitnessFunction<Bitstring>				fitness;

	static final Random									rand					= new Random();
	static final BitstringComparator					bc						= new BitstringComparator(e);
	static final DistancePairComparator				dpc					= new DistancePairComparator();
	static final String 									name 					= e.toString() + "_" + POPULATION_SIZE + "_" + MUTATE_PROB;
	static final 
					HashMap<String, PrintWriter>		writers				= new HashMap<String, PrintWriter>();
	static final 
					ArrayList<Population<Bitstring>> simulations 		= new ArrayList<Population<Bitstring>>();
	//	@formatter:on

	public static void main(String[] args) throws IOException
	{
		// All data needed for stat.dat
		double totalFitness = 0;
		double mutationAverage = 0;
		double positiveMutationAverage = 0;
		double hammingAverage = 0;
		double reachedSolution = 0;
		double numberOfPositiveMutations = 0;
		if(args.length>0)
			init(args[0]);
		else
			init(null);
		// Evolution process
		for (int t = 0; t < NUMBER_OF_GENERATIONS; t++)
		{
			if (t % 10 == 0)
			{
				writers.get("fitness").print(t + "\t");
				writers.get("mutation").print("\n" + t + "\t");
				writers.get("pos_mut").print("\n" + t + "\t");
				totalFitness = 0;
				mutationAverage = 0;
				positiveMutationAverage = 0;
				hammingAverage = 0;
				reachedSolution = 0;
				numberOfPositiveMutations = 0;
			}

			for (int s = 0; s < SIMULATIONS; s++)
			{
				Population<Bitstring> pop = simulations.get(s);

				// Crossover
				// Population<Bitstring> crossStrings = new Population<Bitstring>();
				// if (rand.nextDouble() < CROSS_PROB)
				// none.apply(null, null);

				// Mutate
				Population<Bitstring> mutStrings = new Population<Bitstring>();
				for (Bitstring b : pop)
				{
					Bitstring bb = bitMutator.mutate(b);
					mutStrings.add(bb);
					double change = fitness.applyDirectly(b)
							- fitness.applyDirectly(bb);

					writers.get("mutation").print(change + "\t");
					writers.get("pos_mut")
							.print(change > 0 ? change + "\t" : 0 + "\t");

					mutationAverage += change;
					if (change > 0)
					{
						positiveMutationAverage += change;
						numberOfPositiveMutations++;
					}
				}

				// Select
				pop.addAll(mutStrings);
				// pop.addAll(crossStrings);

				pop = survivalPick.select(pop);
				simulations.set(s, pop);
				// fitness.print(pop);
				// System.out.println();

				// System.out.println(pop.size());
				double bestFitness = fitness.applyDirectly(pop.get(0));
				totalFitness += bestFitness;
				if (bestFitness == 0)
					reachedSolution++;

				if (t % 10 == 0)
				{
					writers.get("fitness").print(bestFitness + "\t");
				}
			}

			if (t % 10 == 0)
			{
				writers.get("fitness").println();
				// writers.get("mutation").println();
				// writers.get("pos_mut").println();
				writers.get("stat")
						.println(
								t + "\t" + totalFitness / SIMULATIONS + "\t"
										+ mutationAverage / SIMULATIONS + "\t"
										+ positiveMutationAverage
												/ numberOfPositiveMutations
										+ "\t" + hammingAverage / SIMULATIONS + "\t"
										+ reachedSolution / SIMULATIONS);
			}
			for (Iterator<Entry<String, PrintWriter>> iterator = writers.entrySet()
					.iterator(); iterator.hasNext();)
				iterator.next().getValue().flush();
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
				double dist = e.distance(seekers.get(i), targets.get(j));

				if (j == targets.size() - 1)
				{
					pairs.add(new DistancePair(seeker, target, dist));
					break;
				} else if (dist == e.distance(seeker, targets.get(j + 1)))
				{
					pairs.add(new DistancePair(seeker, target, dist));
					j++;
				} else if (dist < e.distance(seeker, targets.get(j + 1)))
				{
					pairs.add(new DistancePair(seeker, target, dist));
					break;
				} else j++;
			}
		}

		// Sum the total distance for all the pairs. Add @param PENALTY if target
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
						+ "mutation" + "\t"
						+ "positiveMutation" + "\t" 
						+ "hamming" + "\t"
						+ "reachedSolution");
		//@formatter:on
	}

	public static void init(String args) throws IOException
	{
		// Initiate
		scale = d ->
		{
			double val = Math.max(d, 0.);
			return val < 100 ? val + val * Math.abs(Math.sin(val / 5)) : val;
			// return d;
		};

		fitness = (o) ->
		{
			double td = 0;
			ArrayList<Bitstring> seekerGenes = o.split(GENE_LENGTH);
			td = totalDistance(targetGenes, seekerGenes);
			double val = seekerGenes.size() > targetGenes.size()
					? td - PENALTY * (seekerGenes.size() - targetGenes.size()) : td;
			return scale.applyAsDouble(val);
		};

		initDataFiles();

		targetGenes = new Bitstring(GENOME_LENGTH).randomize().split(GENE_LENGTH);
		crossPick = SelectionOperator.rouletteWheel(fitness, 2);
		survivalPick = SelectionOperator.elitism(fitness, POPULATION_SIZE);

		// survivalPick = SelectionOperator.tournament(fitness, POPULATION_SIZE,
		// 5,
		// 0.7);
		// survivalPick = SelectionOperator.semiElitism(fitness,
		// SelectionOperator.tournament(fitness, POPULATION_SIZE - 1, 2,
		// 0.8),
		// 1);

		// survivalPick = SelectionOperator.elitism(fitness, POPULATION_SIZE);
		// survivalPick = SelectionOperator.semiElitism(fitness,
		// SelectionOperator.rouletteWheel(fitness, 8), 2);

		for (int i = 0; i < SIMULATIONS; i++)
		{
			simulations.add(new Population<Bitstring>());
			Population<Bitstring> p = simulations.get(i);
			for (int j = 0; j < POPULATION_SIZE; j++)
				p.add(new Bitstring(GENOME_LENGTH).randomize());
		}

	}

	public void loadSettings(File settings)
	{
		try (BufferedReader br = Files
				.newBufferedReader(Paths.get()))
		{
			// br returns as stream and convert it into a List
			List<String> list = br.lines().collect(Collectors.toList());

			SIMULATIONS = Integer.parseInt(list.get(0).split("\t")[1]);
			NUMBER_OF_GENERATIONS = Integer.parseInt(list.get(1).split("\t")[1]);
			POPULATION_SIZE = Integer.parseInt(list.get(2).split("\t")[1]);
			GENOME_LENGTH = Integer.parseInt(list.get(3).split("\t")[1]);
			GENE_LENGTH = Integer.parseInt(list.get(4).split("\t")[1]);
			MUTATE_PROB = Double.parseDouble(list.get(5).split("\t")[1]);
			CROSS_PROB = Double.parseDouble(list.get(6).split("\t")[1]);
			PENALTY = Double.parseDouble(list.get(7).split("\t")[1]);
			e = Encoding.valueOf(list.get(8).split("\t")[1].toUpperCase());
			none = CrossoverOperator.none();
			bitMutator = MutationOperator.probFlip(MUTATE_PROB);

		} catch (IOException ex)
		{
		}
	}
}
