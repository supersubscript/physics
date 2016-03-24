import java.util.Comparator;

public class BitstringComparator implements Comparator<Bitstring>
{

	private Encoding e; 
	
	public BitstringComparator(Encoding e)
	{
		this.e = e;
	}
	
	@Override
	public int compare(Bitstring a, Bitstring b)
	{
		return (int) e.signDistance(a, b);
	}

}
