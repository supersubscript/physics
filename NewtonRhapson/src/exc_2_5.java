import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

public class exc_2_5
{
	static Random		rand			= new Random();
	static Exercise	exc			= Exercise.exc_2_5(3.2);
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
			x[i] = exc.getFct().applyAsDouble(.1);
		}
		for (int i = 0; i < x.length; i++){
			double val = rand.nextDouble();
			System.out.print(val + "\t" + exc.getFct().applyAsDouble(val) + "\n");
			writer.write(val + "\t" + exc.getFct().applyAsDouble(val) + "\n");
			}
		writer.write("\n");
		System.out.println();

//		for (int s = 0; s < 200; s++)
//		{
//			for (int i = 0; i < x.length; i++)
//			{
////				x[i] = newton(x[i], exc.getFct(), exc.getDerivative());
//				x[i] = exc.getFct().applyAsDouble(x[i]);
//				String outp = x[i] + "\t" + exc.getFct().applyAsDouble(x[i]) + "\t";
//				System.out.print(outp);
//				writer.write(outp);
//			}
//			System.out.println();
//			writer.write("\n");
//		}
		writer.close();

	}

	public static double newton(double x0, DoubleUnaryOperator fct,
			DoubleUnaryOperator derivative)
	{
		return x0 - fct.applyAsDouble(x0) / derivative.applyAsDouble(x0);
	}

}
