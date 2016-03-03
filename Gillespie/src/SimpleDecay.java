public class SimpleDecay
{
	// Necessary parameters
	static double[]		vals;
	static double[]		decayConsts;

	// RK specifics
	static final double	h		= .05;
	static final double	ttot	= 100;
	static final double	STEPS	= ttot / h;

	public static void main(String[] args)
	{
		// Initialize stuff
		vals = new double[1];
		decayConsts = new double[vals.length];
		for (int i = 0; i < vals.length; i++)
		{
			vals[i] = 1000;
			decayConsts[i] = .5;
		}

		System.out.print(0 + "\t");
		for (int i = 0; i < vals.length; i++)
			System.out.print(vals[i] + "\t");
		System.out.println();

		// Simulate development process.
		for (int i = 0; i < STEPS; i++)
		{
			double t = i * h;

			// Calculate RK step.
			RungeKutta.apply(t, vals);

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
	 * Computes derivatives according to the specified ODE. Note that time is
	 * completely aesthetical in this case.
	 */
	public static interface RungeKutta
	{
		static double[]	k1	= new double[vals.length];
		static double[]	k2	= new double[vals.length];
		static double[]	k3	= new double[vals.length];
		static double[]	k4	= new double[vals.length];

		public static void apply(double t, double[] vals)
		{
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
			for (int j = 0; j < vals.length; j++)
				vals[j] += h * (k1[j] / 6 + k2[j] / 3 + k3[j] / 3 + k4[j] / 6);
		}

		// Derivative calculator. Change according to desired function.
		public static double[] derivs(double t, double[] vals)
		{
			double[] dvals = new double[vals.length];
			for (int i = 0; i < dvals.length; i++)
				dvals[i] = -decayConsts[i] * vals[i];
			return dvals;
		}
	}

}