public class Pair<K, V>
{
	private final K	k;
	private final V	v;

	public static <K, V> Pair<K, V> createPair(K element0, V element1)
	{
		return new Pair<K, V>(element0, element1);
	}

	public Pair(K element0, V element1)
	{
		this.k = element0;
		this.v = element1;
	}

	public K getElement0()
	{
		return k;
	}

	public V getElement1()
	{
		return v;
	}

}