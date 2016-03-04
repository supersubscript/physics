import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

public class longString
{
	// @formatter:off
	private static final int ITERATIONS = 10000; // # generations
	private static final int POP_SIZE = 10; // # ind. in pop
	private static final int GENOME_LENGTH = 100; // # bits in gen.
	private static final int GENE_LENGTH = 10; 
	private static final int SIMULATIONS = 100; // 
	
	private static double MUTATE_PROB = 0.01; // for single bit
	private static final double PENALTY = 10; 
	private static final Encoding e = Encoding.GRAY; 
	private static final int PRINTERVAL = 10; 
	private static double fitAvg = 0; // for single bit
	private static double mutAvg = 0.; // for single bit
	private static double mutPosAvg = 0.; // for single bit
	private static double mutPosOcc = 0.; // for single bit
	private static double hamAvg = 0; // for single bit
	private static double reachedMin = 0; // for single bit
	private static int t;
	
//	private static Random rand;
	private static ArrayList<Bitstring> target;
	private static ArrayList<ArrayList<Bitstring>> targets;
	private static ArrayList<ArrayList<Bitstring>> pop;
	private static ArrayList<HashMap<Bitstring, Double>> fitnessMap;
	private static BitstringComparator bc;
	private static DistancePairComparator dpc;
	private static PrintWriter hamWriter;
	private static PrintWriter fitWriter;
	private static PrintWriter mutWriter;	
	private static PrintWriter avgWriter;	
	// @formatter:on

	public static void main(String[] args)
	      throws FileNotFoundException, UnsupportedEncodingException
	{
		init();

		for (int i = 0; i < SIMULATIONS; i++)
		{
			fitnessMap.add(new HashMap<Bitstring, Double>());
			target.add(new Bitstring(GENOME_LENGTH));
			targets.add(target.get(i).split(GENE_LENGTH));
			ArrayList<Bitstring> ab = new ArrayList<Bitstring>();
			for (int j = 0; j < POP_SIZE; j++)
				ab.add(new Bitstring(GENOME_LENGTH));
			pop.add(ab);
		}

		// Simulate evolution
		for (; t < ITERATIONS; t++)
		{
			mutWriter.print(t + "\t");
			if (t % PRINTERVAL == 0)
			{
				fitWriter.print(t + "\t");
				hamWriter.print(t + "\t");
			}
			// System.out.print(t + "\t");

			for (int i = 0; i < SIMULATIONS; i++)
			{
				fitnessMap.set(i, new HashMap<Bitstring, Double>());
				ArrayList<Bitstring> tlist = targets.get(i); // used a lot

				// This for-loop computes the fitness for all organisms, mutates
				// them, and puts the
				// result in a Map structure.
				for (Bitstring p : pop.get(i))
				{
					ArrayList<Bitstring> genes = p.split(GENE_LENGTH);

					double td = 0;
					if (genes.size() < tlist.size())
						td = totalDist(genes, tlist, i);
					else
						td = totalDist(tlist, genes, i);

					// Add fitness to population map
					fitnessMap.get(i).put(p, td);
					double before = td;

					// Mutate string and calculate fitness
					Bitstring bmut = new Bitstring(p); // copy
					Operator.mutate_single(bmut, MUTATE_PROB);
					genes = bmut.split(GENE_LENGTH);
					if (genes.size() < tlist.size())
						td = totalDist(genes, tlist, i);
					else
						td = totalDist(tlist, genes, i);
					// Print effect of mutation to file
					mutWriter.print(before - td + "\t");

					// Sum for statistics
					mutAvg += before - td;
					if (before - td > 0)
					{
						mutPosAvg += before - td;
						mutPosOcc++;
					}
					// Add to map
					fitnessMap.get(i).put(bmut, td);
				}

				// Put all POP_SIZE best individuals in new pop. Print best
				// individual to file.
				ArrayList<Bitstring> newPop = new ArrayList<Bitstring>(POP_SIZE);
				fitnessMap.set(i,
				      (HashMap<Bitstring, Double>) sortByValue(fitnessMap.get(i)));
				Iterator<Map.Entry<Bitstring, Double>> it = fitnessMap.get(i)
				      .entrySet().iterator();
				Map.Entry<Bitstring, Double> entry = it.next();

				if (t % PRINTERVAL == 0)
				{
					fitWriter.print(entry.getValue() + "\t");
				}
				fitAvg += entry.getValue();
				// System.out.print(entry.getValue() + "\t");
				for (int l = 0; l < POP_SIZE && it.hasNext(); l++)
				{
					newPop.add(entry.getKey());
					entry = it.next();
				}
				pop.set(i, newPop);
			}
			if (t % PRINTERVAL == 0)
			{
				fitWriter.println();
				hamWriter.println();
			}
			// System.out.println();
			mutWriter.println();
			avgWriter.println(t + "\t" + fitAvg / SIMULATIONS + "\t"
			      + mutAvg / SIMULATIONS + "\t" + mutPosAvg / mutPosOcc + "\t"
			      + hamAvg / SIMULATIONS + reachedMin / SIMULATIONS);

			fitAvg = 0.;
			mutAvg = 0.;
			mutPosAvg = 0.;
			hamAvg = 0.;
			reachedMin = 0.;

		}
		mutWriter.close();
		fitWriter.close();
		hamWriter.close();
	}

