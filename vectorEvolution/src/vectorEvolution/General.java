package vectorEvolution;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Stream;

public class General
{
	static Random rand = new Random();

	/* Sort a non-sorting map by value. */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map)
	{
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Entry<K, V>> st = map.entrySet().stream();
		st.sorted(Comparator.comparing(e -> e.getValue()))
				.forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
		return result;
	}

	public static int geometric(double p)
	{
		assert (p >= 0.0 && p <= 1.0);
		return (int) Math
				.floor(Math.log(1 - rand.nextDouble()) / Math.log(1 - p));
	}

	public static void main(String[] args)
	{
		for (int i = 0; i < 100; i++)
		{
			System.out.println(geometric(1));
		}
	}
}
