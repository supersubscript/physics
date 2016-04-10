import java.util.Arrays;

public enum Encoding
{
	GRAY, BINARY, CONSENSUS_BINARY, CONSENSUS_GRAY;

	/*
	 * Calculates the distance between two Bitstrings
	 */
	public double distance(Bitstring a, Bitstring b)
	{
		switch (this)
		{
		case GRAY:
			return Math.abs(gray(a) - gray(b));
		case BINARY:
			return Math.abs(binary(a) - binary(b));
		case CONSENSUS_BINARY:
			return Math.abs(consensusBinary(a) - consensusBinary(b));
		case CONSENSUS_GRAY:
			return Math.abs(consensusGray(a) - consensusGray(b));

		}
		return -1;
	}

	public double signDistance(Bitstring a, Bitstring b)
	{
		switch (this)
		{
		case GRAY:
			return gray(a) - gray(b);
		case BINARY:
			return binary(a) - binary(b);
		case CONSENSUS_BINARY:
			return Math.abs(consensusBinary(a) - consensusBinary(b));
		case CONSENSUS_GRAY:
			return Math.abs(consensusGray(a) - consensusGray(b));
		}
		return -1;
	}

	/**
	 * Reads a binary sequence in regular base-2 encoding. Note that the
	 * direction of reading is reversed!
	 */
	public static double binary(Bitstring t)
	{
		char[] str = new StringBuilder(t.toString()).reverse().toString()
				.toCharArray();
		double result = 0;
		for (int i = 0; i < t.size(); i++)
			if (str[i] == '1')
				result += 1 << i;
		return result;
	}

	/*
	 * Reads a binary sequence using the regular "binary reflected" Gray
	 * encoding. Note that the direction of reading is reversed!
	 */
	public static double gray(Bitstring t)
	{
		int bin = (int) binary(t);
		int result = bin;
		while ((bin >>>= 1) != 0)
			result ^= bin;
		return (double) result;
	}

	public static double consensusBinary(Bitstring t)
	{
		final boolean[] startSequence = new boolean[] { true, true, false, false,
				true, true };
		final int geneLength = 10;
		int length = startSequence.length;
		boolean[] sequence = t.getSequence();

		assert (t.size() > length + geneLength);

		double result = 0;
		int N = 0;

		startingPositions: for (int i = 0; i <= sequence.length - length
				- geneLength; i++)
		{
			for (int j = 0; j < startSequence.length; j++)
			{
				if (sequence[i + j] != startSequence[j])
					continue startingPositions;
			}
			i += length;
			result += binary(new Bitstring(
					Arrays.copyOfRange(sequence, i, i = i + geneLength)));
			N++;
		}
		return result / N;
	}

	public static double consensusGray(Bitstring t)
	{
		final boolean[] startSequence = new boolean[] { true, true, false, false,
				true, true };
		final int geneLength = 10;
		final int length = startSequence.length;
		boolean[] sequence = t.getSequence();

		assert (sequence.length >= length + geneLength);

		double result = 0;
		int N = 0;

		startingPositions: for (int i = 0; i <= sequence.length - length
				- geneLength; i++)
		{
			for (int j = 0; j < startSequence.length; j++)
			{
				if (sequence[i + j] != startSequence[j])
					continue startingPositions;
			}
			i += length;
			result += binary(new Bitstring(
					Arrays.copyOfRange(sequence, i, i = i + geneLength)));
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

		Bitstring bb = new Bitstring(
				"000110011000100111111101010000001111110000000");
		Bitstring bin = new Bitstring("0001001111");

		System.out.println(binary(bin));
		System.out.println(consensusBinary(bb));

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
