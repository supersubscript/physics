package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class rk {
	static double[] vals = new double[7];
	static HashMap<String, Double> p = new HashMap<String, Double>();
	static Random rand = new Random();
	static final double h = 0.01;
	private static double uA = 1.;
	private static double uB = 1.;
	private static double uC = 1.;
	private static double oxA = 1.;
	private static double oxB = 1.;
	private static double oxC = 10.;

	private static double dmA = 1.9144871228136946;
	private static double dmB = 1.5388405099238187;
	private static double dmC = 1.9406501229112112;
	private static double dpA = 0.45280907736490306;
	private static double dpB = 0.1;
	private static double dpBC = 0.9159810009943241;
	private static double dpC = 0.49231280884027545;
	private static double kAC = 401.922010905093;
	private static double kBA = 1000.0;
	private static double kBC = 413.2271201732548;
	private static double kCB = 397.11109754840766;
	private static double nuAC = 2.0;
	private static double nuBA = 2.0;
	private static double nuCA = 1.0;
	private static double nuCB = 1.0;
	private static double kBAC = 600.041241111110;
	private static double tA = 2687.6035038364453;
	private static double tB = 1537.7475401116358;
	private static double tC = 1941.7197236716297;

	public static void main(String[] args) throws IOException {

		// Production
		BufferedReader reader = new BufferedReader(
				new FileReader("/home/william/b16_henrikahl/fitting_data/params.dat"));
		String line = null;
		while ((line = (reader.readLine())) != null) {
			String[] splits = line.split("\t");
			// System.out.println(line);
			String key = splits[0];
			double value = Double.parseDouble(splits[1]);
			p.put(key, value);
		}
		reader.close();

		RungeKutta rk;
		rk = (tval, vals) -> {
			double[] derivs = new double[vals.length];
			derivs[0] = oxA * tA / (1 + Math.pow(vals[5] / kAC, nuAC)) - dmA * vals[0];

			derivs[1] = uA * vals[0] - dpA * vals[1];

			derivs[2] = oxB * tB * Math.pow(vals[1], nuBA) / (Math.pow(kBA, nuBA) + Math.pow(vals[1], nuBA))
					- dmB * vals[2];

			derivs[3] = uB * vals[2] - (dpB + dpBC * vals[5] / (kBC + vals[5])) * vals[3];

			derivs[4] = oxC * tC * Math.pow((vals[3] / kCB), nuCB)
					/ (1 + Math.pow(vals[3] / kCB, nuCB) + Math.pow(vals[1] / kBAC, nuCA)) - dmC * vals[4];

			derivs[5] = uC * vals[4] - dpC * vals[5];

			// Let all combinations be possible
			derivs[6] = p.get("tD") * ((p.get("aDA") == 1. ? Math.pow(vals[1] / p.get("kDA"), p.get("nuDA")) : 1.)
					/ (1 + Math.pow(vals[1] / p.get("kDA"), p.get("nuDA"))) +

			(p.get("aDB") == 1. ? Math.pow(vals[3] / p.get("kDB"), p.get("nuDB")) : 1.)
					/ (1 + Math.pow(vals[3] / p.get("kDB"), p.get("nuDB"))) +

			(p.get("aDC") == 1. ? Math.pow(vals[5] / p.get("kDC"), p.get("nuDC")) : 1.)
					/ (1 + Math.pow(vals[5] / p.get("kDC"), p.get("nuDC"))))

					- p.get("dmD") * vals[6];

			return derivs;
		};

		for (int j = 0; j < vals.length; j++)
			if (j % 2 == 0)
				System.out.print(vals[j] + "\t");
		System.out.println();
		vals = new double[vals.length];
		for (int i = 0; i < 25; i++) {
			vals = rk.fourthOrder(i, vals, i + 1, h);
			for (int j = 0; j < vals.length; j++) {
				if (j % 2 == 0)
					System.out.print(vals[j] + "\t");
			}
			System.out.println();
		}
	}
}
