import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Class representing a binary sequence, such as a genome sequence.
 */
public class Bitstring
{

	private final boolean[] seq;

	public Bitstring(boolean[] seq)
	{
		assert seq != null;
		this.seq = seq;
	}

	public Bitstring(int len)
	{
		assert len > 0;
		Random rand = new Random();
		boolean[] temp = new boolean[len];
		for (int i = 0; i < temp.length; i++)
			temp[i] = rand.nextBoolean();
		this.seq = temp;

	}

	public int length()
	{
		return this.seq.length;
	}

	public void flip(int i)
	{
		assert i >= 0 && i < seq.length;
		seq[i] = !seq[i];
	}

	public boolean get(int i)
	{
		return this.seq[i];
	}

	public Bitstring get(int start, int end)
	{
		boolean[] result = new boolean[end - start];
		for (int i = 0; i < end - start; i++)
		{
			result[i] = this.get(i + start);
		}
		return new Bitstring(result);
	}

	public boolean[] asArray()
	{
		return this.seq.clone();
	}

	@Override
	public Bitstring clone()
	{
		return new Bitstring(this.asArray());
	}

	/**
	 * Check if this sequence contains a specific subsequence.
	 */
	public boolean contains(Bitstring needle)
	{
		return this.firstOccurrence(needle) != -1;
	}

	/**
	 * Get the first occurrence of a specific subsequence. Returns -1 if not
	 * found.
	 */
	public int firstOccurrence(Bitstring needle)
	{
		return this.firstOccurrence(needle, 0);
	}

	/**
	 * Get the first occurrence of a specific subsequence, starting search at the
	 * specified starting position. Returns -1 if not found.
	 */
	public int firstOccurrence(Bitstring needle, int start)
	{
		if (needle.length() > this.length() - start)
			return -1;
		startingPositions: for (int i = start; i <= this.length()
		      - needle.length(); i++)
		{
			for (int j = 0; j < needle.length(); j++)
			{
				if (this.get(i + j) != needle.get(j))
					continue startingPositions;
			}
			return i;
		}
		return -1;
	}

	/**
	 * Get all shortest substrings delimited by the specified start and end
	 * subsequence. A stop sequence can overlap with the start sequence of
	 * another subsequence. If a start sequence is not followed by a stop
	 * sequence the corresponding entry in the result list will contain all bits
	 * from that start to the end of the sequence. The start and end are not
	 * included in the results.
	 */
	public List<Bitstring> genes(Bitstring start, Bitstring stop)
	{
		List<Bitstring> result = new ArrayList<>();
		int startPos = 0;
		int endPos = 0;
		while (true)
		{
			startPos = this.firstOccurrence(start, endPos);
			if (startPos < 0)
				break;
			endPos = this.firstOccurrence(stop, startPos + start.length());
			if (endPos < 0)
				endPos = this.length();
			result.add(this.get(startPos + start.length(), endPos));
		}
		return result;
	}

	/**
	 * Return the starting indices of all occurrences of the target subsequence.
	 * This can be done in an overlapping or non-overlapping manner.
	 */
	public List<Integer> occurrences(Bitstring needle, boolean overlap)
	{
		List<Integer> result = new ArrayList<>();
		int startLooking = 0;
		int firstOcc = this.firstOccurrence(needle);
		while (firstOcc != -1)
		{
			result.add(firstOcc);
			startLooking = firstOcc + (overlap ? 1 : needle.length());
			firstOcc = this.firstOccurrence(needle, startLooking);
		}
		return result;
	}

	/**
	 * Split this Sequence based on a given separator. If the separator is null,
	 * simply return the original sequence.
	 */
	public List<Bitstring> split(Bitstring sep)
	{
		List<Bitstring> result = new ArrayList<>();
		if (sep == null)
		{
			result.add(this);
		} else
		{
			int prevocc = 0;
			for (int occ : this.occurrences(sep, false))
			{
				if (occ <= prevocc)
					continue;
				result.add(this.get(prevocc, occ));
				prevocc = occ + sep.length();
			}
			if (prevocc < this.length())
				result.add(this.get(prevocc, this.length()));
		}
		return result;
	}