	// Method for sorting HashMaps by value
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
	      Map<K, V> map)
	{
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Comparator.comparing(e -> e.getValue()))
		      .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

		return result;
	}

	/*
	 * Returns the distance between two Bitstrings split up into genes (Lists of
	 * Bitstrings). Insert shortest list first.
	 */
	public static double totalDist(ArrayList<Bitstring> s,
	      ArrayList<Bitstring> l, int i)
	{
		double td = 0;
		s.sort(bc);
		l.sort(bc);
		ArrayList<DistancePair> pairs = new ArrayList<DistancePair>(s.size());
		Set<Bitstring> f = new HashSet<Bitstring>();

		int j = 0;
		for (Bitstring b : s)
			while (j < l.size())
			{
				double dist = dist(b, l.get(j), e);
				// If this is the only remaining one, give to all
				if (j == l.size() - 1)
				{
					pairs.add(new DistancePair(b, l.get(j), dist));
					break; // go to next b
				}
				// If this hit is best, take it
				else if (dist < dist(b, l.get(j + 1), e))
				{
					pairs.add(new DistancePair(b, l.get(j), dist));
					break; // go to next b
				}
				// If next hit better, move on
				else
					j++;
			}
		// Check which list holds targets
		boolean sIsTargets = false;
		if (targets.get(i).contains(s.get(0)))
			sIsTargets = true;

		// Sort pairs wrt distance (low distance first)
		pairs.sort(dpc);

		double hd = 0;
		for (DistancePair p : pairs)
		{
			hd += Bitstring.getHammingDistance(p.first, p.second);
			Bitstring b = sIsTargets ? p.first : p.second;
			// Trying to fit to tagged target?
			if (f.contains(b))
				td += p.distance + PENALTY;
			else
			{
				td += p.distance;
				f.add(b);
			}
		}
		if (t % PRINTERVAL == 0)
			hamWriter.print(hd + "\t");
		hamAvg += hd;
		return td;
	}

	// Measure distance between two bitstrings given a specified encoding.
	public static double dist(Bitstring a, Bitstring b, Encoding e)
	{
		if (e.equals(Encoding.GRAY))
			return Math.abs(BitInterpreter.gray(a) - BitInterpreter.gray(b));
		else if (e.equals(Encoding.BINARY))
			return Math.abs(BitInterpreter.binary(a) - BitInterpreter.binary(b));
		return -1;
	}

	public static void init() throws FileNotFoundException
	{
		fitWriter = new PrintWriter("/home/william/b16_henrikahl/evo_out/fitness/"
		      + e + "_mut_prob_" + MUTATE_PROB + ".dat");
		mutWriter = new PrintWriter(
		      "/home/william/b16_henrikahl/evo_out/mutation/" + e + "_mut_prob_"
		            + MUTATE_PROB + ".dat");
		hamWriter = new PrintWriter("/home/william/b16_henrikahl/evo_out/hamming/"
		      + e + "_mut_prob_" + MUTATE_PROB + ".dat");
		avgWriter = new PrintWriter("/home/william/b16_henrikahl/evo_out/stat/"
		      + e + "_mut_prob_" + MUTATE_PROB + ".dat");
		avgWriter.println("%Generation" + "\t" + "fitAvg" + "\t" + "mutAvg" + "\t"
		      + "mutPosAvg" + "\t" + "hamAvg" + "\t" + "reachedMin");

		target = new ArrayList<Bitstring>();
		targets = new ArrayList<ArrayList<Bitstring>>();
		pop = new ArrayList<ArrayList<Bitstring>>();
		fitnessMap = new ArrayList<HashMap<Bitstring, Double>>();
		bc = new BitstringComparator();
		dpc = new DistancePairComparator();
		t = 0;

	}

}