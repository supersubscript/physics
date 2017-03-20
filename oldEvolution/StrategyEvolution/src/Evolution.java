import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/* Perform a single evolution. @author henrikahl */
public class Evolution
{
	//	@formatter:off
	static final int							SIMULATIONS					= 100;
	static final int							NUMBER_OF_GENERATIONS	= 1000000;
	static final int							POPULATION_SIZE			= 100;
	static final int							GENOME_LENGTH				= 100;
	static final int							GENE_LENGTH					= 10;
	static final double						MUTATE_PROB					= 1. / GENOME_LENGTH;
	static final double						CROSS_PROB					= 0.01;
	static final double						PENALTY						= 100.;

	static final Random						rand							= new Random();
	static final Encoding					e								= Encoding.GRAY;
	static final MutationOperator			bitMutator					= MutationOperator.probFlip(MUTATE_PROB);
	static final CrossoverOperator		none							= CrossoverOperator.none();
	static final BitstringComparator		bc								= new BitstringComparator(e);
	static final DistancePairComparator	dpc							= new DistancePairComparator();

	static SelectionOperator				crossPick;
	static SelectionOperator				survivalPick;
	static Population<Bitstring>			pop;
	static Bitstring							target;
	static ArrayList<Bitstring>			targetGenes;
	static FitnessFunction<Bitstring>	fitness;
	//	@formatter:on

	public static void main(String[] args)
	{
		// Initiate
		fitness = (o) ->
		{
			double td = 0;
			ArrayList<Bitstring> seekerGenes = o.split(GENE_LENGTH);
			td = totalDistance(targetGenes, seekerGenes, false);
			return td;
		};
		pop = new Population<Bitstring>(POPULATION_SIZE);
		target = new Bitstring(GENOME_LENGTH).randomize();
		targetGenes = target.split(GENE_LENGTH);
		crossPick = SelectionOperator.rouletteWheel(fitness, 2);
		survivalPick = SelectionOperator.elitism(fitness, POPULATION_SIZE);

		for (int i = 0; i < POPULATION_SIZE; i++)
			pop.add(new Bitstring(GENOME_LENGTH).randomize());

		// Evolution process
		for (int t = 0; t < NUMBER_OF_GENERATIONS; t++)
		{
			Population<Bitstring> newStrings = new Population<Bitstring>();

			// Crossover
			// none.doNothing();

			// Mutate
			for (Bitstring b : pop)
			{
				Bitstring bb = bitMutator.mutate(b);
				newStrings.add(bb);
			}

			// Select
			pop.addAll(newStrings);
			pop = (Population<Bitstring>) survivalPick.select(pop);

//			System.out.println();
			System.err.println(
					totalDistance(targetGenes, pop.get(0).split(GENE_LENGTH), true));
//			System.out.println();
		}
	}

	/*
	 * Returns the distance between two Bitstrings split up into genes (Lists of
	 * Bitstrings). Insert shortest list first.
	 */
	public static double totalDistance(ArrayList<Bitstring> targets,
			ArrayList<Bitstring> seekers, boolean best)
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

		Set<Bitstring> alreadyTagged = new HashSet<Bitstring>();
		pairs.sort(dpc);
		for (DistancePair p : pairs)
		{
			System.out.println(p);
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

	public static void collectMutationData(Bitstring before, Bitstring after,
			File file)
	{

	}

}
