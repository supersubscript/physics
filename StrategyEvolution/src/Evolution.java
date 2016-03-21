import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/* Perform a single evolution. @author henrikahl */
public class Evolution
{
	static final Random rand = new Random();
	static FitnessFunction<Bitstring> fitness;

	static final double MUTATE_PROB = 0.01;
	static final double CROSS_PROB = 0.01;
	static final double PENALTY = 100.;

	static final int NUMBER_OF_GENERATIONS = 10000;
	static final int POPULATION_SIZE = 10;
	static final int GENOME_LENGTH = 100;
	static final int GENE_LENGTH = 10;
	static final int SIMULATIONS = 100;

	static final Encoding e = Encoding.GRAY;
	static final MutationOperator bitMutator = MutationOperator
			.probFlip(MUTATE_PROB);
	static final CrossoverOperator none = CrossoverOperator.none();
	static SelectionOperator crossPick;
	static SelectionOperator survivalPick;
	static Population<Bitstring> pop;
	static Bitstring target;
	static ArrayList<Bitstring> targetGenes;

	static BitstringComparator bc = new BitstringComparator(e);
	static DistancePairComparator dpc = new DistancePairComparator();

	public static void main(String[] args)
	{
		// Initiate
		fitness = (o) ->
		{
			double td = 0;
			ArrayList<Bitstring> genes = o.split(GENE_LENGTH);
			if (genes.size() < targetGenes.size())
				td = totalDistance(genes, targetGenes, false);
			else
				td = totalDistance(targetGenes, genes, false);
			return (double) GENOME_LENGTH / GENE_LENGTH > targetGenes.size()
					? td - (double) GENOME_LENGTH / GENE_LENGTH * PENALTY : td;
		};

		pop = new Population<Bitstring>(POPULATION_SIZE);
		target = new Bitstring(GENOME_LENGTH);
		targetGenes = target.split(GENE_LENGTH);
		crossPick = SelectionOperator.rouletteWheel(fitness, 2);
		survivalPick = SelectionOperator.bestFraction(fitness, POPULATION_SIZE);

		for (int i = 0; i < POPULATION_SIZE; i++)
			pop.add(new Bitstring(GENOME_LENGTH));

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
			ArrayList<Bitstring> temp = (ArrayList<Bitstring>) survivalPick
					.select(pop);

			System.out.println(fitness.applyDirectly(temp.get(0)));

			// Gives different results depending on which argument goes in first
			// System.out.println(totalDistance(targetGenes,
			// temp.get(0).split(GENE_LENGTH), true));
			System.out.println(totalDistance(temp.get(0).split(GENE_LENGTH),
					targetGenes, true));

			System.out.println();

			pop.clear();
			pop.addAll(temp);
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
		Set<Bitstring> f = new HashSet<Bitstring>();
		TreeMap<Double,ArrayList<DistancePair>> map = new TreeMap<Double,ArrayList<DistancePair>>();
		
		int j = 0; 
		for (int i = 0; i < seekers.size(); i++)
		{
			double dist = e.distance(seekers.get(i), targets.get(j));

			for (; j < targets.size(); j++)
			{
				if(j == targets.size() - 1)
				{
					if(map.get(dist) == null)
						map.put(dist, new ArrayList<DistancePair>());
					map.put(dist, map.get(dist).add(new DistancePair(seekers.get(i),targets.get(j))));
					break; 
				}
				else if(dist < e.distance(seekers.get(i), targets.get(j+1)))
				{
					map.put(dist, new DistancePair(seekers.get(i), targets.get(j)));
					break;
				}	
				else
					j++;
			}
		}
		
//		int j = 0;
//		for (Bitstring b : targets)
//			while (j < seekers.size())
//			{
//				double dist = e.distance(b, seekers.get(j));
//				// If this is the only remaining one, give to all
//				if (j == seekers.size() - 1)
//				{
//					pairs.add(new DistancePair(b, seekers.get(j), dist));
//					break; // go to next b
//				}
//				// If this hit is best, take it
//				else if (dist < e.distance(b, seekers.get(j + 1)))
//				{
//					pairs.add(new DistancePair(b, seekers.get(j), dist));
//					break; // go to next b
//				}
//				// If next hit better, move on
//				else
//					j++;
//			}
		// Check which list holds targets
		boolean sIsTargets = true;
//		pairs.sort(dpc);
		// System.out.println(":)" + "\t" + pairs.get(0).distance);

		for (DistancePair p : pairs)
		{
			// if (best)
			// {
			// System.out.println(p.first);
			// System.out.println(p.second);
			// System.out.println(p.distance);
			// }

			Bitstring b = sIsTargets ? p.first : p.second;
			// Trying to fit to tagged target?
			if (f.contains(b))
				total += p.distance + PENALTY;
			else
			{
				total += p.distance;
				f.add(b);
			}
		}
		return total;
	}

}
