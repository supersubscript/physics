import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class evo
{
	// @formatter:off
	public static final int ITERATIONS = 100000; // # generations
	public static final int POP_SIZE = 10; // # ind. in pop
	public static final int TARGETS_SIZE = 10; // # targets
	public static final int GENOME_LENGTH = 100; // # bits in gen.
	public static final int GENE_LENGTH = 100; // # bits in gen.
	
	public static final double MUTATE_PROB = 0.1; // for single bit
	public static final double PENALTY = 100; 
	public static final String ENCODING= "binary"; 
	
	private static Random rand;
	private static ArrayList<Bitstring> targets;
	private static ArrayList<Bitstring> pop;
	private static BitstringComparator bc;
	private static DistancePairComparator dpc;
	// @formatter:on

	public static void main(String[] args)
	{
		init();

		// Actual simulation
		for (int t = 0; t < 1; t++)
		{
			double td = totalDist(pop, targets);
			System.out.println(td);
		}
	}

	// Insert shortest list first
	public static double totalDist(ArrayList<Bitstring> s,
	      ArrayList<Bitstring> l)
	{
		double td = 0;
		s.sort(bc);
		l.sort(bc);
		ArrayList<DistancePair> pairs = new ArrayList<DistancePair>();
		HashSet<Bitstring> f = new HashSet<Bitstring>();

		int j = 0;
		for (Bitstring b : s)
		{
			while (j < l.size())
			{
				double dist = dist(b, l.get(j), ENCODING);
				// If this is the only remaining one, give to all
				if (j == l.size() - 1)
				{
					pairs.add(new DistancePair(b, l.get(j), dist));
					break; // go to next b
				}
				// If this hit is best, take it
				else if (dist < dist(b, l.get(j + 1), ENCODING))
				{
					pairs.add(new DistancePair(b, l.get(j), dist));
					break;
				}
				// If next hit better, move on
				else
				{
					j++;
				}
			}
		}
		// Check which list holds targets
		boolean sIsTargets = false;
		if (targets.contains(s.get(0)))
			sIsTargets = true;

		// Sort pairs wrt distance (low distance first)
		pairs.sort(dpc);

		// for (DistancePair p : pairs)
		// System.out.println(p);
		// double d = 0;
		// for (DistancePair p : pairs)
		// d += p.distance;
		// System.out.println(d);

		for (DistancePair p : pairs)
		{
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
		return td;
	}
	public static double dist(Bitstring a, Bitstring b, String encoding)
	{
		if (encoding.equals("gray"))
			return Math.abs(BitInterpreter.gray(a) - BitInterpreter.gray(b));
		else if (encoding.equals("binary"))
			return Math.abs(BitInterpreter.binary(a) - BitInterpreter.binary(b));
		return -1;

	}
	
	/* Selects fraction of population for survival and replaces it with new. */
	public static ArrayList<Bitstring> fractionSelect(double frac)
	{
				
		return pop;
	}

	public static void init()
	{
		rand = new Random();
		bc = new BitstringComparator();
		dpc = new DistancePairComparator();
		pop = new ArrayList<Bitstring>();
		targets = new ArrayList<Bitstring>();

		// Initialize population and targets
		for (int i = 0; i < POP_SIZE; i++)
			pop.add(new Bitstring(GENOME_LENGTH));
		for (int i = 0; i < TARGETS_SIZE; i++)
			targets.add(new Bitstring(GENOME_LENGTH));
	}

	public static void printB(ArrayList<Bitstring> list)
	{
		for (Bitstring b : list)
			System.out.print(b + "\t");
	}

	public static void printD(ArrayList<DistancePair> list)
	{
		for (DistancePair p : list)
			System.out.print(p + "\t");
	}


}