public class deterministic
{
	// Setup
	static double[]		vals;
	static double[]		k1, k2, k3, k4;

	// Parameter values
	static final double	h		= .05;

	static final double	S1		= 0.4;		// fox log
	static final double	S2		= 0.7;		// fox feedmax
	static final double	S3		= .04;		// rabbit log
	static final double	S4		= .035;		// rabbit feedmax
	static final double	S5		= .3;			// grass log

	static final double	a1		= 5;			// fox grow
	static final double	a2		= .9;			// fox death
	static final double	a3		= 4;			// foxes eat rabbits
	static final double	a4		= 10;			// rabbit growth
	static final double	a5		= 20.3;		// rabbit decay
	static final double	a6		= 1.9;		// grass eaten by rabbits
	static final double	a7		= 2.2;		// grass growth

	static final double	ttot	= 50;
	static final double	STEPS	= ttot / h;

	public static void main(String[] args)
	{
		// Initialize stuff

		vals = new double[3];
		k1 = new double[vals.length];
		k2 = new double[vals.length];
		k3 = new double[vals.length];
		k4 = new double[vals.length];

		for (int i = 0; i < vals.length; i++)
			if (i % 3 == 0)
				vals[i] = Math.random();
			else
				vals[i] = .5;

		System.out.print(0 + "\t");
		for (int i = 0; i < vals.length; i++)
			System.out.print(vals[i] + "\t");
		System.out.println();

		// Simulate development process.
		for (int i = 0; i < STEPS; i++)
		{
			double t = i * h;

			// Calculate RK coefficients.
			double[] dvals = derivs(t, vals);
			for (int j = 0; j < k1.length; j++)
				k1[j] = dvals[j];
			double[] tempvals = new double[vals.length];
			for (int j = 0; j < tempvals.length; j++)
				tempvals[j] = vals[j] + h * k1[j] / 2;
			dvals = derivs(t + h / 2, tempvals);
			for (int j = 0; j < tempvals.length; j++)
				k2[j] = dvals[j];
			for (int j = 0; j < tempvals.length; j++)
				tempvals[j] = vals[j] + h * k2[j] / 2;
			dvals = derivs(t + h / 2, tempvals);
			for (int j = 0; j < tempvals.length; j++)
				k3[j] = dvals[j];
			for (int j = 0; j < tempvals.length; j++)
				tempvals[j] = vals[j] + h * k3[j];
			dvals = derivs(t + h, tempvals);
			for (int j = 0; j < tempvals.length; j++)
				k4[j] = dvals[j];

			// Move all values one step in time according to 4th order RK.
			for (int j = 0; j < vals.length; j++)
				vals[j] += h * (k1[j] / 6 + k2[j] / 3 + k3[j] / 3 + k4[j] / 6);

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

	/*
	 * Computes derivatives according to the repressilator ODE's. Note that time
	 * is completely aesthetical in this case.
	 */
	public static double[] derivs(double t, double[] vals)
	{
		double[] dvals = new double[vals.length];
		for (int i = 0; i < dvals.length; i++)
		{
			if (i % 3 == 0 || i == 0)
			{
				dvals[i] = a1 * vals[i] * vals[i + 1] * (1 - S1 * vals[i])
				      / (1 + S2 * vals[i + 1]) - a2 * vals[i];
				dvals[i + 1] = -a3 * vals[i] * vals[i + 1] * (1 - S1 * vals[i])
				      / (1 + S2 * vals[i + 1])
				      + a4 * vals[i + 2] * vals[i + 1] * (1 - S3 * vals[i + 1])
				            / (1 + S4 * vals[i + 2])
				      - a5 * vals[i + 1];
				dvals[i + 2] = -a6 * vals[i + 2] * vals[i + 1]
				      * (1 - S3 * vals[i + 1]) / (1 + S4 * vals[i + 2])
				      + a7 * vals[i + 2] * (1 - S5 * vals[i + 2]);
			}
		}
		return dvals;
	}

}
