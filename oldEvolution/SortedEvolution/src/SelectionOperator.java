import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Random;

public interface SelectionOperator
{
	static Random rand = new Random();

	/**
	 * Which Organisms to select, out of all the Organisms in the World.
	 */
	public <T extends Collection<Bitstring>> T select(Population<Bitstring> pop);

	// Returns @param:selectionSize organisms based on a roulette wheel scheme.
	public static SelectionOperator rouletteWheel(
			FitnessFunction<Bitstring> fitness, int selectionSize)
	{
		return new SelectionOperator()
		{

			@Override
			public <T extends Collection<Bitstring>> T select(
					Population<Bitstring> pop)
			{
				double[] cumulativeFitnesses = new double[pop.size()];
				Bitstring[] organisms = (Bitstring[]) pop.toArray();
				cumulativeFitnesses[0] = organisms[0].getValue();

				for (int i = 1; i < organisms.length; i++)
				{
					double f = organisms[i].getValue();
					cumulativeFitnesses[i] = cumulativeFitnesses[i - 1] + f;
				}

				Population<Bitstring> selection = new Population<Bitstring>(
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
					selection.add(organisms[index]);
				}
				if (selection instanceof Collection<?>)
					return (T) selection;
				else throw new ClassCastException();
			}
		};
	}

	// Return the best @param N individuals in a population.
	public static SelectionOperator elitism(FitnessFunction<Bitstring> fitness,
			int N)
	{
		assert (N > 0);

		return new SelectionOperator()
		{
			@SuppressWarnings("unchecked")
			@Override
			public <T extends Collection<Bitstring>> T select(
					Population<Bitstring> pop)
			{
				assert (N <= pop.size());

				Population<Bitstring> selection = new Population<Bitstring>(
						MultiEvolution.bc);

				Map<Bitstring, Double> fitnessMap = fitness.apply(pop);
				fitnessMap = General.sortByValue(fitnessMap);

				Iterator<Entry<Bitstring, Double>> it = fitnessMap.entrySet()
						.iterator();
				for (int i = 0; i < N; i++)
				{
					Map.Entry<Bitstring, Double> entry = it.next();
					selection.add(entry.getKey());
				}

				if (selection instanceof Collection<?>)
					return (T) selection;
				else throw new ClassCastException();
			}
		};
	}

	/*
	 * Return N best elements, plus an extra number specified via another
	 * selection algorithm (@param sel).
	 */
	public static SelectionOperator semiElitism(
			FitnessFunction<Bitstring> fitness, SelectionOperator sel, int N)
	{

		return new SelectionOperator()
		{
			@SuppressWarnings("unchecked")
			@Override
			public <T extends Collection<Bitstring>> T select(
					Population<Bitstring> pop)
			{
				Population<Bitstring> selection = new Population<Bitstring>(
						MultiEvolution.bc);

				Map<Bitstring, Double> fitnessMap = fitness.apply(pop);
				fitnessMap = General.sortByValue(fitnessMap);

				// Add N best to selection
				Iterator<Entry<Bitstring, Double>> it = fitnessMap.entrySet()
						.iterator();
				for (int i = 0; i < N; i++)
				{
					selection.add(it.next().getKey());
					it.remove();
				}
				Population<Bitstring> restPop = new Population<Bitstring>(
						MultiEvolution.bc);
				for (int j = 0; j < fitnessMap.size() - N; j++)
					restPop.add(it.next().getKey());

				selection.addAll(sel.select(restPop));

				if (selection instanceof Collection<?>)
					return (T) selection;
				else throw new ClassCastException();
			}
		};
	}

	/* Selects @param N random individuals from population */
	public static SelectionOperator random(int N)
	{

		return new SelectionOperator()
		{
			@Override
			public <T extends Collection<Bitstring>> T select(
					Population<Bitstring> pop)
			{
				assert N > 0 && N <= pop.size();
				Population<Bitstring> selection = new Population<Bitstring>(
						MultiEvolution.bc);
				ArrayList<Bitstring> copy = new ArrayList<Bitstring>();
				for (Bitstring b : pop)
					copy.add(b.clone());

				for (int i = 0; i < N; i++)
				{
					int pos = (int) (rand.nextDouble() * copy.size());
					Bitstring b = copy.get(pos);
					selection.add(b);
					copy.remove(b);
				}

				if (selection instanceof Collection<?>)
					return (T) selection;
				else throw new ClassCastException();
			}
		};
	}

	/*
	 * Returns N individuals via tournament selections, using a given tournament
	 * size and probability.
	 */
	public static SelectionOperator tournament(
			FitnessFunction<Bitstring> fitness, int N, int tournamentSize,
			double prob)
	{

		return new SelectionOperator()
		{
			@SuppressWarnings("unchecked")
			@Override
			public <T extends Collection<Bitstring>> T select(
					Population<Bitstring> pop)
			{
				Population<Bitstring> selection = new Population<Bitstring>(
						MultiEvolution.bc);
				Population<Bitstring> copy = new Population<Bitstring>(
						MultiEvolution.bc);
				for (Bitstring b : pop)
					copy.add(b.clone());

				for (int i = 0; i < N; i++)
				{
					Population<Bitstring> subPop = SelectionOperator
							.random(tournamentSize).select(copy);

					ArrayList<Bitstring> sortedPop = new ArrayList<Bitstring>(
							subPop);
					sortedPop.sort(MultiEvolution.bc);

					int pick = (General.geometric(prob)) % tournamentSize;
					// System.out.println(pick);
					selection.add(sortedPop.get(pick).clone());
				}

				if (selection instanceof Collection<?>)
					return (T) selection;
				else throw new ClassCastException();
			}
		};
	}

}
