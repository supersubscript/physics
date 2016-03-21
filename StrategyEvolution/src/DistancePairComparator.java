import java.util.Comparator;
public class DistancePairComparator implements Comparator<DistancePair>
{

	// sorts pairs after lowest distance
	@Override
	public int compare(DistancePair fromShort, DistancePair fromlong)
	{
		return (int) (fromShort.distance - fromlong.distance);
	}
}