	/**
	 * Find position of largest common subsequence. If multiple such subsequences
	 * exist, only the first is returned. In the resulting Overlap instance, this
	 * sequence will be considered "left", while the other sequence will be
	 * considered "right". Equivalent to LCSS(needle, 0)
	 */
	public Overlap LCSS(Bitstring needle)
	{
		return this.LCSS(needle, 0);
	}

	/**
	 * Find position of largest common subsequence, where at most x mismatches
	 * are allowed (but no gaps). If multiple such subsequences exist, only the
	 * first is returned. In the resulting Overlap instance, this sequence will
	 * be considered "left", while the other sequence will be considered "right".
	 */
	public Overlap LCSS(Bitstring needle, int x)
	{
		// TODO make more efficient; suffix tree algorithm is O(n+m)

		// Make sure needle is smaller than haystack
		if (needle.length() > this.length())
		{
			Overlap res = needle.LCSS(this, x);
			return new Overlap(res.rightStart, res.leftStart, res.length);
		}
		;

		// Initialize result as empty overlap
		Overlap result = Overlap.EMPTY;

		// Slide sequences
		for (int i = 0; i < this.length() - result.length; i++)
		{
			int curlen = 0;
			int mismatch = 0;
			for (int j = 0; j < needle.length() && j < this.length() - i; j++)
			{
				if (this.get(i + j) == needle.get(j))
				{
					curlen++;
				} else
				{
					if (mismatch < x)
					{
						curlen++;
						mismatch++;
					} else
					{
						curlen = 0;
						mismatch = 0;
					}
				}
				if (curlen > result.length)
					result = new Overlap(i, j - curlen + 1, curlen);
			}
		}

		// Return result
		return result;
	}

	/**
	 * Get the complement of this Sequence.
	 */
	public Bitstring complement()
	{
		boolean[] comp = new boolean[this.length()];
		for (int i = 0; i < this.length(); i++)
			comp[i] = !this.get(i);
		return new Bitstring(comp);
	}

	/**
	 * Get the reverse of this Sequence.
	 */
	public Bitstring reverse()
	{
		boolean[] comp = new boolean[this.length()];
		for (int i = this.length() - 1; i >= 0; i--)
			comp[i] = this.get(i);
		return new Bitstring(comp);
	}

	/**
	 * Get the reverse complement of this Sequence.
	 */
	public Bitstring reverseComplement()
	{
		boolean[] comp = new boolean[this.length()];
		for (int i = this.length() - 1; i >= 0; i--)
			comp[i] = !this.get(i);
		return new Bitstring(comp);
	}

	public static Bitstring cat(Bitstring... sequences)
	{
		if (sequences == null || sequences.length == 0)
			return EMPTY;
		int totlen = 0;
		for (Bitstring s : sequences)
			totlen += s.length();
		boolean[] result = new boolean[totlen];
		int i = 0;
		for (Bitstring s : sequences)
			for (boolean bit : s.seq)
			{
				result[i] = bit;
				i++;
			}
		return new Bitstring(result);
	}

	public static final Bitstring EMPTY = new Bitstring(new boolean[0]);

	public static Bitstring random(int length)
	{
		boolean[] seq = new boolean[length];
		Random rand = new Random();
		for (int i = 0; i < length; i++)
			if (rand.nextBoolean())
				seq[i] = true;
		return new Bitstring(seq);
	}

	@Override
	public boolean equals(Object obj)
	{
		assert obj instanceof Bitstring;
		Bitstring that = (Bitstring) obj;
		return Arrays.equals(this.seq, that.seq);
	}

	@Override
	public int hashCode()
	{
		return this.seq.hashCode();
	}

	/**
	 * Class representing an overlap between two sequences (a "left" and "right"
	 * sequence).
	 */
	public static class Overlap
	{
		public final int	leftStart;
		public final int	rightStart;
		public final int	length;

		private Overlap(int left, int right, int len)
		{
			this.leftStart = left;
			this.rightStart = right;
			this.length = len;
		}

		public static final Overlap EMPTY = new Overlap(0, 0, 0);

		@Override
		public String toString()
		{
			return "Overlap starting from (" + this.leftStart + ", "
			      + this.rightStart + ") of length " + this.length;
		}
	}

	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append('(');
		for (boolean b : this.seq)
		{
			result.append(b ? '1' : '0');
		}
		result.append(')');
		return result.toString();
	}

}
