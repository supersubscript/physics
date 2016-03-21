
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a fitness function.
 */
@FunctionalInterface
public interface FitnessFunction<O extends Organism> {
	/**
	 * Apply the fitness computation directly without rescaling. The sum of
	 * these values is not necessarily 1.
	 */
	public double applyDirectly(O o);

	/**
	 * Compute the fitness of an organism, rescaled based on the other values in
	 * the population.
	 */
	public default double apply(Population<O> pop, O o) {
		double sum = 0.;
		for (O p : pop) {
			sum += Math.max(0., this.applyDirectly(p));
		}
		return sum == 0 ? 1. / pop.size() : Math.max(0., this.applyDirectly(o) / sum);
	}

	/**
	 * Compute the fitness of all Organisms in the World.
	 */
	public default Map<O, Double> apply(Population<O> pop) {
		double sum = 0.;
		for (O p : pop) {
			sum += Math.max(0., this.applyDirectly(p));
		}
		Map<O, Double> result = new HashMap<>();
		for (O o : pop) {
			result.put(o, sum == 0 ? 1. / pop.size() : Math.max(0., this.applyDirectly(o) / sum));
		}
		return result;
	}

}