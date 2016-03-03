package settings;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import general.Distribution;
import organisms.Organism;
import physics.World;

/**
 * Strategy interface for selecting Organisms from a population, such as when
 * deciding which organism dies or splits.
 */
@FunctionalInterface
public interface SelectionRule<O extends Organism<O>>
      extends Function<World<O>, Collection<O>>
{

	/**
	 * Alias of {@link #select(World)}.
	 */
	@Override
	default Collection<O> apply(World<O> t)
	{
		return this.select(t);
	}

	/**
	 * Which Organisms to select, out of all the Organisms in the World.
	 */
	public Collection<O> select(World<O> w);

	/**
	 * Whether one specific organism is selected.
	 */
	// Default implementation computes killWhich for the whole population and
	// extracts one.
	// Override to something more efficient when possible.
	public default boolean selectQ(World<O> w, O o)
	{
		return this.select(w).contains(o);
	}

	/**
	 * A selection rule that selects all Organisms.
	 */
	public static <O extends Organism<O>> SelectionRule<O> trivialTrue()
	{
		return new SelectionRule<O>()
		{
			@Override
			public boolean selectQ(World<O> w, O o)
			{
				return true;
			}

			@Override
			public Set<O> select(World<O> w)
			{
				return w.getPopulation();
			}
		};
	}

	/**
	 * A rule that selects random Organisms. The probability that any Organism
	 * will be selected is exactly rate.
	 */
	public static <O extends Organism<O>> SelectionRule<O> constantRate(
	      double rate)
	{
		return new SelectionRule<O>()
		{
			Random rand = new Random();

			@Override
			public boolean selectQ(World<O> w, O o)
			{
				return rate > this.rand.nextDouble();
			}

			@Override
			public Set<O> select(World<O> w)
			{
				Set<O> result = new HashSet<>();
				for (O o : w.getPopulation())
				{
					if (selectQ(w, o))
						result.add(o);
				}
				return result; // TODO make more efficient
			}
		};
	}

	/**
	 * A rule that selects random Organisms. The probability of being selected
	 * goes up proportionally with population size, i.e. probability of being
	 * selected is rate + populationsize*raterate However, selected Organisms are
	 * removed from the pool. In this way, high population sizes are penalized,
	 * but not so much that the probability of being selected can exceed 1.
	 */
	public static <O extends Organism<O>> SelectionRule<O> proportionalRate(
	      double rate, double raterate)
	{
		return new SelectionRule<O>()
		{
			Random rand = new Random();

			@Override
			public Set<O> select(World<O> w)
			{
				Set<O> result = new HashSet<>();
				for (O o : w.getPopulation())
				{
					if (rate + (w.getPopulation().size() - result.size())
		               * raterate > rand.nextDouble())
						result.add(o);
				}
				return result;
			}
		};
	}

	/**
	 * Represents a fitness-based mechanism. The probability is defined as an
	 * arithmetic manipulation of the fitness function, more specifically f' = a
	 * * f^p + b Where f' is the probability of being selected, and f is the
	 * calculated fitness value (the total sum of all fs in pop is 1)
	 */
	public static <O extends Organism<O>> SelectionRule<O> directFitness(
	      FitnessFunction<O> fitness, double p, double a, double b)
	{
		return new SelectionRule<O>()
		{
			Random rand = new Random();

			@Override
			public Set<O> select(World<O> w)
			{
				Set<O> result = new HashSet<>();
				Map<O, Double> fs = fitness.apply(w);
				for (O o : w.getPopulation())
				{
					if (a * Math.pow(fs.get(o), p) + b < this.rand.nextDouble())
						result.add(o);
				}
				return result;
			}
		};
	}

	/**
	 * Direct fitness-based selection mechanism. See: {@link #SelectionRule} with
	 * p=1, a=1, b=0.
	 */
	public static <O extends Organism<O>> SelectionRule<O> directFitness(
	      FitnessFunction<O> fitness)
	{
		return directFitness(fitness, 1., 1., 0.);
	}

	/**
	 * Select a random sample from the population. Probability of being selected
	 * is proportional to fitness function. Random selection is done with
	 * replacement.
	 */
	public static <O extends Organism<O>> SelectionRule<O> selectProportion(
	      FitnessFunction<O> fitness, double proportion)
	{
		double internalProportion = Math.max(proportion, 0.);
		return new SelectionRule<O>()
		{
			@Override
			public Collection<O> select(World<O> w)
			{
				Map<O, Double> fit = fitness.apply(w);
				Distribution<O> randomizer = Distribution.weighted(fit);
				return randomizer.getRandom((int) Math
		            .floor(internalProportion * w.getPopulation().size()));
			}

			@Override
			public String toString()
			{
				return "Selection rule (proportion " + proportion + "; fitness "
		            + fitness + ")";
			}
		};
	}

	/**
	 * Represents a mechanism based on the number of neighbors. All Organisms
	 * with i or less neighbors are selected.
	 */
	public static <O extends Organism<O>> SelectionRule<O> maxNeighbors(int i,
	      int dist)
	{
		return new SelectionRule<O>()
		{
			@Override
			public Set<O> select(World<O> w)
			{
				LocationFilter fullTest = LocationFilter.full();
				Set<O> result = new HashSet<>();
				for (O o : w.getPopulation())
				{
					int neighbors = fullTest.count(w,
		               w.getLocation(o).neighbors(dist));
					if (neighbors <= i)
						result.add(o);
				}
				return result;
			}
		};
	}

	/**
	 * Represents a mechanism based on the number of neighbors. All Organisms
	 * with i or more neighbors are selected.
	 */
	public static <O extends Organism<O>> SelectionRule<O> minNeighbors(int i,
	      int dist)
	{
		return new SelectionRule<O>()
		{
			@Override
			public Set<O> select(World<O> w)
			{
				LocationFilter fullTest = LocationFilter.full();
				Set<O> result = new HashSet<>();
				for (O o : w.getPopulation())
				{
					int neighbors = fullTest.count(w,
		               w.getLocation(o).neighbors(dist));
					if (neighbors >= i)
						result.add(o);
				}
				return result;
			}
		};
	}

	/**
	 * Selection rule that uses direct application of a given function. The
	 * function returns the probability for each Organism that they will be
	 * selected. This is evaluated completely independently for each Organism.
	 */
	public static <O extends Organism<O>> SelectionRule<O> directApply(
	      Function<O, Double> fun)
	{
		return new SelectionRule<O>()
		{
			Random rand = new Random();

			@Override
			public Collection<O> select(World<O> w)
			{
				Collection<O> result = new HashSet<>();
				for (O o : w.getPopulation())
					if (fun.apply(o) > rand.nextDouble())
						result.add(o);
				return result;
			}
		};
	}

	public static <O extends Organism<O>> SelectionRule<O> directApply(
	      Predicate<O> fun)
	{
		return new SelectionRule<O>()
		{
			@Override
			public Collection<O> select(World<O> w)
			{
				Collection<O> result = new HashSet<>();
				for (O o : w.getPopulation())
					if (fun.test(o))
						result.add(o);
				return result;
			}
		};
	}
}