
public class DistancePair
{
	public final Bitstring	first;
	public final Bitstring	second;
	public final double		distance;

	public DistancePair(Bitstring first, Bitstring second, double distance)
	{
		this.first = first;
		this.second = second;
		this.distance = distance;
	}

	@Override
	public String toString()
	{
		return first + "\t" + second + "\t" + distance;
	}
}
