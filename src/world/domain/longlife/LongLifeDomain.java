package world.domain.longlife;

import world.domain.Individual;
import world.domain.Domain;

//-----------------------------------------------------------------------------

/**
 * Long Life: miniature 8x8 version of Conway's Game of Life.
 * @author Cameron Browne
 */
public class LongLifeDomain extends Domain 
{
	//-------------------------------------------------------------------------
	//	** Domain-specific details shared by all individuals. **
	
	/** Grid size (8x8). */
	protected static int dim = 8;

	/** Left edge bits. */
	protected static final long LEFT = 0x8080808080808080L;
	
	/** Right edge bits. */
	protected static final long RIGHT = 0x0101010101010101L;
	
	/** Top edge bits. */
	protected static final long TOP = 0x00000000000000FFL;
	
	/** Bottom edge bits. */
	protected static final long BOTTOM = 0xFF00000000000000L;
	
	/** Left edge bits mask. */
	protected static final long NOT_LEFT = ~LEFT;
	
	/** Right edge bits mask. */
	protected static final long NOT_RIGHT = ~RIGHT;

	//-------------------------------------------------------------------------
			
	/**
	 * Default constructor.
	 */
	public LongLifeDomain() 
	{
		super("Long Life", 64);  // give your class an appropriate name and bit size
	}

	//-------------------------------------------------------------------------
	
	@Override
	public Individual individual(final long genotype) 
	{
		LongLife indiv = new LongLife(this, genotype);  // <-- replace with your derived class
		indiv.repair();  // ensure is complete within this domain
		return indiv;
	}
	
	//-------------------------------------------------------------------------

}
