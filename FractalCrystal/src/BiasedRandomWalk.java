import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class BiasedRandomWalk
{
	static double			VOLTAGE_CONSTANT;
	static final int		NUMBER_OF_PARTICLES	= 100000;
	static int				gridWidth				= 100;
	static int				gridHeight				= 100;
	static boolean[][]	grid						= new boolean[gridWidth][gridHeight];
	static int				sourceX					= gridWidth / 2;
	static int				sourceY					= gridHeight / 2;

	public static void main(String[] args) throws IOException
	{
		VOLTAGE_CONSTANT = Double.parseDouble(args[0]);
		// Initialize system
		Random rand = new Random();
		grid[sourceX][sourceY] = true;
		PrintWriter crystal = new PrintWriter(
				new BufferedWriter(new FileWriter(System.getProperty("user.home")
						+ "/crystalData/crystal" + (int) VOLTAGE_CONSTANT + ".dat",
				false)));
		PrintWriter boxDim = new PrintWriter(new BufferedWriter(new FileWriter(
				System.getProperty("user.home") + "/crystalData/boxDim.dat",
				true)));

		// Every particle
		particleLoop: for (int i = 0; i < NUMBER_OF_PARTICLES; i++)
		{
			int side = rand.nextInt(4); // rdlu
			int x = 0;
			int y = 0;
			int timeSteps = 0;

			// Where do we start?
			switch (side)
			{
			case 0:
				x = gridWidth - 1;
				y = (int) (rand.nextDouble() * gridHeight);
				break;
			case 1:
				x = (int) (rand.nextDouble() * gridWidth);
				y = 0;
				break;
			case 2:
				x = 0;
				y = (int) (rand.nextDouble() * gridHeight);
				break;
			case 3:
				x = (int) (rand.nextDouble() * gridWidth);
				y = gridHeight - 1;
				break;
			}

			// Simulate walk
			while (true)
			{
				timeSteps++;
				int rightX = x + 1 == gridWidth ? x : x + 1;
				int leftX = x - 1 == -1 ? x : x - 1;
				int upY = y + 1 == gridHeight ? y : y + 1;
				int downY = y - 1 == -1 ? y : y - 1;

				// Check if crystal adjacent
				if (grid[rightX][y] || grid[leftX][y] || grid[x][upY]
						|| grid[x][downY])
				{
					grid[x][y] = true;
					if (timeSteps < 20)
					{
						growGrid();
					}
					continue particleLoop;
				} else
				{
					// What is the attracting point?
					double distSquared = (sourceX - x) * (sourceX - x)
							+ (sourceY - y) * (sourceY - y);

					double[] noNormProb = new double[8]; // rdlu
					double[] normProbCum = new double[8];

					noNormProb[0] = .25;
					noNormProb[1] = .25;
					noNormProb[2] = .25;
					noNormProb[3] = .25;
					// Go right, down, left, up?
					double theta = Math.atan2(y - sourceY, x - sourceX);
					double yMod = Math.abs(Math.sin(theta));
					double xMod = Math.abs(Math.cos(theta));
					noNormProb[4] = sourceX - x <= 0 ? 0
							: VOLTAGE_CONSTANT / distSquared * xMod;
					noNormProb[5] = y - sourceY > 0
							? VOLTAGE_CONSTANT / distSquared * yMod : 0;
					noNormProb[6] = sourceX - x >= 0 ? 0
							: VOLTAGE_CONSTANT / distSquared * xMod;
					noNormProb[7] = y - sourceY < 0
							? VOLTAGE_CONSTANT / distSquared * yMod : 0;

					double normalizationConstant = 0;
					for (int j = 0; j < noNormProb.length; j++)
					{
						normalizationConstant += noNormProb[j];
					}

					normProbCum[0] = noNormProb[0] / normalizationConstant;
					for (int j = 1; j < normProbCum.length; j++)
					{
						normProbCum[j] = normProbCum[j - 1]
								+ noNormProb[j] / normalizationConstant;
					}
					// System.out.println(normProbCum[0]);
					// for (int j = 1; j < normProbCum.length; j++)
					// {
					// System.out.println(normProbCum[j] - normProbCum[j - 1]);
					// }
					// System.out.println();

					double randomDirectionProb = rand.nextDouble();
					if (randomDirectionProb < normProbCum[0])
					{
						x = rightX;
					} else if (randomDirectionProb < normProbCum[1])
					{
						y = downY;
					} else if (randomDirectionProb < normProbCum[2])
					{
						x = leftX;
					} else if (randomDirectionProb < normProbCum[3])
					{
						y = upY;
					} else if (randomDirectionProb < normProbCum[4])
					{
						x = rightX;
					} else if (randomDirectionProb < normProbCum[5])
					{
						y = downY;
					} else if (randomDirectionProb < normProbCum[6])
					{
						x = leftX;
					} else
					{
						y = upY;
					}
				}
			}
		}
		// System.out.println(grid.length + "\t" + grid[0].length + "\t" +
		// gridWidth + "\t" + gridHeight);
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[0].length; j++)
			{
				crystal.print((grid[i][j] == false ? "0" : "1") + "\t");
			}
			crystal.println();
		}
		boxDim.println(VOLTAGE_CONSTANT + "\t" + boxDim(grid, 100, 1));
		boxDim.close();
		crystal.close();
	}

	public static double boxDim(boolean[][] grid, double delta, double delta2)
	{
		double[] N_delta = new double[2];

		// delta_1
		for (int i = 0; i < Math.rint(gridWidth / delta); i++)
		{
			boxLoop: for (int j = 0; j < Math.rint(gridWidth / delta); j++)
			{
				// Sub-matrix
				for (int i2 = (int) (i * delta); i2 < (i + 1) * delta
						&& i2 < gridWidth; i2++)
				{
					for (int j2 = (int) (j * delta); j2 < (j + 1) * delta
							&& j2 < gridHeight; j2++)
					{
						if (grid[i2][j2])
						{
							N_delta[0]++;
							continue boxLoop;
						}
					}
				}
			}
		}
		// delta_2
		for (int i = 0; i < Math.rint(gridWidth / delta2); i++)
		{
			boxLoop: for (int j = 0; j < Math.rint(gridWidth / delta2); j++)
			{
				for (int i2 = (int) (i * delta2); i2 < (i + 1) * delta2
						&& i2 < gridWidth; i2++)
				{
					for (int j2 = (int) (j * delta2); j2 < (j + 1) * delta2
							&& j2 < gridHeight; j2++)
					{
						if (grid[i2][j2])
						{
							N_delta[1]++;
							continue boxLoop;
						}
					}
				}
			}
		}
		return Math.log(N_delta[1] / N_delta[0]) / Math.log(delta / delta2);
	}

	public static void growGrid()
	{
		final int increment = 100;
		gridWidth += increment;
		gridHeight += increment;
		// System.out.println(gridWidth + "\t" + gridHeight);
		sourceX = gridWidth / 2;
		sourceY = gridHeight / 2;
		boolean[][] newGrid = new boolean[gridWidth][gridHeight];
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[0].length; j++)
			{
				newGrid[i + increment / 2][j + increment / 2] = grid[i][j];
			}
		}
		grid = newGrid;
		// System.out.println(grid.length + "\t" + grid[0].length);
	}
}
