
public class Organism
{
	private double[] parameters;

	public Organism(double... parameters)
	{
		this.parameters = parameters;
	}

	public void setParameter(double val, int i)
	{
		parameters[i] = val;
	}

	public double getParameter(int i)
	{
		return parameters[i];
	}

}
