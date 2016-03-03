/*
 * Bitstring interpreter for strings of shorter length than 32 bits
 * */
public class BitInterpreter
{

	public BitInterpreter()
	{
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
				result += Math.pow(2, i);
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
		while ((bin >>>= 1) != 0) // Unintelligable code pulled from internet.
			result ^= bin;
		return result;
	}

	/*
	 * Count an interval of bits and determine value via voting principle. String
	 * length must be a multiple of interval.
	 */
	// TODO: Write this one
	public static double embeddedBinary(Bitstring t, short interval)
	{
		assert (t.size() % interval == 0);
		Bitstring decoded = new Bitstring(t.size() / interval);
		for (int i = 0; i < t.size();)
		{
			int found = 0;
			for (int j = 0; j < interval; j++)
			{
				if (t.get(i))
					found++;
				i++;
			}
			System.out.println(found);
			if (found >= interval / 2)
				decoded.set(i / interval, true);
		}
		return binary(decoded);
	}

	// Test

	public static void main(String[] args)
	{
		Bitstring t = new Bitstring(10);
		System.out.println(t);
		System.out.println(BitInterpreter.binary(t));
		System.out.println(BitInterpreter.gray(t));
//		System.out.println(BitInterpreter.embeddedBinary(t, (short) 4));
	}

}
