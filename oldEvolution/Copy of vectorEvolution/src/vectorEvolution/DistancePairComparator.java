package vectorEvolution;
import java.util.Comparator;

public class DistancePairComparator implements Comparator<DistancePair>
{

	// sorts pairs after lowest distance
	@Override
	public int compare(DistancePair fromShort, DistancePair fromLong)
	{
		return Double.compare(fromShort.distance, fromLong.distance);
	}
}