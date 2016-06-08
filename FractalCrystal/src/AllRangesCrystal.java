import java.io.IOException;

public class AllRangesCrystal
{
	public static void main(String[] args) throws IOException
	{
		String[] values = new String[] { "0", "10", "100", "1000", "10000",
				"100000" };
		for (String value : values)
		{
			System.out.println("Running input value " + value + "...");
			String[] v = new String[1];
			v[0] = value;
			BiasedRandomWalk w = new BiasedRandomWalk();
			w.main(v);
		}
		System.out.println("All simulations complete!");


	}

}
