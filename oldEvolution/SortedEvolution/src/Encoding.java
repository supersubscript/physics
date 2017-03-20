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

		}
		return -1;
	}

	/**
	 * Reads a binary sequence in regular base-2 encoding. Note that the
	 * direction of reading is reversed!
	 */
	public static double binary(boolean[] t)
	{
		double result = 0;
		for (int i = 0; i < t.length; i++)
			if (t[i])
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
		return result / N;
	}

	public static double consensusGray(boolean[] t)
	{
		final boolean[] startSequence = new boolean[] { true, true, false, false,
				true, true };
		final int geneLength = 10;
		final int length = startSequence.length;

		assert (t.length >= length + geneLength);

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
		return result / N;
	}

	// for (int i = 0; i < t.size(); i++)
	// {
	// if (i + length + geneLength > t.size())
	// break;
	//
	// if ()
	// {
	// N++;
	// result += gray(new Bitstring(Arrays.copyOfRange(sequence,
	// i + length, i + length + geneLength)));
	// i += length + geneLength;
	// }
	// }
	//
	// }

	public static void main(String[] args)
	{
		// Bitstring t = new Bitstring(new boolean[] { false, true });
		// Bitstring tt = new Bitstring(new boolean[] { true, false });

		// System.out.println(t + "\t" + tt);
		// System.out.println("binary\t" + binary(t) + "\t" + binary(tt));
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
