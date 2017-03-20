import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

@FunctionalInterface
public interface FitnessFunction<GeneNetwork>
{
	public double applyDirectly(GeneNetwork o);

	public default double apply(ArrayList<GeneNetwork> pop, GeneNetwork o)
	{
		double sum = 0.;
		for (GeneNetwork p : pop)
			sum += Math.max(0., this.applyDirectly(p));

		return sum == 0 ? 1. / pop.size()
		      : Math.max(0., this.applyDirectly(o) / sum);
	}

	public default Map<GeneNetwork, Double> apply(ArrayList<GeneNetwork> pop)
	{
		double sum = 0.;
		for (GeneNetwork p : pop)
			sum += Math.max(0., this.applyDirectly(p));
		Map<GeneNetwork, Double> result = new HashMap<>();

		for (GeneNetwork o : pop)
			result.put(o, sum == 0 ? 1. / pop.size()
			      : Math.max(0., this.applyDirectly(o) / sum));
		return result;
	}

	public default Map<GeneNetwork, Double> applyDirectlySortedMap(
	      ArrayList<GeneNetwork> pop)
	{
		Map<GeneNetwork, Double> result = new HashMap<>();
		for (GeneNetwork o : pop)
			result.put(o, Math.max(0., this.applyDirectly(o)));
		result = this.sortByValue(result);

		return result;
	}

	// Sort map after value (cannot be used on TreeMap)
	public default <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
	      Map<K, V> map)
	{
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Comparator.comparing(e -> e.getValue()))
		      .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

		return result;
	}

}