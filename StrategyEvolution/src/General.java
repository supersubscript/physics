import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class General
{

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

}
