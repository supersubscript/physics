import java.util.ArrayList;
import java.util.stream.Collectors;

public class Population<O extends Organism> extends ArrayList<O>
{
	private static final long serialVersionUID = 1326061483981456487L;

	public Population()
	{
		super();
	}

	public Population(Population<O> p)
	{
		this.addAll(p);
	}

	public Population(int i)
	{
		super(i);
	}

	@Override
	public String toString()
	{
		String str = this.stream().map(i -> i.toString())
				.collect(Collectors.joining("\n"));
		return str;
	}

	@Override
	public Population<O> clone()
	{
		Population<O> copy = new Population<O>();
		copy.addAll(this);
		return copy;
	}
}
