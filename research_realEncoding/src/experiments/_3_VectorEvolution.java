package experiments;

import general.Distribution;
import general.Prime;
import general.Sequence;
import organisms.Organism;
import physics.TrivialCoordinateSystem;
import physics.World;
import settings.CrossOverOperator;
import settings.FitnessFunction;
import settings.LocationFilter;
import settings.MutationOperator;
import settings.PairSelectionRule;
import settings.RealReader;
import settings.SelectionRule;
import settings.Settings;

public class _3_VectorEvolution
{

	public static <O extends Organism<O>> void printInfo(World<O> w)
	{
		System.out.println(w.getTime() + ", " + w.getPopulation().size() + ", "
		      + w.getRandomOrganism());
	}

	public static void main(String[] args)
	{

		double subRate = 0.0001;
		double indelRate = 0.00001;
		int initialSize = 100;
		int worldSize = 200;
		int initialPop = 100;
		int duration = 1000000;
		Sequence consensus = new Sequence(
		      new boolean[] { true, true, false, true, true });
		Sequence separator = new Sequence(
		      new boolean[] { false, true, true, true, true, true, false });
		int valueDiam = 3;
		double sensuality = 0.1;
		// How to read the bitstring sequence into a real number
		RealReader internalReader = RealReader.applyFunction(RealReader.gray(),
		      (x) -> Math.exp(20. / Prime.get(x.intValue())));
		RealReader reader = RealReader.consensus(consensus, valueDiam,
		      internalReader);
		// RealReader reader = RealReader.multiplicative(Distribution.uniform(-2.,
		// 2.));
		// How to calculate crossovers
		CrossOverOperator crossover = CrossOverOperator.singleSynapse(0);
		// The fitness landscape
		FitnessFunction<organisms.Vector> fitness = (n) ->
		{
			double result = 0;
			int i = 0;
			for (double v : n.getValues())
			{
				if (i > 1)
				{
					result -= 1;
				} else
				{
					result -= 1;
					double val = Math.max(v, 0.);
					val = val > 2. ? 0. : val * Math.sin(10. * Math.PI * val) + 0.5;
					result += val;
					i++;
				}
			}
			return result;
		};

		// Settings
		Settings<organisms.Vector> settings = new Settings<>();
		MutationOperator mut = MutationOperator.combine(
		      MutationOperator.poisson_sub(subRate),
		      MutationOperator.poisson_indel(indelRate, indelRate, indelRate,
		            indelRate, indelRate, indelRate, indelRate, indelRate,
		            indelRate, indelRate));
		Distribution<Integer> initialSizeDistribution = Distribution
		      .constant(initialSize);

		settings.setOrganismFactory(new organisms.Vector.Factory(mut,
		      initialSizeDistribution, reader, crossover, separator));
		settings.setSplittingRule(
		      SelectionRule.selectProportion(fitness, 1 - sensuality));
		settings.setSexyRule(PairSelectionRule
		      .any(SelectionRule.selectProportion(fitness, 2 * sensuality)));
		settings.setDeathRule(SelectionRule.trivialTrue());
		settings.setSpawnRule(LocationFilter.empty(), 1);
		settings.setCoordinateSystem(new TrivialCoordinateSystem(worldSize));

		// World
		World<organisms.Vector> w = new World<>(settings);
		for (int i = 0; i < initialPop; i++)
			w.spawnRandom();

		// Time
		for (int i = 0; i < duration; i++)
		{
			printInfo(w);
			// System.out.println(_1_ConwaysGameOfLife.printGrid(w, gridSize));
			w.tick();
			// List<Double> directFit = new ArrayList<>();
			// for (organisms.Number o : w.population())
			// directFit.add(fitness.applyDirectly(o));
			// System.out.println(directFit);
			// System.out.println(fitness.apply(w).values());
		}
		printInfo(w);
	}

}
