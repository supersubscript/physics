import java.util.Comparator;

public class BitstringComparator implements Comparator<Bitstring>
{

	@Override
	public int compare(Bitstring a, Bitstring b)
	{
		// Can return big number, so normalize
		return (int) Math
		      .signum((BitInterpreter.binary(a) - BitInterpreter.binary(b)));
	}

}