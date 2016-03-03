import java.util.Random;

public class stochastic
{
	// Setup
	static double[]		vals;
	static double[]		k1, k2, k3, k4;

	// Parameter values
	static final double	h		= .005;

	static final double	S1		= 0.4;			// fox log
	static final double	S2		= 0.7;			// fox feedmax
	static final double	S3		= 0.04;			// rabbit log
	static final double	S4		= 0.035;			// rabbit feedmax
	static final double	S5		= 0.03;			// grass log

	static final double	a1		= 100.8;			// fox grow
	static final double	a2		= .01;			// fox death
	static final double	a3		= 2;				// foxes eat rabbits
	static final double	a4		= 15;				// rabbit growth
	static final double	a5		= 2.3;			// rabbit decay
	static final double	a6		= .9;				// grass eaten by rabbits
	static final double	a7		= 300.2;			// grass growth

	static final double	ttot	= 50;
	static final double	STEPS	= ttot / h;
	static final Random	rand	= new Random();

	public static void main(String[] args)
	{
		// Initialize stuff
		vals = new double[3];
		vals[0] = 2;
		vals[1] = 30;
		vals[2] = 100;

		System.out.print(0 + "\t");
		for (int i = 0; i < vals.length; i++)
			System.out.print(vals[i] + "\t");
		System.out.println();

		// Simulate development process.
		double t = 0;
		double[] a = new double[12];
		while (t < ttot)
		{
			double r1 = rand.nextDouble();
			double r2 = rand.nextDouble();
			// a[0] = a1 * vals[0] * vals[1] * (1 - S1 * vals[0])
			// / (1 + S2 * vals[1]);
			a[0] = a1 * vals[0] * vals[1] / (1 + S2 * vals[1]);
			a[1] = a1 * vals[0] * vals[1] * S1 * vals[0] / (1 + S2 * vals[1]);
			a[2] = a2 * vals[0];

			a[3] = a3 * vals[0] * vals[1] / (1 + S2 * vals[1]);
			a[4] = a3 * vals[0] * vals[1] * S1 * vals[0] / (1 + S2 * vals[1]);
			a[5] = a4 * vals[1] * vals[2] / (1 + S4 * vals[2]);
			a[6] = a4 * vals[1] * vals[2] * S3 * vals[1] / (1 + S4 * vals[2]);
			a[7] = a5 * vals[1];

			a[8] = a6 * vals[1] * vals[2] / (1 + S4 * vals[2]);
			a[9] = a6 * vals[1] * vals[2] * S3 * vals[1] / (1 + S4 * vals[2]);
			a[10] = a7 * vals[2];
			a[11] = a7 * vals[2] * S5 * vals[2];
			double a0 = 0;
			for (int i = 0; i < a.length; i++)
				a0 += a[i];

			double[] p = new double[a.length];
			// Simulate development process
			p[0] = a[0] / a0;

			for (int i = 1; i < a.length; i++)
				p[i] = p[i - 1] + a[i] / a0;

			t += 1. / a0 * Math.log(1. / r1);

			if (r2 < p[0])
				vals[0]++;
			else if (r2 < p[1])
				vals[0]--;
			else if (r2 < p[2])
				vals[0]--;

			else if (r2 < p[3])
				vals[1]++;
			else if (r2 < p[4])
				vals[1]--;
			else if (r2 < p[5])
				vals[1]++;
			else if (r2 < p[6])
				vals[1]--;
			else if (r2 < p[7])
				vals[1]--;

			else if (r2 < p[8])
				vals[2]++;
			else if (r2 < p[9])
				vals[2]--;
			else if (r2 < p[10])
				vals[2]++;
			else if (r2 <= p[11])
				vals[2]--;

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
