import java.util.Arrays;
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

	public static void main(String[] args)
	{

		BitstringComparator b = new BitstringComparator(Encoding.BINARY);
		Bitstring[] bbb = new Bitstring[] { new Bitstring("1110"),
				new Bitstring("1100"), new Bitstring("0110") };
		Arrays.sort(bbb, b);
		for (int i = 0; i < bbb.length; i++)
		{
			System.out.println(Encoding.binary(bbb[i]));
		}
		// System.out.println());
	}

}
