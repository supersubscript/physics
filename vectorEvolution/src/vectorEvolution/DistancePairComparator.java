package vectorEvolution;

import java.util.Comparator;

public class DistancePairComparator implements Comparator<DistancePair>
{
	@Override
	public int compare(DistancePair fromShort, DistancePair fromLong)
	{
		return Double.compare(fromShort.distance, fromLong.distance);
	}
}