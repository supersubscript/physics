package settings;

import java.util.Random;
import java.util.function.BiFunction;

import general.Sequence;

/**
 * Strategy interface that represents the crossover operator of a digital
 * population.
 */
@FunctionalInterface
public interface CrossOverOperator
		extends BiFunction<Sequence, Sequence, Sequence>
{

	/**
	 * CrossOverOperator that simply returns a random parent without crossover.
	 */
	public static CrossOverOperator none()
	{
		return new CrossOverOperator()
		{
			Random rand = new Random();

			@Override
			public Sequence apply(Sequence t, Sequence u)
			{
				return rand.nextBoolean() ? t : u;
			}
		};
	}

	/**
	 * CrossOverOperator that finds the single longest common subsequence of the
	 * two parent genomes (with the given amount of mismatches allowed). The
	 * resulting Sequence after cross-over contains this common sequence plus the
	 * sequence to the left taken from a random parent, and the sequence to the
	 * right taken from a random parent. Note: 1/2 probability that this operator
	 * simply returns one of the parents.
	 */
	public static CrossOverOperator singleSynapse(int mismatches)
	{
		return new CrossOverOperator()
		{
			Random rand = new Random();

			@Override
			public Sequence apply(Sequence t, Sequence u)
			{
				Sequence.Overlap overlap = t.LCSS(u, mismatches);
				Sequence left = rand.nextBoolean() ? t.get(0, overlap.leftStart)
						: u.get(0, overlap.rightStart);
				Sequence middle = t.get(overlap.leftStart,
						overlap.leftStart + overlap.length);
				Sequence right = rand.nextBoolean()
						? t.get(overlap.leftStart + overlap.length, t.length())
						: u.get(overlap.rightStart + overlap.length, u.length());
				return Sequence.cat(left, middle, right);
			}
		};
	}

}
