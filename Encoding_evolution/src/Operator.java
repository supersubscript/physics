import java.util.ArrayList;
import java.util.Random;

public class Operator
{
	private static Random rand = new Random();

	public static void mutate_single(Bitstring t, double mutateProb)
	{
		for (int i = 0; i < t.size(); i++)
			if (rand.nextDouble() < mutateProb)
				t.flip(i);
	}
}
