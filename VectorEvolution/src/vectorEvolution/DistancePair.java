package vectorEvolution;

public class DistancePair
{
	public Bitstring	first;
	public Bitstring	second;
	public Integer 	third;
	public double		distance;

	public DistancePair(Bitstring first, Bitstring second, double distance)
	{
		this.first = first;
		this.second = second;
		this.distance = distance;
	}
	public DistancePair(Bitstring first, Integer third, double distance)
	{
		this.first = first;
		this.third = third;
		this.distance = distance;
	}


	@Override
	public String toString()
	{
		return first + "\t" + second + "\t" + distance;
	}

	//
	// public double getDistance()
	// {
	// return distance;
	// }
	//
	// public Bitstring getFirst()
	// {
	// return first;
	// }
	//
	// public Bitstring getSecond()
	// {
	// return second;
	// }

}
