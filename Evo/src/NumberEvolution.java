import java.util.ArrayList;
import java.util.Random;

public class sorted
{
	// @formatter:off
	public static final int ITERATIONS = 100000; // # generations
	public static final int POP_SIZE = 30; // # ind. in pop
	public static final int TARGETS_SIZE = 30; // # targets
	public static final int GENOME_LENGTH = 10; // # bits in gen.
	public static final double MUTATE_PROB = 0.1; // for single bit

	private static Random rand;
	private static ArrayList<Bitstring> targets;
	private static ArrayList<Bitstring> pop;
	private static BitstringComparator bc;
//	private static DistancePairComparator dpc;
	// @formatter:on

	public static void main(String[] args)
	{
		// Init stuff
		rand = new Random();
		bc = new BitstringComparator();
		dpc = new DistancePairComparator();
		pop = new ArrayList<Bitstring>();
		targets = new ArrayList<Bitstring>();
		ot = new ArrayList<Bitstring>();
		oi = new ArrayList<Bitstring>();

		// Initialize population and targets
		for (int i = 0; i < POP_SIZE; i++)
			pop.add(new Bitstring(GENOME_LENGTH));
		for (int i = 0; i < TARGETS_SIZE; i++)
			targets.add(new Bitstring(GENOME_LENGTH));

		// Actual simulation
		for (int t = 0; t < ITERATIONS; t++)
		{
			System.out.print(t + "\t");
			int index = rand.nextInt(POP_SIZE);

			Bitstring rp = new Bitstring(pop.get(index)); // copy
			double indBefore = indDist(rp, targets, oi, ot);
			Operator.mutate_single(rp, MUTATE_PROB);
			double indAfter = indDist(rp, targets, ot, ot);

			if (indBefore > indAfter)
				pop.set(index, rp);

			System.out.print(indBefore - indAfter + "\t");

			double cost;
			if (targets.size() > pop.size())
				cost = totalDist(pop, targets);
			else
				cost = totalDist(targets, pop);
			System.out.println(cost);
		}
	}

	// return closest fit for a given individual b
	public static double indDist(Bitstring b, ArrayList<Bitstring> targets,
	      ArrayList<Bitstring> ot, double prev)
	{
		targets.sort(bc);
		double dist = Double.MAX_VALUE;
		for (int i = 0; i < targets.size(); i++)
		{
			Bitstring b2 = targets.get(i);
			double nextDist = dist(b, b2);
			if (dist < nextDist)
				break;
			dist = nextDist;
			if (dist == 0)
			{
				ot.add(b2);
				oi.add(b);
				return dist;
			}
		}
		return dist;
	}

	// Insert shortest list first
	public static double totalDist(ArrayList<Bitstring> s,
	      ArrayList<Bitstring> l)
	{
		s.sort(bc);
		l.sort(bc);
		ArrayList<DistancePair> pairs = new ArrayList<DistancePair>();
		ArrayList<Bitstring> lfound = new ArrayList<Bitstring>();
		ArrayList<Bitstring> lnew = new ArrayList<Bitstring>();
		ArrayList<Bitstring> snew = new ArrayList<Bitstring>();

		double td = 0;
		int i = 0;
		int j = 0;

		while (j < l.size() && i < s.size())
		{
			Bitstring b = s.get(i);
			Bitstring b2 = l.get(j);
			double dist = dist(b, b2);

			// Give this l to all the remaining
			if (j == l.size() - 1)
			{
				pairs.add(new DistancePair(b, b2, dist));
				i++;
			} else
			{
				// Check if next item is smaller
				double nextDist = dist(b, l.get(j + 1));
				if (nextDist < dist)
					j++;
				else
				{
					pairs.add(new DistancePair(b, b2, dist));
					i++;
				}
			}
		}

		// first -- s
		// second -- l
		pairs.sort(dpc);
		// System.out.println();
		// for (DistancePair p : pairs)
		// System.out.println(
		// p.getFirst() + "\t" + p.getSecond() + "\t" + p.getDistance());
		// System.out.println();

		for (DistancePair pair : pairs)
			if (lfound.contains(pair.getSecond()))
				snew.add(pair.getFirst());
			else
			{
				lfound.add(pair.getSecond());
				td += pair.getDistance();
			}
		for (Bitstring b : l)
			if (!lfound.contains(b))
				lnew.add(b);
		if (snew.size() != 0)
			td += totalDist(snew, lnew);
		return td;
	}

	public static double dist(Bitstring a, Bitstring b)
	{
		return Math.abs(BitInterpreter.binary(a) - BitInterpreter.binary(b));
	}
}
