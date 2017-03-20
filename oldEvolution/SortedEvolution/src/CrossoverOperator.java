
import java.util.Random;

@FunctionalInterface
public interface CrossoverOperator
{
	static Random rand = new Random();

	Bitstring apply(Bitstring t, Bitstring u);

	public default void doNothing()
	{
	};

	/**
	 * CrossOverOperator that simply returns a random parent without crossover.
	 */
	public static CrossoverOperator none()
	{
		return new CrossoverOperator()
		{

			@Override
			public Bitstring apply(Bitstring t, Bitstring u)
			{
				return rand.nextBoolean() ? t : u;
			}
		};
	}

}