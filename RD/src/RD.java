import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class RD
{
	static final double	TTOT		= 100000;
	static final double	DELTA_T	= 1;
	static final double	DELTA_X	= 1;
	static final double	D_A		= 0.001;
	static final double	D_H		= .2;
	static final double	RHO_A		= .01;
	static final double	RHO_H		= .02;
	static final double	MU_A		= .01;
	static final double	MU_H		= .02;
	static final double	SIGMA_A	= 0;
	static final double	SIGMA_H	= .0;
	static final double	K			= .25;

	static double			t;
	static double[][]		avals;
	static double[][]		hvals;
	static int				HEIGHT	= 30;
	static int				WIDTH		= 30;
	static Random			RAND		= new Random();

	// For deep copying of matrices
	static double[][] deepCopy(double[][] matrix)
	{
		return java.util.Arrays.stream(matrix).map(el -> el.clone())
		      .toArray($ -> matrix.clone());
	}

	public static void main(String[] args)
	      throws FileNotFoundException, UnsupportedEncodingException
	{
		// Initialization
		t = 0;
		avals = new double[HEIGHT][WIDTH];
		hvals = new double[HEIGHT][WIDTH];
		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++)
			{
				hvals[i][j] = RAND.nextDouble();
				avals[i][j] = RAND.nextDouble();
			}

		print();

		while (t < TTOT)
		{
			t += DELTA_T;
			// Ugly solution to expansion
			if ((int) t % 1000 == 0)
			{
				double[][][] inc = expandDiag(avals, hvals);
				avals = inc[0];
				hvals = inc[1];
			}

			double[][] tempa = new double[HEIGHT][WIDTH]; // deepCopy(avals);
			double[][] temph = new double[HEIGHT][WIDTH]; // deepCopy(hvals);

			for (int i = 0; i < HEIGHT; i++)
				for (int j = 0; j < WIDTH; j++)
				{
					// Make grid periodic/toroidal
					int nexti = 0, previ = 0, nextj = 0, prevj = 0;
					if (i == 0)
						previ = HEIGHT - 1;
					else if (i == HEIGHT - 1)
						nexti = 0;
					else
					{
						nexti = i + 1;
						previ = i - 1;
					}
					if (j == 0)
						prevj = WIDTH - 1;
					else if (j == WIDTH - 1)
						nextj = 0;
					else
					{
						nextj = j + 1;
						prevj = j - 1;
					}
					// Calculate increment
					tempa[i][j] = avals[i][j] + DELTA_T * (D_A / (DELTA_X * DELTA_X)
					      * (avals[i][nextj] + avals[i][prevj] + avals[nexti][j]
					            + avals[previ][j] - 4 * avals[i][j])
					      + RHO_A
					            * (avals[i][j] * avals[i][j]
					                  / (hvals[i][j] * (1
					                        + K * avals[i][j] * avals[i][j])))
					      - MU_A * avals[i][j] + SIGMA_A);

					temph[i][j] = hvals[i][j] + DELTA_T * (D_H / (DELTA_X * DELTA_X)
					      * (hvals[i][nextj] + hvals[i][prevj] + hvals[nexti][j]
					            + hvals[previ][j] - 4 * hvals[i][j])
					      + RHO_H * avals[i][j] * avals[i][j] - MU_H * hvals[i][j]
					      + SIGMA_H);
				}
			avals = tempa;
			hvals = temph;

			// for (int i = 0; i < HEIGHT; i++)
			// {
			// for (int j = 0; j < WIDTH; j++)
			// System.out.println(t + "\t" + i + "\t" + j + "\t" + avals[i][j]
			// + "\t" + hvals[i][j]);
			// System.out.println();
			// }
			// System.out.println();

			// Print output.
			if ((int) t % 5000 == 0)
				print();
		}
	}

	// Increment size of map diagonally (= add cross in toroidal case)
	public static double[][][] expandDiag(double[][] matrixOne,
	      double[][] matrixTwo)
	{

		double[][] newMatrixOne = new double[matrixOne.length
		      + 1][matrixOne[0].length + 1];
		double[][] newMatrixTwo = new double[matrixTwo.length
		      + 1][matrixTwo[0].length + 1];

		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++)
			{
				newMatrixOne[i][j] = matrixOne[i][j];
				newMatrixTwo[i][j] = matrixTwo[i][j];
			}
		
		for (int i = 0; i < newMatrixTwo.length; i++)
		{
			newMatrixOne[newMatrixOne.length - 1][i] = Double.MIN_VALUE;
			newMatrixTwo[newMatrixTwo.length - 1][i] = Double.MIN_VALUE;
			newMatrixOne[i][newMatrixOne.length - 1] = Double.MIN_VALUE;
			newMatrixTwo[i][newMatrixTwo.length - 1] = Double.MIN_VALUE;
		}
		HEIGHT += 1;
		WIDTH += 1;
		double[][][] newMatrices = new double[2][HEIGHT][WIDTH];
		newMatrices[0] = newMatrixOne;
		newMatrices[1] = newMatrixTwo;

		return newMatrices;
	}

	public static void print()
	      throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(
		      "/home/william/b16_henrikahl/reactdiff/t" + (int) t + ".dat",
		      "UTF-8");
		for (int i = 0; i < HEIGHT; i++)
		{
			for (int j = 0; j < WIDTH; j++)
				writer.println(t + "\t" + i + "\t" + j + "\t" + avals[i][j] + "\t"
				      + hvals[i][j]);
			writer.println();
		}
		writer.close();
	}
}