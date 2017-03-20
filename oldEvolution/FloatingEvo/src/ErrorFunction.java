
@FunctionalInterface
public interface ErrorFunction
{

	public double apply();

	public default double MSE(double[] vals, double[] targets)
	{
		double error = 0.;
		int minLen = (int) Math.min(vals.length, targets.length);
		for (int i = 0; i < minLen; i++)
			error += (vals[i] - targets[i]) * (vals[i] - targets[i]);
		return error;
	}

}
