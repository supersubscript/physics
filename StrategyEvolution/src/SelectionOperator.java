import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public interface SelectionOperator
{
	static Random rand = new Random();

	/**
	 * Which Organisms to select, out of all the Organisms in the World.
	 */
	public Collection<Bitstring> select(Population<Bitstring> pop);

	// Returns @param:selectionSize organisms based on a roulette wheel scheme.
	public static SelectionOperator rouletteWheel(
			FitnessFunction<Bitstring> fitness, int selectionSize)
	{
		return new SelectionOperator()
		{

			@Override
			public Collection<Bitstring> select(Population<Bitstring> pop)
			{
				double[] cumulativeFitnesses = new double[pop.size()];
				cumulativeFitnesses[0] = fitness.applyDirectly(pop.get(0));
				for (int i = 1; i < pop.size(); i++)
				{
					double f = fitness.applyDirectly(pop.get(i));
					cumulativeFitnesses[i] = cumulativeFitnesses[i - 1] + f;
				}

				ArrayList<Bitstring> selection = new ArrayList<Bitstring>(
						selectionSize);
				for (int i = 0; i < selectionSize; i++)
				{
					double randomFitness = rand.nextDouble()
							* cumulativeFitnesses[cumulativeFitnesses.length - 1];
					int index = Arrays.binarySearch(cumulativeFitnesses,
							randomFitness);
					if (index < 0)
					{
						// Convert negative insertion point to array index.
						index = Math.abs(index + 1);
					}
					selection.add(pop.get(index));
				}
				return selection;
			}
		};
	}

	// Return the best @param n individuals in a population.
	public static SelectionOperator bestFraction(
			FitnessFunction<Bitstring> fitness, int n)
	{
		assert (n > 0);

		return new SelectionOperator()
		{
			@Override
			public Collection<Bitstring> select(Population<Bitstring> pop)
			{
				assert (n <= pop.size());

				ArrayList<Bitstring> selection = new ArrayList<Bitstring>();

				Map<Bitstring, Double> fitnessMap = fitness.apply(pop);
				fitnessMap = General.sortByValue(fitnessMap);

				Iterator<Entry<Bitstring, Double>> it = fitnessMap.entrySet()
						.iterator();
				for (int i = 0; i < n; i++)
				{
					Map.Entry<Bitstring, Double> entry = it.next();
					selection.add(entry.getKey());
				}

				return selection;
			}
		};
	}

}
