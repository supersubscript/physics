import java.util.Random;

public class BiasedRandomWalk
{
	static double			voltageConstant		= 1000;
	static int				gridWidth				= 100;
	static int				gridHeight				= 100;
	static boolean[][]	grid						= new boolean[gridWidth][gridHeight];
	static final int		NUMBER_OF_PARTICLES	= 1;

	public static void main(String[] args)
	{
		// Initialize system
		Random rand = new Random();
		grid[gridWidth / 2][gridHeight / 2] = true;

		// Every particle
		particleLoop: for (int i = 0; i < NUMBER_OF_PARTICLES; i++)
		{
			int side = rand.nextInt(4); // rdlu
			int x = 0;
			int y = 0;

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

			while (true)
			{
				int rightX = x + 1 == gridWidth ? x : x + 1;
				int leftX = x - 1 == -1 ? x : x - 1;
				int upY = y + 1 == gridHeight ? y : y + 1;
				int downY = y - 1 == -1 ? y : y - 1;

				// Check if crystal adjacent
				if (grid[rightX][y] || grid[leftX][y] || grid[x][upY]
						|| grid[x][downY])
				{
					grid[x][y] = true;
					continue particleLoop;
				} else
				{
					// Take random step
					int sourceX = gridWidth / 2;
					int sourceY = gridHeight / 2;

					double distSquared = (gridWidth / 2 - x) * (gridWidth / 2 - x)
							+ (gridHeight / 2 - y) * (gridHeight / 2 - y);

					double[] noNormProb = new double[8]; // rdlu
					double[] normProbCum = new double[8];

					noNormProb[0] = .25;
					noNormProb[1] = .25;
					noNormProb[2] = .25;
					noNormProb[3] = .25;
					// Go right, down, left, up?
					noNormProb[4] = sourceX - x < 0 ? 0
							: voltageConstant / distSquared;
					noNormProb[5] = sourceY - y < 0 ? voltageConstant / distSquared
							: 0;
					noNormProb[6] = sourceX - x > 0 ? 0
							: voltageConstant / distSquared;
					noNormProb[7] = sourceY - y > 0 ? voltageConstant / distSquared
							: 0;

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
					System.out.println(normProbCum[0]);
					for (int j = 1; j < normProbCum.length; j++)
					{
						System.out.println(normProbCum[j] - normProbCum[j - 1]);
					}
					System.out.println();

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
		// for (int i = 0; i < gridWidth; i++)
		// {
		// for (int j = 0; j < gridHeight; j++)
		// {
		// System.out.print((grid[i][j] == false ? "0" : "1") + "\t");
		// }
		// System.out.println();
		// }
	}
}
