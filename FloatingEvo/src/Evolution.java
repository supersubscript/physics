import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Evolution
{
	static final int							GENERATIONS		= 1000000;
	static final int							POP_SIZE			= 40;
	static final Random						rand				= new Random();
	static final double						h					= .5;
	static final double						NUMBER_PARAMS	= 11;
	static final double						MUTATE_PROB		= 1 / NUMBER_PARAMS;
	static ArrayList<O>			pop;
	static SelectionRule<O>	crossoverSelection;
	static FitnessFunction<O>	fitness;
	static Data[]								data;

	public static void main(String[] args) throws IOException
	{
		init();

		for (int t = 0; t < GENERATIONS; t++)
		{
			// List of new networks being created this generation
			ArrayList<O> newNets = new ArrayList<O>();

			// Perform crossovers based on a roulette wheel selection scheme.
			for (int i = 0; i < POP_SIZE / 10; i++)
			{
				ArrayList<O> crossPicks = crossoverSelection.select(pop);
				O offspring = Operator.PIM_crossover(crossPicks.get(0),
				      crossPicks.get(1));
				newNets.add(offspring);
			}

			// Perform mutations
			for (O gn : pop)
			{
				HashMap<String, Double> map = (HashMap<String, Double>) gn
				      .getParameters().entrySet().stream().collect(Collectors
				            .toMap(e -> e.getKey(), e -> new Double(e.getValue())));

				// For every parameter, mutate a random parameter with a probability
				// of 1/#params. Ugly, but works.
				for (int i = 0; i < NUMBER_PARAMS; i++)
				{
					if (rand.nextDouble() < MUTATE_PROB)
					{

						List<String> keysAsArray = new ArrayList<String>(
						      map.keySet());
						String key = keysAsArray
						      .get(rand.nextInt(keysAsArray.size()));

						double currentVal = map.get(key);
						double newVal = 0;

						if (key.contains("t"))
							newVal = Operator.mutate_gaussian(currentVal, 0, 1e4);
						else if (key.contains("dp"))
							newVal = Operator.mutate_gaussian(currentVal, 0, 1);
						else if (key.contains("dm"))
							newVal = Operator.mutate_gaussian(currentVal, 0, 5);
						else if (key.contains("k"))
							newVal = Operator.mutate_gaussian(currentVal, 0, 1000);
						else if (key.contains("nu"))
							if ((int) (.5 + map.get(key)) == 2)
								newVal = 1;
							else
								newVal = 2;
						else if (key.contains("a"))
							if ((int) (.5 + map.get(key)) == 1)
								newVal = 0.;
							else
								newVal = 1.;
						else
							newVal = 1;
						map.put(key, newVal);
					}
				}
				O gnew = new O(map);
				newNets.add(gnew);
			}

			pop.addAll(newNets); // Avoid concurrency error

			// Sort individual solutions by fitness and add the best to a new pool.

			Map<O, Double> fitMap = fitness.applyDirectlySortedMap(pop);

			ArrayList<O> newPop = new ArrayList<O>(POP_SIZE);
			Iterator<Entry<O, Double>> iterator = fitMap.entrySet()
			      .iterator();
			Map.Entry<O, Double> entry = iterator.next();
			newPop.add(entry.getKey());

			// Print best solution to file every 100th generation.
			if (t % 100 == 0)
			{
				String temp = args.length > 0 ? args[0] : "";
				try (PrintWriter out = new PrintWriter(new BufferedWriter(
				      new FileWriter("/home/william/b16_henrikahl/fitting_data/test"
				            + temp + ".txt", true))))
				{
					out.println("generation: \t" + t);
					out.println(
					      "fitness: \t" + fitness.applyDirectly(entry.getKey()));
					TreeMap<String, Double> d = new TreeMap<String, Double>();
					d.putAll(entry.getKey().getParameters());

					Iterator<Entry<String, Double>> dIter = d.entrySet().iterator();

					while (dIter.hasNext())
					{
						Map.Entry<String, Double> e = dIter.next();
						out.println(e.getKey() + "\t" + e.getValue());
					}
					out.println();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			// Update the rest of the population
			int index = 1;
			while (index < POP_SIZE)
			{
				entry = iterator.next();
				newPop.add(entry.getKey());
				index++;
			}
			pop = newPop;
		}
	}

	public static void init() throws IOException
	{
		Data[] data = Data.values();
		double[][] d = new double[10][25];
		// d[compounds][conc]
		for (int i = data.length - 3; i < data.length; i++)
			d[i] = data[i].getData();

		fitness = (o) ->
		{
			double error = 0.;
			double[][] anVals = new double[25][7];
			for (int i = 1; i < 25; i++)
			{
				anVals[i] = o.getRK().fourthOrder(i - 1, anVals[i - 1], i, h);
				if (Double.isNaN(anVals[i][6]))
					return Double.MAX_VALUE;
				error += (d[7][i] - anVals[i][6]) * (d[7][i] - anVals[i][6]);

			}
			o.setOxA(10);
			for (int i = 1; i < 25; i++)
			{
				anVals[i] = o.getRK().fourthOrder(i - 1, anVals[i - 1], i, h);
				if (Double.isNaN(anVals[i][6]))
					return Double.MAX_VALUE;
				error += .5 * (d[8][i] - anVals[i][6]) * (d[8][i] - anVals[i][6]);
			}
			o.resetOx();
			o.setOxC(10);
			for (int i = 1; i < 25; i++)
			{
				anVals[i] = o.getRK().fourthOrder(i - 1, anVals[i - 1], i, h);
				if (Double.isNaN(anVals[i][6]))
					return Double.MAX_VALUE;
				error += .5 * (d[9][i] - anVals[i][6]) * (d[9][i] - anVals[i][6]);
			}
//			for (int i = 1; i < 25; i++)
//			{
//				System.out.print(d[9][i] + "\t");
//			}
//			System.out.println();
//			for (int i = 1; i < 25; i++)
//			{
//				System.out.print(anVals[i][6] + "\t");
//			}
//			System.out.println();
//			System.out.println();

			o.resetOx();

			return error;

			// WT
		   // for (int i = 1; i < 25; i++)
		   // {
		   // anVals[i] = o.getRK().fourthOrder(i - 1, anVals[i - 1], i, h);
		   // if (Double.isNaN(anVals[i][0]))
		   // return Double.MAX_VALUE;
		   // error += (d[0][i] - anVals[i][0]) * (d[0][i] - anVals[i][0]);
		   // error += (d[1][i] - anVals[i][2]) * (d[1][i] - anVals[i][2]);
		   // error += (d[2][i] - anVals[i][4]) * (d[2][i] - anVals[i][4]);
		   // }
		   //
		   // // OXA
		   // o.setOxA(10);
		   // for (int i = 1; i < 25; i++)
		   // {
		   // anVals[i] = o.getRK().fourthOrder(i - 1, anVals[i - 1], i, h);
		   // if (Double.isNaN(anVals[i][2]))
		   // return Double.MAX_VALUE;
		   // error += .5 * (d[3][i] - anVals[i][2]) * (d[3][i] - anVals[i][2]);
		   // }
		   // o.resetOx();
		   //
		   // // OXB
		   // o.setOxB(10);
		   // for (int i = 1; i < 25; i++)
		   // {
		   // anVals[i] = o.getRK().fourthOrder(i - 1, anVals[i - 1], i, h);
		   // if (Double.isNaN(anVals[i][4]))
		   // return Double.MAX_VALUE;
		   // error += .5 * (d[4][i] - anVals[i][4]) * (d[4][i] - anVals[i][4]);
		   // }
		   // o.resetOx();
		   //
		   // // OXC
		   // o.setOxC(10);
		   // for (int i = 1; i < 25; i++)
		   // {
		   // anVals[i] = o.getRK().fourthOrder(i - 1, anVals[i - 1], i, h);
		   // if (Double.isNaN(anVals[i][0]))
		   // return Double.MAX_VALUE;
		   // error += .5 * (d[5][i] - anVals[i][0]) * (d[5][i] - anVals[i][0]);
		   // }
		   //
		   // // KNOCKC
		   // o.setOxB(0);
		   // o.setOxC(0);
		   // for (int i = 1; i < 25; i++)
		   // {
		   // anVals[i] = o.getRK().fourthOrder(i - 1, anVals[i - 1], i, h);
		   //
		   // if (Double.isNaN(anVals[i][0]))
		   // return Double.MAX_VALUE;
		   // error += 10 * (d[6][i] - anVals[i][0]) * (d[6][i] - anVals[i][0]);
		   // }
		   // o.resetOx();

			// return error;
		};

		crossoverSelection = SelectionRule.rouletteWheel(fitness);
		HashMap<String, Double> p = new HashMap<String, Double>();

		p.put("tD", 1000 + rand.nextDouble() * 9000);
		p.put("dmD", .5 + rand.nextDouble() * .5);

		p.put("aDA", Math.rint(rand.nextDouble()));
		p.put("aDB", Math.rint(rand.nextDouble()));
		p.put("aDC", Math.rint(rand.nextDouble()));

		p.put("kDA", 500 + rand.nextDouble() * 500);
		p.put("kDB", 500 + rand.nextDouble() * 500);
		p.put("kDC", 500 + rand.nextDouble() * 500);

		p.put("nuDA", 1 + Math.rint(rand.nextDouble()));
		p.put("nuDB", 1 + Math.rint(rand.nextDouble()));
		p.put("nuDC", 1 + Math.rint(rand.nextDouble()));

		// * (p.get("aDA") * Math.pow(vals[1] / p.get("kDA"), p.get("nuDA"))
		// + p.get("aDB")
		// * Math.pow((vals[3] / p.get("kDB")), p.get("nuDB"))
		// + p.get("aDC")
		// * Math.pow((vals[5] / p.get("kDC")), p.get("nuDC")))
		// / (1 + p.get("aDA")
		// * Math.pow((vals[1] / p.get("kDA")), p.get("nuDA"))
		// + p.get("aDB")
		// * Math.pow((vals[3] / p.get("kDB")),
		// p.get("nuDB"))
		// + p.get("aDC")
		// * Math.pow(
		// (vals[5]
		// / p.get("kDC")),
		// p.get("nuDC")))
		// / (1 + (1 - p.get("aDA"))
		// * Math.pow((vals[1] / p.get("kDA")), p.get("nuDA"))
		// / (1 + (1 - p.get("aDB"))
		// * Math.pow((vals[3] / p.get("kDB")), p.get("nuDB")))
		// / (1 + (1 - p.get("aDC"))
		// * Math.pow((vals[5] / p.get("kDC")), p.get("nuDC"))))
		// - p.get("dmD") * vals[6];

		pop = new ArrayList<O>();
		for (int j = 0; j < POP_SIZE; j++)
			pop.add(new O(p));

	}
}
