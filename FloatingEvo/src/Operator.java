import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

public class Operator
{
	private static Random rand = new Random();

	public static double mutate_gaussian(double val, double lowRange,
	      double highRange)
	{
		double length = Math.abs(highRange - lowRange);
		return Math.min(
		      Math.max(rand.nextGaussian() * length / 10 + val, lowRange),
		      highRange);
	}

	public static Pair<GeneNetwork, GeneNetwork> onePointCrossover(GeneNetwork a,
	      GeneNetwork b)
	{
		HashMap<String, Double> offParam1 = new HashMap<String, Double>();
		HashMap<String, Double> offParam2 = new HashMap<String, Double>();

		Iterator<Entry<String, Double>> ait = a.getParameters().entrySet()
		      .iterator();
		Iterator<Entry<String, Double>> bit = b.getParameters().entrySet()
		      .iterator();
		double breakPoint = rand.nextDouble() * a.getParameters().size();
		int l = 0;
		while (ait.hasNext() && bit.hasNext() && l < breakPoint)
		{
			Map.Entry<String, Double> e1 = ait.next();
			Map.Entry<String, Double> e2 = bit.next();
			offParam1.put(e1.getKey(), e1.getValue());
			offParam2.put(e2.getKey(), e2.getValue());
		}
		while (ait.hasNext() && bit.hasNext())
		{
			Map.Entry<String, Double> e1 = ait.next();
			Map.Entry<String, Double> e2 = bit.next();
			offParam1.put(e2.getKey(), e2.getValue());
			offParam2.put(e1.getKey(), e1.getValue());
		}

		GeneNetwork off1 = new GeneNetwork(offParam1);
		GeneNetwork off2 = new GeneNetwork(offParam2);

		return new Pair<GeneNetwork, GeneNetwork>(off1, off2);
	}

	// Perform point-in-middle crossover
	public static GeneNetwork PIM_crossover(GeneNetwork a, GeneNetwork b)
	{

		HashMap<String, Double> offParam = new HashMap<String, Double>();

		Iterator<Entry<String, Double>> ait = a.getParameters().entrySet()
		      .iterator();
		Iterator<Entry<String, Double>> bit = b.getParameters().entrySet()
		      .iterator();

		while (ait.hasNext() && bit.hasNext())
		{
			double r = rand.nextDouble();
			Map.Entry<String, Double> e1 = ait.next();
			Map.Entry<String, Double> e2 = bit.next();
			offParam.put(e1.getKey(), (1 - r) * e1.getValue() + r * e2.getValue());
		}

		return new GeneNetwork(offParam);
	}

}
