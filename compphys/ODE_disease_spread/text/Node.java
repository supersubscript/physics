/* Class representing a node in a network. */
public class Node
{
	public String name;
	private double S, I, R;
	
	public Node(String name, double S, double I, double R)
	{
		this.name = name;
		this.S = S;
		this.I = I;
		this.R = R;
	}
	
	public double getS()
	{
		return S;
	}
	
	public double getI()
	{
		return I;
	}
	
	public double getR()
	{
		return R;
	}
	
	public double getN()
	{
		return S + I + R;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setS(double S)
	{
		this.S = S;
	}
	
	public void setI(double I)
	{
		this.I = I;
	}
	
	public void setR(double R)
	{
		this.R = R;
	}
	
}