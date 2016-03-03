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

public class _2_NumberEvolution
{

	public static <O extends Organism<O>> void printInfo(World<O> w)
	{
		System.out.println(w.getTime() + ", " + w.getPopulation().size() + ", "
		      + w.getRandomOrganism());
	}

	public static void main(String[] args)
	{

		double subRate = 0.001;
		double indelRate = 0.001;
		int initialSize = 100;
		int worldSize = 200;
		int initialPop = 100;
		int duration = 10000;
		Sequence consensus = new Sequence(
		      new boolean[] { true, true, true, true, true });
		int valueDiam = 4;
		double sensuality = 0.01;
		// How to read the bitstring sequence into a real number
		RealReader internalReader = RealReader.applyFunction(RealReader.gray(),
		      (x) -> Math.exp(5. / Prime.get(x.intValue())));
		RealReader reader = RealReader.consensus(consensus, valueDiam,
		      internalReader);
		// RealReader reader = RealReader.multiplicative(Distribution.uniform(-2.,
		// 2.));
		// How to calculate crossovers
		CrossOverOperator crossover = CrossOverOperator.singleSynapse(0);
		// The fitness landscape
		FitnessFunction<organisms.Number> fitness = (n) -> {
			double val = Math.max(n.getValue(), 0.);
			return val > 2. ? 0. : val * Math.sin(10. * Math.PI * val) + 2.;
		};

		// Settings
		Settings<organisms.Number> settings = new Settings<>();
		MutationOperator mut = MutationOperator.combine(
		      MutationOperator.poisson_sub(subRate),
		      MutationOperator.poisson_indel(indelRate));
		Distribution<Integer> initialSizeDistribution = Distribution
		      .constant(initialSize);

		settings.setOrganismFactory(new organisms.Number.Factory(mut,
		      initialSizeDistribution, reader, crossover));
		settings.setSplittingRule(
		      SelectionRule.selectProportion(fitness, 1 - sensuality));
		settings.setDeathRule(SelectionRule.trivialTrue());
		settings.setSexyRule(PairSelectionRule
		      .any(SelectionRule.selectProportion(fitness, 2 * sensuality)));
		settings.setSpawnRule(LocationFilter.empty(), 1);
		settings.setCoordinateSystem(new TrivialCoordinateSystem(worldSize));

		// World
		World<organisms.Number> w = new World<organisms.Number>(settings);
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
