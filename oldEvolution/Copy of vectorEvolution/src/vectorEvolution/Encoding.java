package vectorEvolution;

import java.util.Arrays;

public enum Encoding
{
	GRAY, BINARY, CONSENSUS_BINARY, CONSENSUS_GRAY;

	public double value(Bitstring a)
	{
		return MultiEvolution.bitstringValues.get(a.toString());
	}

	public double value(String b)
	{
		boolean[] a = new boolean[b.length()];
		for (int i = 0; i < a.length; i++)
			if (b.charAt(i) == '1')
				a[i] = true;

		switch (this)
		{
		case GRAY:
			return gray(a);
		case BINARY:
			return binary(a);
		case CONSENSUS_BINARY:
			return consensusBinary(a);
		case CONSENSUS_GRAY:
			return consensusGray(a);
		default:
			throw new IllegalArgumentException();
		}
	}

	/*
	 * Calculates the distance between two Bitstrings
	 */
	// public double distance(Bitstring s, Bitstring t)
	// {
	// switch (this)
	// {
	// case GRAY:
	// return Math.abs(gray(s) - gray(t));
	// case BINARY:
	// return Math.abs(binary(s) - binary(t));
	// case CONSENSUS_BINARY:
	// return Math.abs(consensusBinary(s) - consensusBinary(t));
	// case CONSENSUS_GRAY:
	// return Math.abs(consensusGray(s) - consensusGray(t));
	// default:
	// throw new IllegalArgumentException();
	// }
	// }

	/**
	 * Reads a binary sequence in regular base-2 encoding. Note that the
	 * direction of reading is reversed!
	 */
	public static double binary(boolean[] t)
	{
		double result = 0;
		for (int i = 0; i < t.length; i++)
			if (t[t.length - 1 - i])
				result += 1 << i;
		return result;
	}

	/*
	 * Reads a binary sequence using the regular "binary reflected" Gray
	 * encoding. Note that the direction of reading is reversed!
	 */
	public static double gray(boolean[] t)
	{
		int bin = (int) binary(t);
		int result = bin;
		while ((bin >>>= 1) != 0)
			result ^= bin;
		return (double) result;
	}

	public static double consensusBinary(boolean[] t)
	{
		final boolean[] startSequence = new boolean[] { true, true, false, false,
				true, true };
		final int geneLength = 10;
		int length = startSequence.length;

		assert (t.length > length + geneLength);

		double result = 0;
		int N = 0;

		startingPositions: for (int i = 0; i <= t.length - length
				- geneLength; i++)
		{
			for (int j = 0; j < startSequence.length; j++)
			{
				if (t[i + j] != startSequence[j])
					continue startingPositions;
			}
			i += length;
			result += binary(Arrays.copyOfRange(t, i, i = i + geneLength));
			N++;
		}
		double value = result / N;
		return Double.isNaN(value) ? Double.MAX_VALUE : value;
	}

	public static double consensusGray(boolean[] t)
	{
		final boolean[] startSequence = new boolean[] { true, true, false, false,
				true, true };
		final int geneLength = 10;
		int length = startSequence.length;

		assert (t.length > length + geneLength);

		double result = 0;
		int N = 0;

		startingPositions: for (int i = 0; i <= t.length - length
				- geneLength; i++)
		{
			for (int j = 0; j < startSequence.length; j++)
			{
				if (t[i + j] != startSequence[j])
					continue startingPositions;
			}
			i += length;
			result += gray(Arrays.copyOfRange(t, i, i = i + geneLength));
			N++;
		}
		double value = result / N;
		return Double.isNaN(value) ? Double.MAX_VALUE : value;
	}

	public static void main(String[] args)
	{
		Bitstring t = new Bitstring(new boolean[] { false, true });
		Bitstring tt = new Bitstring(new boolean[] { true, false });

		Bitstring bb = new Bitstring(
				"000110011000100111111101010000001111110000000");
		Bitstring bin = new Bitstring("0001001111");

		System.out.println(binary(bin.getSequence()));
		System.out.println(consensusBinary(bb.getSequence()));

		System.out.println(t + "\t" + tt);
		System.out.println("binary\t" + binary(t.getSequence()) + "\t"
				+ binary(tt.getSequence()));
				// System.out.println("gray\t" + gray(t) + "\t" + gray(tt));
				// Encoding er = Encoding.BINARY;
				// Encoding ee = Encoding.GRAY;
				// System.out.println("bindist\t" + er.distance(t, tt));
				// System.out.println("gdist\t" + ee.distance(t, tt));
				// System.out.println("bindist\t" + er.signDistance(t, tt));
				// System.out.println("gdist\t" + ee.signDistance(t, tt));

		// System.out.println(BitInterpreter.embeddedBinary(t, (short) 4));
	}

}
