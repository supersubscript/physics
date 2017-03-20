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

public class _5_HenrikEvolution
{

	public static <O extends Organism<O>> void printInfo(World<O> w)
	{
		System.out.println(w.getTime() + "\t" + w.getPopulation().size() + "\t"
		      + w.getRandomOrganism());
	}

	public static void main(String[] args)
	{
		int nrGenes = 10;
		int initialPop = 10;
		int duration = 1000;
		int worldSize = 200;
		double mutProb = .1;
		double[] targets = new double[10];
		for (int i = 0; i < targets.length; i++)
			targets[i] = (int) Math.random() * 1024;
		Sequence[] inds = new Sequence[nrGenes];
		for (int i = 0; i < inds.length; i++)
			inds[i] = new Sequence(10);
		Distribution<Integer> initialSizeDistribution = Distribution
		      .constant(initialPop);

		// How to read the bitstring sequence into a real number
		RealReader internalReader = RealReader.applyFunction(RealReader.gray(),
		      (x) -> Math.exp(5. / Prime.get(x.intValue())));

		// RealReader reader = RealReader.consensus(consensus, valueDiam,
		// internalReader);

		// How to calculate crossovers
		CrossOverOperator crossover = CrossOverOperator.none();

		// The fitness landscape. Find closest target.
		FitnessFunction<organisms.Number> fitness = n ->
		{
			double val = Math.max(n.getValue(), 0.);
			double min = Double.MAX_VALUE;
			for (int i = 0; i < targets.length; i++)
				if (min < n.getValue() - targets[i])
					min = n.getValue();
			return min;
		};

		// Settings
		Settings<organisms.Number> settings = new Settings<>();
		MutationOperator mut = MutationOperator.poisson_sub(mutProb);

		// MutationOperator mut = MutationOperator.combine(
		// MutationOperator.poisson_sub(subRate),
		// MutationOperator.poisson_indel(indelRate));
		// Distribution<Integer> initialSizeDistribution = Distribution
		// .constant(initialSize);

		settings.setOrganismFactory(new organisms.Number.Factory(mut,
		      initialSizeDistribution, internalReader, crossover));
		// settings.setSplittingRule(
		// SelectionRule.selectProportion(fitness, 1 - sensuality));
		settings.setDeathRule(SelectionRule.SelectProportion(fitness, .5));
		// settings.setSexyRule(PairSelectionRule
		// .any(SelectionRule.selectProportion(fitness, 2 * sensuality)));
		// settings.setSpawnRule(LocationFilter.empty(), 1);
		settings.setCoordinateSystem(new TrivialCoordinateSystem(worldSize));

		// World
		World<organisms.Number> w = new World<organisms.Number>(settings);
		for (int i = 0; i < initialPop; i++)
			w.spawnRandom();

		// Time
		for (int i = 0; i < duration; i++)
		{
			printInfo(w);
			w.tick();
		}
		printInfo(w);
	}

}
