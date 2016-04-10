import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

public class NewtonRhapson
{
	static Random		rand			= new Random();
	static Exercise	exc			= Exercise.exc_2_5(3.845);
	static Writer		writer;

	public static void main(String[] args) throws IOException
	{
		writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(System.getProperty("user.home") + "/nr.dat"),
				"utf-8"));

		// Init conditions
		double[] x = new double[1];

		for (int i = 0; i < x.length; i++)
		{
			// double nr = 0;

			// while (nr * nr < .00001)
			// {
			// nr = -2 + rand.nextDouble() * 4;
			// }
			x[i] = .5;
		}
		for (int i = 0; i < x.length; i++)
			System.out.print(x[i] + "\t");
		System.out.println();

		for (int s = 0; s < 1000; s++)
		{
			for (int i = 0; i < x.length; i++)
			{
//				x[i] = newton(x[i], exc.getFct(), exc.getDerivative());
				x[i] = exc.getFct().applyAsDouble(x[i]);
				String outp = x[i] + "\t" + exc.getFct().applyAsDouble(x[i]) + "\t";
				System.out.print(outp);
				writer.write(outp);
			}
			System.out.println();
			writer.write("\n");
		}
		writer.close();

	}

	public static double newton(double x0, DoubleUnaryOperator fct,
			DoubleUnaryOperator derivative)
	{
		return x0 - fct.applyAsDouble(x0) / derivative.applyAsDouble(x0);
	}

}
