import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/* Main method simulating the spreading of a disease over a network 
 * consisting of a set of nodes. */
public class DiseaseSpread
{
	private final static double h = 0.01; // size of the timestep

	public static void main(String[] args) throws FileNotFoundException
	{
		Network europe = new Network();
		ArrayList<Node> nodes = new ArrayList<Node>();
		Disease disease = new Disease(1, 2.5, .002);
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		double[][] commute = new double[28][28]; // travel weights
		int iterations = 52;

		// Read from file and compute
		String line = null;
		try
		{
			line = br.readLine();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		Set<String> seen = new HashSet<String>();
		while (line != null)
		{
			String[] fields = line.split("\t");
			if (!seen.contains(fields[0]))
			{
				seen.add(fields[0]);
				nodes.add(new Node(fields[0], Double.parseDouble(fields[3]), 0, 0));
			}
			for (int i = 0; i < 28; i++)
			{
				if (nodes.size() - 1 == i)
					continue;
				
				String[] innerFields = line.split("\t");
				commute[nodes.size() - 1][i] = Double.parseDouble(innerFields[2])
						/ Double.parseDouble(innerFields[3]) / 52.;
				try
				{
					line = br.readLine();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		try
		{
			br.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		europe.setCommute(commute);
		nodes.get(0).setI(1); // Set initial infected
		europe.setNodes(nodes);
		Node[] nodearr = europe.getNodes(); // Used a lot
		PrintWriter[] w = new PrintWriter[europe.getNodes().length];

		System.out.println("#Week \tSusceptible \tInfected \tRecovered");
		for (int i = 0; i < nodearr.length; i++)
		{
			w[i] = new PrintWriter(
			      new FileOutputStream(nodearr[i].getName() + ".dat"), true);
			w[i].println("#t \tSusceptible \tInfected \tRecovered");
			w[i].println("0 \t" + (int) (nodearr[i].getS() + .5) + "\t"
			      + (int) (nodearr[i].getI() + .5) + "\t"
			      + (int) (nodearr[i].getR() + .5));

			System.out.println("0 \t" + (int) (nodearr[i].getS() + .5) + "\t"
			      + (int) (nodearr[i].getI() + .5) + "\t"
			      + (int) (nodearr[i].getR() + .5));
		}

		for (int i = 0; i < iterations / h; i++)
		{
			Network newNetwork;
			newNetwork = ODEsolver.timeStep(europe, disease, h);
			europe.setNodes(newNetwork.getNodes());

			// Saves values to files and prints to console
			for (int j = 0; j < newNetwork.getNodes().length; j++)
			{
			w[j].println(((i + 1) * h) + "\t" + (int) (nodearr[j].getS() + .5)
			      + "\t" + (int) (nodearr[j].getI() + .5) + "\t"
			      + (int) (nodearr[j].getR() + .5));
			System.out
			      .println((i + 1) * h + "\t" + (int) (nodearr[j].getS() + .5)
			            + "\t" + (int) (nodearr[j].getI() + .5) + "\t"
			            + (int) (nodearr[j].getR() + .5));
			}
		}
		for (int i = 0; i < w.length; i++)
			w[i].close();
	}
}
