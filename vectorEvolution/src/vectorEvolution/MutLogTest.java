package vectorEvolution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.DoubleUnaryOperator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class MutLogTest
{
	//	@formatter:off
	static int												SIMULATIONS					= 100;
	static int												NUMBER_OF_GENERATIONS	= 100000;
	static int												POPULATION_SIZE			= 40;
	static int												GENOME_LENGTH				= 100;
	static int												GENE_LENGTH					= 10;
	static double											MUTATE_PROB					= 1. / GENOME_LENGTH;
	static double											CROSS_PROB					= 0.01;
	static Encoding										encoding						= Encoding.GRAY;
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

			// System.out.print(t + "\t");
			if (t % 10 == 0)
			{
				writers.get("fitness").print(t + "\t");
				writers.get("mutation").print("\n" + t + "\t");
			}
			for (int s = 0; s < SIMULATIONS; s++)
			{
				HashMap<Bitstring, Double> pop = simulations.get(s);
				targetIntegerSet = targetIntegers.get(s);

				// Mutate

				HashMap<Bitstring, Double> mutStrings = new HashMap<Bitstring, Double>();
				for (Entry<Bitstring, Double> entry : pop.entrySet())
				{

					Bitstring bb = bitMutator.mutate(entry.getKey());
					mutStrings.put(bb, fitness.applyDirectly(bb));
					double before = scale.applyAsDouble(entry.getValue());

					if (before < 1000 && before > 100)
					{
						if ((differentIndividuals1000 += 10000) < 60000)
						{
							while (nr1000 < differentIndividuals1000)
							{
								mutStrings.put(bb, fitness.applyDirectly(bb));
								double after = scale.applyAsDouble(mutStrings.get(bb));
								if (after == 0 && before == after)
								{
									writers.get("fitness_1000_mut.dat").printf("%1.2f\n",
											1.00);
								} else
								{
									writers.get("fitness_1000_mut.dat").printf("%1.2f\n",
											before / after);
								}
								nr1000++;
							}
						}
					} else if (before > 50)
					{
						if ((differentIndividuals100 += 10000) < 60000)
						{
							while (nr100 < differentIndividuals100)
							{
								mutStrings.put(bb, fitness.applyDirectly(bb));
								double after = scale.applyAsDouble(mutStrings.get(bb));
								if (after == 0 && before == after)
								{
									writers.get("fitness_100_mut.dat").printf("%1.2f\n",
											1.00);
								} else
								{
									writers.get("fitness_100_mut.dat").printf("%1.2f\n",
											before / after);
								}
								nr100++;
							}
						}
					} else if (before > 25)
					{
						if ((differentIndividuals50 += 10000) < 60000)
						{
							while (nr50 < differentIndividuals50)
							{
								mutStrings.put(bb, fitness.applyDirectly(bb));
								double after = scale.applyAsDouble(mutStrings.get(bb));
								if (after == 0 && before == after)
								{
									writers.get("fitness_50_mut.dat").printf("%1.2f\n",
											1.00);
								} else
								{
									writers.get("fitness_50_mut.dat").printf("%1.2f\n",
											before / after);
								}
								nr50++;
							}
						}
					} else if (before > 10)
					{
						if ((differentIndividuals25 += 10000) < 60000)
						{
							while (nr25 < differentIndividuals25)
							{

								mutStrings.put(bb, fitness.applyDirectly(bb));
								double after = scale.applyAsDouble(mutStrings.get(bb));
								if (after == 0 && before == after)
								{
									writers.get("fitness_25_mut.dat").printf("%1.2f\n",
											1.00);
								} else
								{
									writers.get("fitness_25_mut.dat").printf("%1.2f\n",
											before / after);
								}
								nr25++;
							}
						}
					} else
					{
						if ((differentIndividuals10 += 10000) < 60000)
						{
							while (nr10 < differentIndividuals10)
							{
								mutStrings.put(bb, fitness.applyDirectly(bb));
								double after = scale.applyAsDouble(mutStrings.get(bb));
								if (after == 0 && before == after)
								{
									writers.get("fitness_10_mut.dat").printf("%1.2f\n",
											1.00);
								} else
								{
									writers.get("fitness_10_mut.dat").printf("%1.2f\n",
											before / after);
								}
								nr10++;
							}
						}
					}
				}

				pop.putAll(mutStrings);
				assert (pop.size() == 2 * POPULATION_SIZE);

				// Select
				pop = (HashMap<Bitstring, Double>) survivalPick.select(pop, scale);
				// System.out.println(pop);
				simulations.set(s, pop);
				assert (pop.size() == POPULATION_SIZE);

				double bestFitness = pop.entrySet().stream()
						.min((e1, e2) -> Double.compare(
								scale.applyAsDouble((double) (e1.getValue())),
								scale.applyAsDouble((double) (e2.getValue()))))
						.get().getValue();

				bestFitness = Double.isNaN(bestFitness) ? 2000 : bestFitness;
				double scaledBestFitness = Double.isNaN(bestFitness) ? 2000
						: scale.applyAsDouble(bestFitness);

				// System.out.print(bestFitness + "\t");
				sumOfSquaredShiftedFitnesses += (bestFitness - shift)
						* (bestFitness - shift);
				totalFitness += bestFitness;
				totalShiftedFitness += bestFitness - shift;

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
										/ (SIMULATIONS - 1) / SIMULATIONS));
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
		writers.put("fitness", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/fitness_1000_mut.dat", true))));
		writers.put("fitness", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/fitness_100_mut.dat", true))));
		writers.put("fitness", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/fitness_50_mut.dat", true))));
		writers.put("fitness", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/fitness_25_mut.dat", true))));
		writers.put("fitness", 	new PrintWriter(new BufferedWriter(new FileWriter(path.getAbsolutePath() + "/fitness_10_mut.dat", true))));
				
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
			return scale.applyAsDouble(td);
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
