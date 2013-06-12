package world.domain;

import world.domain.Domain;

//-----------------------------------------------------------------------------

/**
 * Description of an individual and the properties specific to it.
 * @author Cameron Browne
 */
public abstract class Individual
{
	/** Current domain. */
	protected Domain domain;

	/** This individual's genotype (up to 64 bits). */
	protected long genotype;
		
	/** How typical this individual is of the genre (0..1). */
	protected double typicality;

	/** Quality estimate (0..1). */
	protected double quality;
		
	//-------------------------------------------------------------------------

	/**
	 * Constructor.
	 * @param domain Domain associated with this individual.
	 * @param genotype Genotype value.
	 */
	public Individual(final Domain domain, final long genotype)
	{
		this.domain   = domain;
		this.genotype = genotype;		
	}

	//-------------------------------------------------------------------------
	// Virtual functions -- *must* be implemented in derived class.
	
	/**
	 * Measure typicality: how typical this individual is of its domain (0..1).
	 */
	public abstract void measureTypicality();

	/**
	 * Measure quality: how interesting this individual is within its domain (0..1).
	 */
	public abstract void measureQuality();

	/**
	 * Export this individual to file (text, JSON, PDF, PostScript, image, sound file, etc).
	 * @param path Path to export file to.
	 * @return Name of file exported to, else "" on failure.
	 */
	public abstract String export(final String path);

	//-------------------------------------------------------------------------

	/**
	 * Repair this individual's genotype if under/over-represented, so that 
	 * it is complete (but not necessarily typical) of the domain.
	 */
	public void repair()
	{
		// Default behaviour: ensure that at least one bit is on
		if ((genotype & domain.bitsMask) == 0)
			genotype |= (1L << domain.rng.nextInt(domain.numBits()));  // turn a random bit on
	}

	//-------------------------------------------------------------------------
	
	/**
	 * @return Genotype.
	 */
	public long genotype()
	{
		return genotype;
	}
		
	/**
	 * @return Typicality value.
	 */
	public double typicality()
	{
		return typicality;
	}
	
	/**
	 * @return Overall quality (score).
	 */
	public double quality()
	{
		return quality;
	}
	
	//-------------------------------------------------------------------------

	/**
	 * PRE: measureTypicality() and measureQuality() have already been called.
	 * @return Fitness value of this individual.
	 */
	public double fitness()
	{
		return (typicality + quality) / 2.0;
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Measure typicality and quality.
	 */
	public void measure()
	{
		measureTypicality();
		measureQuality();
	}

}
