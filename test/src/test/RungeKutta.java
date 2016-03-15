package test;
@FunctionalInterface
public interface RungeKutta
{

	public double[] derivs(double t, double[] vars);

	// Fourth order Runge-Kutta for n (nequ) ordinary differential equations
	// (ODE)
	public default double[] fourthOrder(double x0, double[] y0, double xn,
	      double h)
	{
		int nequ = y0.length;
		double[] k1 = new double[nequ];
		double[] k2 = new double[nequ];
		double[] k3 = new double[nequ];
		double[] k4 = new double[nequ];
		double[] y = new double[nequ];
		double[] yd = new double[nequ];
		double[] dydx = new double[nequ];
		double x = 0.0D;

		// Calculate nsteps
		double ns = (xn - x0) / h;
		ns = Math.rint(ns);
		int nsteps = (int) ns;
		h = (xn - x0) / ns;

		// initialise
		for (int i = 0; i < nequ; i++)
			y[i] = y0[i];

		// iteration over allowed steps
		for (int j = 0; j < nsteps; j++)
		{
			x = x0 + j * h;
			dydx = derivs(x, y);
			for (int i = 0; i < nequ; i++)
				k1[i] = h * dydx[i];

			for (int i = 0; i < nequ; i++)
				yd[i] = y[i] + k1[i] / 2;
			dydx = derivs(x + h / 2, yd);
			for (int i = 0; i < nequ; i++)
				k2[i] = h * dydx[i];

			for (int i = 0; i < nequ; i++)
				yd[i] = y[i] + k2[i] / 2;
			dydx = derivs(x + h / 2, yd);
			for (int i = 0; i < nequ; i++)
				k3[i] = h * dydx[i];

			for (int i = 0; i < nequ; i++)
				yd[i] = y[i] + k3[i];
			dydx = derivs(x + h, yd);
			for (int i = 0; i < nequ; i++)
				k4[i] = h * dydx[i];

			for (int i = 0; i < nequ; i++)
				y[i] += k1[i] / 6 + k2[i] / 3 + k3[i] / 3 + k4[i] / 6;

		}
		return y;
	}
}
