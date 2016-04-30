package vectorEvolution;

import java.util.Arrays;

public enum Encoding
{
	GRAY, BINARY, CONSENSUS_BINARY, CONSENSUS_GRAY;

	public double value(boolean[] a)
	{
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

	static final boolean[]	startSequence	= new boolean[] { true, true, false,
			false, true, true };
	static final double		length			= startSequence.length;
	static final int			geneLength		= 10;

	public static double consensusBinary(boolean[] t)
	{
		assert (t.length > length + geneLength);

		double result = 0;
		int N = 0;

		int j = 0;
		for (int i = 0; i <= t.length - length - geneLength; i++)
		{
			if (j == startSequence.length)
			{
				j = 0;
				result += binary(Arrays.copyOfRange(t, i, i = i + geneLength));
				N++;
			} else if (t[i] == startSequence[j])
				j++;
			else if (j < 2 || j > 3)
				j = 0;
			else if (j == 3)
				j = 1;
		}
		double val = result / N;
		// System.out.print(N + "\t" + Math.floor(val*100)/100 +"\t");
		return Double.isNaN(val) ? 10000 : val;
	}

	public static double consensusGray(boolean[] t)
	{
		assert (t.length > length + geneLength);

		double result = 0;
		int N = 0;

		int j = 0;
		for (int i = 0; i <= t.length - length - geneLength; i++)
		{
			if (j == startSequence.length)
			{
				j = 0;
				result += gray(Arrays.copyOfRange(t, i, i = i + geneLength));
				N++;
			} else if (t[i] == startSequence[j])
				j++;
			else if (j < 2 || j > 3)
				j = 0;
			else if (j == 3)
				j = 1;
		}
		double val = result / N;
		return Double.isNaN(val) ? 10000 : val;
	}

	//
	public static void main(String[] args)
	{
		Bitstring t = new Bitstring(new boolean[] { false, true });
		Bitstring tt = new Bitstring(new boolean[] { true, false });
		//
		// Bitstring bb = new Bitstring(
		// "000110011000100111111101010000001111110000000");
		// Bitstring bin = new Bitstring("0001001111");
		// //
		// System.out.println(binary(bin.getSequence()));
		// System.out.println(consensusBinary(bb.getSequence()));
		//
		System.out.println(t + "\t" + tt);
		System.out.println("binary\t" + binary(t.getSequence()) + "\t"
				+ binary(tt.getSequence()));
		System.out.println(
				"gray\t" + gray(t.getSequence()) + "\t" + gray(tt.getSequence()));

		// System.out.println("gray\t" + gray(t) + "\t" + gray(tt));
		// // Encoding er = Encoding.BINARY;
		// // Encoding ee = Encoding.GRAY;
		// // System.out.println("bindist\t" + er.distance(t, tt));
		// // System.out.println("gdist\t" + ee.distance(t, tt));
		// // System.out.println("bindist\t" + er.signDistance(t, tt));
		// // System.out.println("gdist\t" + ee.signDistance(t, tt));
		//
		// System.out.println(BitInterpreter.embeddedBinary(t, (short) 4));
	}

}
