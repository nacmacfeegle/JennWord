package world;

//---------------------------------------------------------------------

/** 
 * Global constants and utility functions. 
 * @author Cameron Browne
 */
public final class Utils
{
	/** Reference to main application, for access to domain, master population, etc. */
	public static World world;
	
	//--------------------------------------------------------------
	//	Bitwise operations

	/**
	 * @param bits Source bits.
	 * @param b Bit index to check.
	 * @return Whether the specified bit is on.
	 */
	public static boolean bitOn(final long bits, final int b)
	{
		return (bits & (1L << b)) != 0;
	}

	/**
	 * Count number of on-bits.
	 * From http://graphics.stanford.edu/~seander/bithacks.html#CountBitsSetKernighan
	 * @param bits Source bits.
	 * @return Number of on-bits.
	 */
	public static int countBits(final long bits)
	{
		int count;
		long val = bits;
		for (count = 0; val != 0; count++)
		  val &= val - 1;  // clear the least significant bit set
		return count;
	}
	
	//--------------------------------------------------------------

}
