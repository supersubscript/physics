package vectorEvolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Bitstring extends Organism implements Comparable<Bitstring>
{
	static private final Random	rand	= new Random();
	private boolean[]					t;
	private double						value;
	private int genes = 0; 

	// Initializes Bitstring with specified length
	public Bitstring(int len)
	{
		this.t = new boolean[len];
		for (int i = 0; i < t.length; i++)
			this.t[i] = rand.nextBoolean();
		Pair<Double, Integer> vals = MutLogTest.encoding.value(this.t);
		this.value = vals.getElement0();
		this.genes = vals.getElement1();
	}

	// Initializes bitstring with given sequence
//	public Bitstring(String sequence)
//	{
//		char[] seq = sequence.toCharArray();
//		t = new boolean[seq.length];
//		for (int i = 0; i < t.length; i++)
//			if (seq[i] == '1')
//				t[i] = true;
//	}

	// Initializes bitstring with given sequence
	public Bitstring(boolean[] t)
	{
		this.t = t;
		Pair<Double, Integer> vals = MutLogTest.encoding.value(this.t);
		this.value = vals.getElement0();
		this.genes = vals.getElement1();
	}

	public Bitstring(boolean[] t, double value, int genes)
	{
		this.t = t;
		this.value = value;
		this.genes = genes;
	}

	@Override
	public Bitstring clone()
	{
		return new Bitstring(Arrays.copyOf(this.t, this.t.length), this.value, this.genes);
	}

	public Bitstring(Bitstring b)
	{
		this.t = b.clone().getSequence();
		this.value = b.getValue();
		this.genes = b.getGenes();
	}

	public boolean[] getSequence()
	{
		return this.t;
	}

	public void flip(int i)
	{
		assert i > -1 && i < t.length;
		t[i] = !t[i];
	}

	public int size()
	{
		return t.length;
	}

	public boolean get(int i)
	{
		assert i > -1 && i < t.length;
		return t[i];
	}

	public void set(int i, boolean val)
	{
		assert i > -1 && i < t.length;
		t[i] = val;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(t.length);
		for (int i = 0; i < t.length; i++)
			if (t[i])
				sb.append(1);
			else sb.append(0);
		return sb.toString();
	}

	@Override
	public int hashCode()
	{
		return t.hashCode();
	}

	public boolean isSame(Bitstring b)
	{
		return Arrays.equals(this.t, b.getSequence());
	}

	// @Override
	// public boolean equals(Object b)
	// {
	// return this.hashCode() == b.hashCode();
	// }

	/* Split Bitstring into set of new bitstrings */
	public ArrayList<Bitstring> split(int length)
	{
		assert t.length % length == 0;
		ArrayList<Bitstring> temp = new ArrayList<Bitstring>();

		int piece = length;
		int j = 0;
		while (piece <= t.length)
		{
			boolean[] gene = new boolean[length];
			for (int i = 0; i < length; i++)
			{
				gene[i] = t[j];
				j++;
			}
			temp.add(new Bitstring(gene));
			piece += length;

		}
		return temp;
	}

	/* Retrieve a subsequence of a Bitstring. */
	public static Bitstring subsequence(Bitstring b, int from, int to)
	{
		assert (from > 0 && from < b.size() && to >= from && to < b.size());
		return new Bitstring(Arrays.copyOfRange(b.getSequence(), from, to));
	}

	public static int hammingDistance(Bitstring a, Bitstring b)
	{
		boolean[] ab = a.getSequence();
		boolean[] bb = b.getSequence();

		int shorter = Math.min(a.size(), b.size());
		int longest = Math.max(a.size(), b.size());

		int result = 0;
		for (int i = 0; i < shorter; i++)
			if (ab[i] != bb[i])
				result++;

		result += longest - shorter;

		return result;
	}

	/* Randomizes Bitstring */
	public Bitstring randomize()
	{
		Random rand = new Random();
		for (int i = 0; i < t.length; i++)
			t[i] = rand.nextBoolean();
		return this;
	}

	public static double distance(Bitstring a, Bitstring b)
	{
		return Math.abs(a.getValue() - b.getValue());
	}

	public double getValue()
	{
		return this.value;
	}

	public static void main(String[] args)
	{

		// Bitstring a = new Bitstring("1111");
		// Bitstring b = new Bitstring("1111");

		// System.out.println(a.equals(b));

	}

	@Override
	public int compareTo(Bitstring o)
	{
		return (int) (this.value - o.getValue());
	}

	public int getGenes()
	{
		return genes;
	}

	public void setGenes(int genes)
	{
		this.genes = genes;
	}
}
