import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Population<O extends Organism> extends PriorityQueue<O>
{
	private static final long serialVersionUID = 1326061483981456487L;

	@SuppressWarnings("unchecked")
	public Population(BitstringComparator bc)
	{
		super((Comparator<? super O>) bc);
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
}
