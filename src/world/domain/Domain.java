package world.domain;

import java.util.Random;
import world.domain.Individual;

//-----------------------------------------------------------------------------

/**
 * Domain information.
 * @author Cameron Browne
 */
public abstract class Domain 
{
	/** Domain name. */
	protected String name = "Unknown";
	
	/** Number of bits. */
	protected int numBits;
	
	/** Mask for all potential on-bits. */
	protected long bitsMask;

	/** Random number generator. */
	public Random rng = new Random(System.currentTimeMillis());

	//-------------------------------------------------------------------------

	/**
	 * Constructor.
	 * @param name Domain name.
	 * @param numBits Number of bits required for genotype.
	 */
	public Domain(final String name, final int numBits)
	{		
		this.name    = name;		
		this.numBits = numBits;
		
		bitsMask = (0xffffffffffffffffL >>> (64 - numBits));  // genotype range 
	}
	
	//-------------------------------------------------------------------------
	// Virtual function -- *must* be implemented in derived class.

	/** 
	 * Constructs a new individual of the appropriate type.
	 * @param genotype Individual's genotype value.
	 * @return New individual.
	 * POST: Individual is repaired (i.e. complete) though not necessarily typical.
	 */
	public abstract Individual individual(final long genotype);
	
	//-------------------------------------------------------------------------

	/**
	 * @return Domain name.
	 */
	public String name()
	{
		return name;		
	}
		
	/**
	 * @return Number of bits.
	 */
	public int numBits()
	{
		return numBits;
	}
	
	/**
	 * @return Bits mask.
	 */
	public long bitsMask()
	{
		return bitsMask;
	}

	//------------------------------------------------------------------

	/**
	 * @return Random genotype within this domain's bit range.
	 */
	public long randomGenotype()
	{
		return (rng.nextLong() & bitsMask);
	}
	
}
