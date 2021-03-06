import java.util.Random;

@FunctionalInterface
public interface MutationOperator
{
	static Random rand = new Random();

	public Bitstring mutate(Bitstring seq);

	/**
	 * Trivial implementation. Does nothing.
	 */
	public static MutationOperator trivial()
	{
		return new MutationOperator()
		{
			@Override
			public Bitstring mutate(Bitstring seq)
			{
				return seq.clone();
			}
		};
	}

	public static MutationOperator probFlip(double prob)
	{
		return new MutationOperator()
		{
			@Override
			public Bitstring mutate(Bitstring seq)
			{
				Bitstring b = seq.clone();

				for (int i = 0; i < b.size(); i++)
					if (rand.nextDouble() < prob)
						b.flip(i);
				
				return b;
			}
		};
	}
}