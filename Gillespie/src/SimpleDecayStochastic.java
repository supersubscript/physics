import java.util.Random;

public class SimpleDecayStochastic
{
	// Necessary parameters
	static double[]		vals;

	// RK specifics
	static final Random	rand	= new Random();
	static final double	ttot	= 100;
	static double			a0;
	static double			mu;
	static double			decayRate;

	public static void main(String[] args)
	{
		decayRate = 0.5;
		// Initialize stuff
		vals = new double[1];
		for (int i = 0; i < vals.length; i++)
			vals[i] = 1000;

		System.out.print(0 + "\t");
		for (int i = 0; i < vals.length; i++)
			System.out.print(vals[i] + "\t");
		System.out.println();

		double t = 0;
		// Simulate development process.
		while (t < ttot)
		{
			a0 = -decayRate * vals[0]; // = a1: only one thing happens
			double r1 = rand.nextDouble();
			t += -1. / a0 * Math.log(1. / r1);
			vals[0] += a0;
			// Print output. Disallow negative values.
			System.out.print(t + "\t");
			for (int j = 0; j < vals.length; j++)
			{
				if (vals[j] < 0)
					vals[j] = 0;
				System.out.print(vals[j] + "\t");
			}
			System.out.println();
		}
	}
}