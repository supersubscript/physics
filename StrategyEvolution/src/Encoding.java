
public enum Encoding
{
	GRAY, BINARY;

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

	public static void main(String[] args)
	{
		Bitstring t = new Bitstring(new boolean[] { true, true });
		Bitstring tt = new Bitstring(new boolean[] { true, true});

		System.out.println(t + "\t" + tt);
		System.out.println("binary\t" + binary(t) + "\t" + binary(tt));
		System.out.println("gray\t" + gray(t) + "\t" + gray(tt));
		Encoding er = Encoding.BINARY;
		Encoding ee = Encoding.GRAY;
		System.out.println("bindist\t" + er.distance(t, tt));
		System.out.println("gdist\t" + ee.distance(t, tt));
		// System.out.println(BitInterpreter.embeddedBinary(t, (short) 4));
	}

}
