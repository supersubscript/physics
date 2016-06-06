package vectorEvolution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.DoubleUnaryOperator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class MutLogTest
{
	//	@formatter:off
	static int												SIMULATIONS					= 1;
	static int												NUMBER_OF_GENERATIONS	= 10000;
	static int												POPULATION_SIZE			= 40;
	static int												GENOME_LENGTH				= 100;
	static int												GENE_LENGTH					= 10;
	static double											MUTATE_PROB					= 1. / GENOME_LENGTH;
	static double											CROSS_PROB					= 0.01;
	static Encoding										encoding						= Encoding.CONSENSUS_GRAY;
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
	static ArrayList<ArrayList<Integer>>			targetIntegers; 
	static ArrayList<Integer>							targetIntegerSet;
	static HashMap<String, PrintWriter>				writers;				
	static ArrayList<HashMap<Bitstring, Double>>	simulations;
	static final boolean test = false; 
	//	@formatter:on

	public static void main(String[] args) throws IOException
	{
		init(args);
		// Data needed for stat.dat
		double totalFitness = 0;
		double sumOfSquaredShiftedFitnesses = 0;
		double totalGenes = 0;
		double sumOfSquaredGenes = 0;
		double totalShiftedFitness = 0;
		double shift = 0;
		double totalScaledFitness = 0;
		double sumOfSquaredShiftedScaledFitnesses = 0;
		double totalShiftedScaledFitness = 0;
		double scaledShift = 0;
		int nr10 = 0;
		int nr25 = 0;
		int nr50 = 0;
		int nr100 = 0;
		int nr1000 = 0;
		int differentIndividuals10 = 0;
		int differentIndividuals25 = 0;
		int differentIndividuals50 = 0;
		int differentIndividuals100 = 0;
		int differentIndividuals1000 = 0;
		double prior = 0;
		double diag = 0;
		double horiz = 0;
		double priorFitness = 0;
		double[] geneArray = new double[10];

		// Evolution process
		for (int t = 0; t < NUMBER_OF_GENERATIONS; t++)
		{
			shift = simulations.get(0).entrySet().stream()
					.min((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get()
					.getValue();
			scaledShift = scale.applyAsDouble(shift);

			totalFitness = 0;
			totalShiftedFitness = 0;
			sumOfSquaredShiftedFitnesses = 0;
			totalScaledFitness = 0;
			totalShiftedScaledFitness = 0;
			sumOfSquaredShiftedScaledFitnesses = 0;
			totalGenes = 0;
			sumOfSquaredGenes = 0;

			// System.out.print(t + "\t");
			if (t % 10 == 0)
			{
				writers.get("fitness").print(t + "\t");
				writers.get("mutation").print("\n" + t + "\t");
				writers.get("genes").print("\n" + t + "\t");

			}
			for (int s = 0; s < SIMULATIONS; s++)
			{
				HashMap<Bitstring, Double> pop = simulations.get(s);
				targetIntegerSet = targetIntegers.get(s);
				differentIndividuals1000 += 10000;
				differentIndividuals100 += 10000;
				differentIndividuals50 += 10000;
				differentIndividuals25 += 10000;
				differentIndividuals10 += 10000;

				// Mutate
				HashMap<Bitstring, Double> mutStrings = new HashMap<Bitstring, Double>();
				for (Entry<Bitstring, Double> entry : pop.entrySet())
				{

					Bitstring beforeString = entry.getKey();
					Bitstring bb = bitMutator.mutate(beforeString);
					double after = fitness.applyDirectly(bb);
					mutStrings.put(bb, after);
					after = scale.applyAsDouble(after);
					double before = scale.applyAsDouble(entry.getValue());

					// Log mutation effects
					// if (after < 1000)
					// {
					// if (before > 1000 && after > 100)
					// {
					// if (differentIndividuals1000 <= 50000)
					// {
					// for (int iter = 0; iter < 10000; iter++)
					// {
					// Bitstring mutant = bitMutator.mutate(bb);
					// differentIndividuals1000++;
					// double mutantCost = scale
					// .applyAsDouble(fitness.applyDirectly(mutant));
					// if (mutantCost == 0)
					// writers.get("fitness_1000_mut.dat").printf(
					// "%1.2f\n", Double.POSITIVE_INFINITY);
					// else writers.get("fitness_1000_mut.dat")
					// .printf("%1.2f\n", before / mutantCost);
					// }
					// }
					// }else if (before > 100 && after > 50 && after < 100)
					// {
					// if (differentIndividuals100 <= 50000)
					// {
					// for (int iter = 0; iter < 10000; iter++)
					// {
					// Bitstring mutant = bitMutator.mutate(bb);
					// differentIndividuals100++;
					// double mutantCost = scale
					// .applyAsDouble(fitness.applyDirectly(mutant));
					// if (mutantCost == 0)
					// writers.get("fitness_100_mut.dat").printf(
					// "%1.2f\n", Double.POSITIVE_INFINITY);
					// else writers.get("fitness_100_mut.dat")
					// .printf("%1.2f\n", before / mutantCost);
					// }
					// }
					// }else if (before > 50 && after > 25 && after < 50)
					// {
					// if (differentIndividuals50 <= 50000)
					// {
					// for (int iter = 0; iter < 10000; iter++)
					// {
					// Bitstring mutant = bitMutator.mutate(bb);
					// differentIndividuals50++;
					// double mutantCost = scale
					// .applyAsDouble(fitness.applyDirectly(mutant));
					// if (mutantCost == 0)
					// writers.get("fitness_50_mut.dat").printf(
					// "%1.2f\n", Double.POSITIVE_INFINITY);
					// else writers.get("fitness_50_mut.dat")
					// .printf("%1.2f\n", before / mutantCost);
					// }
					// }
					// }
					// else if (before > 25 && after > 10 && after < 25)
					// {
					// if (differentIndividuals25 <= 50000)
					// {
					// for (int iter = 0; iter < 10000; iter++)
					// {
					// Bitstring mutant = bitMutator.mutate(bb);
					// differentIndividuals25++;
					// double mutantCost = scale
					// .applyAsDouble(fitness.applyDirectly(mutant));
					// if (mutantCost == 0)
					// writers.get("fitness_25_mut.dat").printf(
					// "%1.2f\n", Double.POSITIVE_INFINITY);
					// else writers.get("fitness_25_mut.dat")
					// .printf("%1.2f\n", before / mutantCost);
					// }
					// }
					// }else if (before > 10 && after < 10)
					// {
					// if (differentIndividuals10 <= 50000)
					// {
					// for (int iter = 0; iter < 10000; iter++)
					// {
					// Bitstring mutant = bitMutator.mutate(bb);
					// differentIndividuals10++;
					// double mutantCost = scale
					// .applyAsDouble(fitness.applyDirectly(mutant));
					// if (mutantCost == 0)
					// writers.get("fitness_10_mut.dat").printf(
					// "%1.2f\n", Double.POSITIVE_INFINITY);
					// else writers.get("fitness_10_mut.dat")
					// .printf("%1.2f\n", before / mutantCost);
					// }
					// }
					// }
					// }
				}

				pop.putAll(mutStrings);
				assert (pop.size() == 2 * POPULATION_SIZE);

				// Select
				pop = (HashMap<Bitstring, Double>) survivalPick.select(pop, scale);
				// System.out.println(pop);
				simulations.set(s, pop);
				assert (pop.size() == POPULATION_SIZE);

				Entry<Bitstring, Double> best = pop.entrySet().stream()
						.min((e1, e2) -> Double.compare(
								scale.applyAsDouble((double) (e1.getValue())),
								scale.applyAsDouble((double) (e2.getValue()))))
						.get();
				double bestFitness = pop.entrySet().stream()
						.min((e1, e2) -> Double.compare(
								scale.applyAsDouble((double) (e1.getValue())),
								scale.applyAsDouble((double) (e2.getValue()))))
						.get().getValue();

				bestFitness = Double.isNaN(bestFitness) ? 10000 : bestFitness;
				double scaledBestFitness = Double.isNaN(bestFitness) ? 10000
						: scale.applyAsDouble(bestFitness);

				// for (Map.Entry<Bitstring, Double> b : pop.entrySet())
				// {
				// System.out.print(b.getKey().getGenes() +"\t");
				// }
				// System.out.println();

				// System.out.print(bestFitness + "\t");
				sumOfSquaredShiftedFitnesses += (bestFitness - shift)
						* (bestFitness - shift);
				totalFitness += bestFitness;
				ArrayList<Bitstring> strrr = best.getKey().split(GENE_LENGTH);
				double genes = 0;
				boolean lol = true;
				System.out.print(t + "\t");
				int lll = 0;
				for (Bitstring b : strrr)
				{
					genes += b.getGenes();
					if (geneArray[lll] != b.getGenes())
					{
						lol = false;
					}
					geneArray[lll] = b.getGenes();
					lll++;
				}
				System.out.println(genes + "\t" + bestFitness);
				if (bestFitness < priorFitness && genes == prior && !lol)
				{
					horiz++;
				} else if (bestFitness < priorFitness)
					diag++;
				priorFitness = bestFitness;
				prior = genes;
				genes /= 10.;
				if (bestFitness == 0)
					System.out.println("horizontal:" + horiz / (horiz + diag));

				totalGenes += genes;
				sumOfSquaredGenes += (genes) * (genes);

				totalShiftedFitness += bestFitness - shift;
				// System.out.print(genes + "\t");
				// Sum over scaled variants
				sumOfSquaredShiftedScaledFitnesses += (scaledBestFitness
						- scaledShift) * (scaledBestFitness - scaledShift);
				totalScaledFitness += scaledBestFitness;
				totalShiftedScaledFitness += scaledBestFitness - scaledShift;
				// System.out.println(bestFitness);
				if (t % 10 == 0)
				{
					writers.get("fitness").printf("%1.2f\t", bestFitness);
				}
			}
			// System.out.println();
			// System.out.println();

			if (t % 10 == 0)
			{
				writers.get("fitness").println();
				writers.get("stat")
						.println(t + "\t" + totalFitness / SIMULATIONS + "\t"
								+ Math.sqrt((sumOfSquaredShiftedFitnesses
										- totalShiftedFitness * totalShiftedFitness
												/ SIMULATIONS)
										/ (SIMULATIONS - 1) / SIMULATIONS)
								+ "\t" + totalScaledFitness / SIMULATIONS + "\t"
								+ Math.sqrt((sumOfSquaredShiftedScaledFitnesses
										- totalShiftedScaledFitness
												* totalShiftedScaledFitness / SIMULATIONS)
										/ (SIMULATIONS - 1) / SIMULATIONS)

								+ "\t" + totalGenes / SIMULATIONS + "\t"
								+ Math.sqrt((sumOfSquaredGenes
										- totalGenes * totalGenes / SIMULATIONS)
										/ (SIMULATIONS - 1) / SIMULATIONS)

				);
			}
		}
		// Close printer streams
		writers.entrySet().stream().forEach(s -> s.getValue().close());

	}

	/*
	 * Returns the distance between two Bitstrings split up into genes (Lists of
	 * Bitstrings).
	 */
	public static double totalDistance(ArrayList<Integer> targets,
			ArrayList<Bitstring> seekers)
	{
		double total = 0;
		Collections.sort(targets);
		Collections.sort(seekers, bc);
		final int tSize = targets.size();
		final int sSize = seekers.size();
		ArrayList<DistancePair> pairs = new ArrayList<DistancePair>();
		ArrayList<Integer> newTargets = new ArrayList<Integer>();
		ArrayList<Bitstring> newSeekers = new ArrayList<Bitstring>();

		boolean[] taggedSeekers = new boolean[sSize];

		int j = 0;
		for (int i = 0; i < tSize; i++)
		{
			int t = targets.get(i);
			for (; j < sSize;)
			{
				Bitstring s = seekers.get(j);
				double dist = Math.abs(s.getValue() - t);

				if (j == sSize - 1
						|| dist < Math.abs(seekers.get(j + 1).getValue() - t))
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

		// System.out.println("s \t t \t d");
		Set<Bitstring> alreadyTagged = new TreeSet<Bitstring>(bc);
		pairs.sort(dpc);

		for (DistancePair p : pairs)
		{
			Bitstring pairSeeker = p.first;
			// System.out.println(p);

			if (alreadyTagged.stream().anyMatch(s -> s.isSame(pairSeeker)))
				newTargets.add(p.third);
			else
			{
				total += p.distance;
				alreadyTagged.add(pairSeeker);
			}
		}
		// System.out.println();
		if (newSeekers.size() == 0 || newTargets.size() == 0)
			return total;
		else total += totalDistance(newTargets, newSeekers);
		return total;
	}

	public static void initDataFiles(String[] args) throws IOException
	{
		String dataFolder = null;
		if (args.length > 2)
		{
			dataFolder = args[3] + "/";
		}

		File path = new File(
				// System.getProperty("user.home") + "/evo_data/"
				// "/home/william/b16_henrikahl" + "/evo_data/"
				// + (dataFolder == null ? "" : dataFolder) + name);
				"/scratch/bob/b16_henrikahl" + "/evo_out/"
						+ (dataFolder == null ? "" : dataFolder) + name);
		path.mkdirs();

		//@formatter:off
		writers.put("fitness", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/fitness.dat", true))));
		writers.put("fitness_1000_mut.dat", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/fitness_1000_mut.dat", true))));
		writers.put("fitness_100_mut.dat", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/fitness_100_mut.dat", true))));
		writers.put("fitness_50_mut.dat", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/fitness_50_mut.dat", true))));
		writers.put("fitness_25_mut.dat", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/fitness_25_mut.dat", true))));
		writers.put("fitness_10_mut.dat", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/fitness_10_mut.dat", true))));
		writers.put("genes", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/genes.dat", true))));
					
		writers.put("mutation", new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/mutation.dat", 	true))));
		writers.put("stat", 		new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/stat.dat", 		true))));
		writers.get("stat").println(
						  "#generation" 	+ "\t" 
						+ "fitness" 		+ "\t" 
						+ "stdevfitness" 	+ "\t"
						+ "scaledFitness" + "\t" 
						+ "stdevScaledFitness");
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
			MUTATE_PROB /= 100;
		}

		bitMutator = MutationOperator.probFlip(MUTATE_PROB);
		rand = new Random();
		dpc = new DistancePairComparator();
		bc = new BitstringComparator();
		writers = new HashMap<String, PrintWriter>();
		simulations = new ArrayList<HashMap<Bitstring, Double>>();
		name = encoding.toString() + "_" + scaleFunction.toString() + "_"
				+ MUTATE_PROB;
		initDataFiles(args);

		// Define fitness function
		scale = scaleFunction.getFunction();
		fitness = (o) ->
		{
			double td = 0;
			ArrayList<Bitstring> seekerGenes = o.split(GENE_LENGTH);
			td = totalDistance(targetIntegerSet, seekerGenes);
			return td;
		};

		targetIntegers = new ArrayList<ArrayList<Integer>>();

		for (int i = 0; i < SIMULATIONS; i++)
		{
			targetIntegers.add(new ArrayList<Integer>());
			targetIntegerSet = targetIntegers.get(i);
			for (int j = 0; j < GENOME_LENGTH / GENE_LENGTH; j++)
			{
				targetIntegerSet.add((int) (rand.nextDouble() * 1024));
			}
			simulations.add(new HashMap<Bitstring, Double>());

			// Initialize population
			HashMap<Bitstring, Double> p = simulations.get(i);
			for (int j = 0; j < POPULATION_SIZE; j++)
			{
				Bitstring individual = new Bitstring(GENOME_LENGTH);
				p.put(individual, fitness.applyDirectly(individual));
			}
		}
		survivalPick = SelectionOperator.tournament(fitness, POPULATION_SIZE, 2,
				1.);
		// crossPick = SelectionOperator.rouletteWheel(fitness, 2);
	}
}
