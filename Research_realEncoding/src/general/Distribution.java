package general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

/**
 * Abstract class that represents a probability distribution. Provides several
 * methods that return implementations.
 *
 */
@FunctionalInterface
public interface Distribution<N> extends Supplier<N>
{

	@Override
	public default N get()
	{
		return this.getRandom();
	}

	public N getRandom();

	public default Collection<N> getRandom(int amt)
	{
		Collection<N> result = new ArrayList<>();
		for (int i = 0; i < amt; i++)
			result.add(this.getRandom());
		return result;
	}

	public static Distribution<Integer> binomial(int n, double p)
	{
		return new Distribution<Integer>()
		{
			Random rand = new Random();

			@Override
			public Integer getRandom()
			{
				double log_q = Math.log(1.0 - p);
				int x = 0;
				double sum = 0;
				for (;;)
				{
					sum += Math.log(rand.nextDouble()) / (n - x);
					if (sum < log_q)
					{
						return x;
					}
					x++;
				}
			}
		};
	}

	public static <N> Distribution<N> constant(N n)
	{
		return new Distribution<N>()
		{
			@Override
			public N getRandom()
			{
				return n;
			}
		};
	}

	public static Distribution<Double> normal(double mu, double sigma)
	{
		return new Distribution<Double>()
		{
			Random rand = new Random();

			@Override
			public Double getRandom()
			{
				return rand.nextGaussian() * sigma + mu;
			}
		};
	}

	public static Distribution<Integer> poisson(double lambda)
	{
		return new Distribution<Integer>()
		{
			Random rand = new Random();

			@Override
			public Integer getRandom()
			{
				double L = Math.exp(-lambda);
				double p = 1.0;
				int k = 0;

				do
				{
					k++;
					p *= rand.nextDouble();
				} while (p > L);

				return (int) (k - 1);
			}
		};
	}

	public static Distribution<Double> uniform(double start, double end)
	{
		return new Distribution<Double>()
		{
			Random rand = new Random();

			@Override
			public Double getRandom()
			{
				return rand.nextDouble() * (end - start) + start;
			}
		};
	}

	public static Distribution<Integer> uniform(int start, int end)
	{
		return new Distribution<Integer>()
		{
			Random rand = new Random();

			@Override
			public Integer getRandom()
			{
				return rand.nextInt(end - start) + start;
			}
		};
	}

	public static <N> Distribution<N> uniform(N[] ns)
	{
		return new Distribution<N>()
		{
			Random rand = new Random();

			@Override
			public N getRandom()
			{
				return ns[rand.nextInt(ns.length)];
			}
		};
	}

	public static <N> Distribution<N> uniform(Collection<N> ns)
	{
		return new Distribution<N>()
		{
			Random	rand		= new Random();
			List<N>	targets	= new ArrayList<N>(ns);

			@Override
			public N getRandom()
			{
				if (targets.size() == 0)
					return null;
				return targets.get(rand.nextInt(ns.size()));
			}
		};
	}

	/**
	 * Get any finite distribution where the probability of any element is given
	 * by the provided weights map. Negative weights are regarded as zero. If all
	 * weights are zero, the result is a uniform distribution instead over all
	 * the key elements of the weights map. If the supplied map is empty or null,
	 * returns the constant distribution for null.
	 */
	public static <N> Distribution<N> weighted(Map<N, Double> weights)
	{
		if (weights == null || weights.isEmpty())
			return Distribution.constant(null);

		double sum = 0;
		for (double d : weights.values())
			sum += Math.max(0, d);
		if (sum == 0.)
			return Distribution.uniform(weights.keySet());

		NavigableMap<Double, N> partialWeights = new TreeMap<>();
		double cumsum = 0.;
		for (Map.Entry<N, Double> entry : weights.entrySet())
		{
			if (entry.getValue() <= Double.MIN_VALUE)
				continue;
			cumsum += entry.getValue() / sum;
			partialWeights.put(cumsum, entry.getKey());
		}

		return new Distribution<N>()
		{
			Random rand = new Random();

			@Override
			public N getRandom()
			{
				return partialWeights.ceilingEntry(rand.nextDouble()).getValue();
			}

			@Override
			public String toString()
			{
				return "Weighted distribution: " + weights;
			}
		};
	}

	public static <N> Distribution<N> weighted(List<N> ns, List<Double> weights)
	{
		if (ns == null || weights == null || ns.isEmpty() || weights.isEmpty())
			return constant(null);
		Map<N, Double> map = new HashMap<>();
		for (int i = 0; i < Math.min(ns.size(), weights.size()); i++)
			map.put(ns.get(i), weights.get(i));
		return Distribution.weighted(map);
	}

	public static <N> Distribution<N> weighted(Set<N> ns, ToDoubleFunction<N> f)
	{
		if (ns == null || f == null || ns.isEmpty())
			return constant(null);
		Map<N, Double> map = new HashMap<>();
		for (N n : ns)
			map.put(n, f.applyAsDouble(n));
		return Distribution.weighted(map);
	}

	public static <N> Distribution<N> weighted(Set<N> ns, Function<N, Double> f)
	{
		if (ns == null || f == null || ns.isEmpty())
			return constant(null);
		Map<N, Double> map = new HashMap<>();
		for (N n : ns)
			map.put(n, f.apply(n));
		return Distribution.weighted(map);
	}

}
