import java.util.HashMap;
import java.util.Random;

public class O
{
	private static Random				rand	= new Random();
	private double[]						vals	= new double[7];
	private HashMap<String, Double>	p;
	private RungeKutta					rk;
	private static double				uA		= 1.;
	private static double				uB		= 1.;
	private static double				uC		= 1.;
	private static double				oxA	= 1.;
	private static double				oxB	= 1.;
	private static double				oxC	= 1.;
	private static double				dmA	= 1.9144871228136946;
	private static double				dmB	= 1.5388405099238187;
	private static double				dmC	= 1.9406501229112112;
	private static double				dpA	= 0.45280907736490306;
	private static double				dpB	= 0.1;
	private static double				dpBC	= 0.9159810009943241;
	private static double				dpC	= 0.49231280884027545;
	private static double				kAC	= 401.922010905093;
	private static double				kBA	= 1000.0;
	private static double				kBC	= 413.2271201732548;
	private static double				kCB	= 397.11109754840766;
	private static double				nuAC	= 2.0;
	private static double				nuBA	= 2.0;
	private static double				nuCA	= 1.0;
	private static double				nuCB	= 1.0;
	private static double				kBAC	= 600.041241111110;
	private static double				tA		= 2687.6035038364453;
	private static double				tB		= 1537.7475401116358;
	private static double				tC		= 1941.7197236716297;

	// Initialize random GeneNetwork
	public O()
	{
		p = new HashMap<String, Double>();
		// Production
		// p.put("tA", 5e3 + rand.nextDouble() * 1000);
		// p.put("tB", 5e3 + rand.nextDouble() * 1000);
		// p.put("tC", 5e3 + rand.nextDouble() * 1000);
		//
		// // Decay mRNA
		// p.put("dmA", 2.5 + rand.nextDouble());
		// p.put("dmB", 2.5 + rand.nextDouble());
		// p.put("dmC", 2.5 + rand.nextDouble());
		//
		// // Decay proteins
		// p.put("dpA", .5 + rand.nextDouble() * .5);
		// p.put("dpB", .5 + rand.nextDouble() * .5);
		// p.put("dpC", .5 + rand.nextDouble() * .5);
		// p.put("dpBC", .5 + rand.nextDouble() * .5);
		//
		// // Saturation
		// p.put("kAC", 500 + rand.nextDouble() * 500);
		// p.put("kBC", 500 + rand.nextDouble() * 500);
		// p.put("kBA", 500 + rand.nextDouble() * 500);
		// p.put("kCB", 500 + rand.nextDouble() * 500);
		// p.put("kBAC", 500 + rand.nextDouble() * 500);
		//
		// // Hill coefficients
		// p.put("nuAC", (int) 1.5 + rand.nextDouble());
		// p.put("nuBA", (int) 1.5 + rand.nextDouble());
		// p.put("nuCB", (int) 1.5 + rand.nextDouble());
		// p.put("nuCA", (int) 1.5 + rand.nextDouble());

		updateRK();
	}

	public O(HashMap<String, Double> parameters)
	{
		this.p = parameters;
		updateRK();
	}

	// Update RungeKutta method according to current parameters
	public void updateRK()
	{
		rk = (tval, vals) ->
		{
			double[] derivs = new double[vals.length];
			derivs[0] = oxA * tA / (1 + Math.pow(vals[5] / kAC, nuAC))
		         - dmA * vals[0];

			derivs[1] = uA * vals[0] - dpA * vals[1];

			derivs[2] = oxB * tB * Math.pow(vals[1], nuBA)
		         / (Math.pow(kBA, nuBA) + Math.pow(vals[1], nuBA))
		         - dmB * vals[2];

			derivs[3] = uB * vals[2]
		         - (dpB + dpBC * vals[5] / (kBC + vals[5])) * vals[3];

			derivs[4] = oxC * tC * Math.pow((vals[3] / kCB), nuCB) / (1
		         + Math.pow(vals[3] / kCB, nuCB) + Math.pow(vals[1] / kBAC, nuCA))
		         - dmC * vals[4];

			derivs[5] = uC * vals[4] - dpC * vals[5];

			// Let all combinations be possible
			derivs[6] = p.get("tD")*(
					(p.get("aDA") == 1. ? Math.pow(vals[1] / p.get("kDA"), p.get("nuDA")) : 1.)
					/(1 + Math.pow(vals[1] / p.get("kDA"), p.get("nuDA"))) + 
							
					(p.get("aDB") == 1. ? Math.pow(vals[3] / p.get("kDB"), p.get("nuDB")) : 1.)
					/(1 + Math.pow(vals[3] / p.get("kDB"), p.get("nuDB"))) +
					
					(p.get("aDC") == 1. ? Math.pow(vals[5] / p.get("kDC"), p.get("nuDC")) : 1.)
					/(1 + Math.pow(vals[5] / p.get("kDC"), p.get("nuDC")))) - 
					
					p.get("dmD") * vals[6];

			return derivs;
		};
	}

	public RungeKutta getRK()
	{
		return rk;
	}

	public HashMap<String, Double> getParameters()
	{
		return p;
	}

	public void setParameters(HashMap<String, Double> parameters)
	{
		this.p = parameters;
	}

	public RungeKutta getRk()
	{
		return rk;
	}

	public void setRk(RungeKutta rk)
	{
		this.rk = rk;
	}

	public double[] getVals()
	{
		return vals;
	}

	public void setVals(double[] vals)
	{
		this.vals = vals;
	}

	public void setRK(RungeKutta rk)
	{
		this.rk = rk;
	}

	public void resetOx()
	{
		oxA = 1.;
		oxB = 1.;
		oxC = 1.;
		updateRK();
	}

	public void setOxA(double oxA)
	{
		O.oxA = oxA;
		updateRK();
	}

	public void setOxB(double oxB)
	{
		O.oxB = oxB;
		updateRK();
	}

	public void setOxC(double oxC)
	{
		O.oxC = oxC;
		updateRK();
	}

}
