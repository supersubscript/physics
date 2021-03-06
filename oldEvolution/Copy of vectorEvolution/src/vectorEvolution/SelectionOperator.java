package vectorEvolution;
import java.util.ArrayList;
import java.util.HashMap;
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
	public HashMap<Bitstring, Double> select(HashMap<Bitstring, Double> pop);

	// Returns @param:selectionSize organisms based on a roulette wheel scheme.
	// public static SelectionOperator rouletteWheel(
	// FitnessFunction<Bitstring> fitness, int selectionSize)
	// {
	// return new SelectionOperator()
	// {
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// public <T extends Collection<Bitstring>> T select(
	// HashMap<Bitstring, Double> pop)
	// {
	// double[] cumulativeFitnesses = new double[pop.size()];
	// cumulativeFitnesses[0] = fitness.applyDirectly(pop.get(0));
	// for (int i = 1; i < pop.size(); i++)
	// {
	// double f = fitness.applyDirectly(pop.get(i));
	// cumulativeFitnesses[i] = cumulativeFitnesses[i - 1] + f;
	// }
	//
	// Population<Bitstring> selection = new Population<Bitstring>(
	// selectionSize);
	// for (int i = 0; i < selectionSize; i++)
	// {
	// double randomFitness = rand.nextDouble()
	// * cumulativeFitnesses[cumulativeFitnesses.length - 1];
	// int index = Arrays.binarySearch(cumulativeFitnesses,
	// randomFitness);
	// if (index < 0)
	// {
	// // Convert negative insertion point to array index.
	// index = Math.abs(index + 1);
	// }
	// selection.add(pop.get(index));
	// }
	// if (selection instanceof Collection<?>)
	// return (T) selection;
	// else throw new ClassCastException();
	// }
	// };
	// }

	// Return the best @param N individuals in a population.
	public static SelectionOperator elitism(FitnessFunction<Bitstring> fitness,
			int N)
	{
		assert (N > 0);

		return new SelectionOperator()
		{
			@Override
			public HashMap<Bitstring, Double> select(
					HashMap<Bitstring, Double> pop)
			{
				assert (N <= pop.size());

				HashMap<Bitstring, Double> selection = new HashMap<Bitstring, Double>();

				pop = (HashMap<Bitstring, Double>) General.sortByValue(pop);

				Iterator<Entry<Bitstring, Double>> it = pop.entrySet().iterator();
				for (int i = 0; i < N; i++)
				{
					Map.Entry<Bitstring, Double> entry = it.next();
					selection.put(entry.getKey(), entry.getValue());
				}

				return selection;
			}
		};
	}

	/*
	 * Return N best elements, plus an extra number specified via another
	 * selection algorithm (@param sel).
	 */
	// public static SelectionOperator semiElitism(
	// FitnessFunction<Bitstring> fitness, SelectionOperator sel, int N)
	// {
	//
	// return new SelectionOperator()
	// {
	// @SuppressWarnings("unchecked")
	// @Override
	// public <T extends Collection<Bitstring>> T select(
	// HashMap<Bitstring, Double> pop)
	// {
	// Population<Bitstring> selection = new Population<Bitstring>();
	//
	// Map<Bitstring, Double> fitnessMap = fitness.apply(pop);
	// fitnessMap = General.sortByValue(fitnessMap);
	//
	// // Add N best to selection
	// Iterator<Entry<Bitstring, Double>> it = fitnessMap.entrySet()
	// .iterator();
	// for (int i = 0; i < N; i++)
	// {
	// selection.add(it.next().getKey());
	// it.remove();
	// }
	// Population<Bitstring> restPop = new Population<Bitstring>();
	// for (int j = 0; j < fitnessMap.size() - N; j++)
	// restPop.add(it.next().getKey());
	//
	// selection.addAll(sel.select(restPop));
	//
	// if (selection instanceof Collection<?>)
	// return (T) selection;
	// else throw new ClassCastException();
	// }
	// };
	// }

	/* Selects @param N random individuals from population */
	public static SelectionOperator random(int N)
	{

		return new SelectionOperator()
		{
			@Override
			public HashMap<Bitstring, Double> select(
					HashMap<Bitstring, Double> pop)
			{
				assert N > 0 && N <= pop.size();
				HashMap<Bitstring, Double> selection = new HashMap<Bitstring, Double>();

				ArrayList<Bitstring> list = new ArrayList<Bitstring>(pop.keySet());

				for (int i = 0; i < N; i++)
				{
					int pos = (int) (rand.nextDouble() * list.size());
					Bitstring b = list.get(pos);
					selection.put(b, pop.get(b));
					list.remove(b);
				}

				return selection;
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
			@Override
			public HashMap<Bitstring, Double> select(
					HashMap<Bitstring, Double> pop)
			{
				HashMap<Bitstring, Double> selection = new HashMap<Bitstring, Double>();
				HashMap<Bitstring, Double> popCopy = new HashMap<Bitstring, Double>(
						pop);

				for (int i = 0; i < N; i++)
				{
					HashMap<Bitstring, Double> subPop = SelectionOperator
							.random(tournamentSize).select(popCopy);
					subPop = (HashMap<Bitstring, Double>) General
							.sortByValue(subPop);
					int pick = (General.geometric(prob) - 1) % tournamentSize;
					// System.out.println(pick);

					Iterator<Map.Entry<Bitstring, Double>> it = subPop.entrySet()
							.iterator();
					for (int j = 0; j < pick - 1; j++)
						it.next();

					Entry<Bitstring, Double> entry = it.next();
					selection.put(entry.getKey().clone(), entry.getValue());
					it.remove();
				}

				return selection;
			}
		};
	}
}
