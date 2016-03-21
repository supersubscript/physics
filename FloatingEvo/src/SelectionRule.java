import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

@FunctionalInterface
public interface SelectionRule<GeneNetwork>
{

	/**
	 * Which Organisms to select, out of all the Organisms in the World.
	 */
	public ArrayList<GeneNetwork> select(ArrayList<GeneNetwork> pop);

	/**
	 * Whether one specific organism is selected.
	 */
	// Default implementation computes killWhich for the whole population and
	// extracts one.
	// Override to something more efficient when possible.
	public default boolean selectQ(ArrayList<GeneNetwork> pop, GeneNetwork o)
	{
		return select(pop).contains(o);
	}

		// Returns two organisms based on a roulette wheel scheme.
		public static <GeneNetwork> SelectionRule<GeneNetwork> rouletteWheel(
		      FitnessFunction<GeneNetwork> fitness)
		{
			return new SelectionRule<GeneNetwork>()
			{
				Random rand = new Random();
	
				@Override
				public ArrayList<GeneNetwork> select(ArrayList<GeneNetwork> pop)
				{
					// System.out.println(pop.size());
	
					Map<GeneNetwork, Double> fs = fitness.apply(pop);
	
					double[] cumulativeFitnesses = new double[pop.size()];
					cumulativeFitnesses[0] = fitness.applyDirectly(pop.get(0));
					for (int i = 1; i < pop.size(); i++)
					{
						GeneNetwork gn = pop.get(i);
						double f = fitness.applyDirectly(gn);
						cumulativeFitnesses[i] = cumulativeFitnesses[i - 1] + f;
					}
	
					int selectionSize = 2;
					ArrayList<GeneNetwork> selection = new ArrayList<GeneNetwork>(
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
}