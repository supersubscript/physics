package Jama;

public class Meinhardt
{
	static final double a = 0.1;
	static final double rho_a = 1; // don't touch
	static final double rho_h = 0.1;
	static final double D_a = 0.1; // 
	static final double D_h = 1; // don't touch
	static final double L = 100; // don't touch
	static final double J = 100; // don't touch
	
	
	public static void main(String[] args)
	{
		Matrix a = new Matrix(new double[3][9]);
		System.out.println(a.rank());
		
	}

	// public static double[] derivs(double t, double[] cellVals)
	// {
	// double[] dvals = new double[cellVals.length];
	// for (int i = 0; i < dvals.length; i++)
	// dvals[i] = -[i] * vals[i];
	// return dvals;
	// }

}
