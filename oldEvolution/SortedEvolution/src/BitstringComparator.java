import java.util.Comparator;

public class BitstringComparator implements Comparator<Bitstring>
{
	@Override
	public int compare(Bitstring a, Bitstring b)
	{
		return (int) MultiEvolution.encoding.distance(a, b);
	}
}
