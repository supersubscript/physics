import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Bitstring extends Organism
{
	private boolean[] t;

	// Initializes Bitstring with specified length
	public Bitstring(int len)
	{
		this.t = new boolean[len];
	}

	// Initializes bitstring with given sequence
	public Bitstring(String sequence)
	{
		char[] seq = sequence.toCharArray();
		t = new boolean[seq.length];
		for (int i = 0; i < t.length; i++)
			if (seq[i] == '1')
				t[i] = true;
	}

	
	// Initializes bitstring with given sequence
	public Bitstring(boolean[] t)
	{
		this.t = t;
	}

	@Override
	public Bitstring clone()
	{
		return new Bitstring(Arrays.copyOf(t, t.length));
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

//	@Override
//	public boolean equals(Object b)
//	{
//		return this.hashCode() == b.hashCode();
//	}

	/* Split Bitstring into set of new bitstrings */
	public ArrayList<Bitstring> split(int length)
	{
		assert t.length % length == 0;
		ArrayList<Bitstring> temp = new ArrayList<Bitstring>();

		int lol = length;
		int j = 0;
		while (lol <= t.length)
		{
			Bitstring b = new Bitstring(length);
			for (int i = 0; i < length; i++)
			{
				b.set(i, t[j]);
				j++;
			}
			temp.add(b);
			lol += length;

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

	
	public static void main(String[] args)
	{
		
		Bitstring a = new Bitstring("1111");
		Bitstring b = new Bitstring("1111");
		System.out.println(a.equals(b));
		
		
	}
}
