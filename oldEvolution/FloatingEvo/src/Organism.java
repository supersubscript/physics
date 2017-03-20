
public class Organism
{
	protected double[] parameters;

	public Organism(double... parameters)
	{
		this.parameters = parameters;
	}

	public void setParameter(double val, int i)
	{
		parameters[i] = val;
	}
	public void setParameters(double[] parameters)
	{
		this.parameters = parameters;
	}

	public double getParameter(int i)
	{
		return parameters[i];
	}

	public double[] getParameters()
	{
		return parameters;
	}
	
	public interface Factory<O extends Organism>
	{

		public O random();

		/**
		 * Create an exact copy of an Organism. This method may alter the state of
		 * the given Organism. Only invoke when all external factors are ready
		 * (e.g. there is room for offspring). Returns null when no child can be
		 * created.
		 */
		public O copy(O o);

		/**
		 * Create offspring for this Organism. This method may alter the state of
		 * the given Organism. Only invoke when all external factors are ready
		 * (e.g. there is room for offspring). Returns null when no child can be
		 * created.
		 */
		public O split(O o);

		/**
		 * Create offspring from two Organisms. This method may alter the state of
		 * the given Organism. Only invoke when all external factors are ready
		 * (e.g. there is room for offspring). Returns null when no child can be
		 * created.
		 */
		public O sex(O mommy, O daddy);

	}
}
