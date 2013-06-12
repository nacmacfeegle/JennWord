package world;

import java.util.ArrayList;
import java.util.List;

import world.Utils;
import world.domain.Individual;

//-----------------------------------------------------------------------------

/**
 * Population of individuals (artefacts) from creative search.
 * @author Cameron Browne
 */
public class Population 
{
	/** Current population. */
	protected List<Individual> members = new ArrayList<Individual>();
		
	/** Number of duplicates within population. */
	protected int numCollisions;
	
	/** Maximum number of members allowed in population. */
	protected final int maxMembers = 10000;
	
	/** Diversity sample size. */
	protected final int sampleSize = 1000;
	
	//-------------------------------------------------------------------------

	/**
	 * @return Size of population.
	 */
	public int size()
	{
		return members.size();
	}

	/**
	 * @param i Index.
	 * @return Individual at position i.
	 */
	public Individual get(final int i)
	{
		return members.get(i);
	}
	
	/**
	 * @return Number of duplicates in population.
	 */
	public int numCollisions()
	{
		return numCollisions;
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Clears the current population.
	 */
	public void clear()
	{
		members.clear();
		numCollisions = 0;
	}
	
	//-------------------------------------------------------------------------
	
	/**
	 * Insert individual in the population if unique, ordered by fitness.
	 * 
	 * Do insertion and collision testing manually -- is faster for ArrayLists.
	 * 
	 * @param indiv Individual to be inserted.
	 */
	public void insert(final Individual indiv)
	{
		final int count = members.size();
		int insertAt = count;
		
		for (int i = 0; i < members.size(); i++)
		{
			final Individual member = members.get(i);
			if (member.genotype() == indiv.genotype())
			{
				numCollisions++;
				return;  // don't add duplicates
			}
			if (insertAt == count && member.fitness() < indiv.fitness())
				insertAt = i;  // insert at position i
		}		
		members.add(insertAt, indiv);
		
		while (members.size() > maxMembers)
			members.remove(members.size() - 1);  // remove worst performer
	}

	//-------------------------------------------------------------------------

    /**
     * Measures diversity using Morrison & De Jong's centre of intertia (COI) method.
     * From: http://gogoshen.org/jn/Shared Documents/Measurement of Population Diversity.pdf
     */
    public void measureDiversityCOI()
    {
    	if (members.size() == 0)
    		return;
    	
    	final int numBits = Utils.world.domain().numBits();
    	final int numToMeasure = Math.min(sampleSize, members.size() / 10);  // measure best 10% of population
		
    	double[] centroids = new double[numBits];

    	//	Calculate centres of interia
    	for (int i = 0; i < numToMeasure; i++)
    	{
    		final long genotype = members.get(i).genotype();
    		for (int b = 0; b < numBits; b++)
    			centroids[b] += (Utils.bitOn(genotype,  b) ? 1 : 0);
    	}
   		for (int b = 0; b < numBits; b++)
			centroids[b] /= numToMeasure;
     	
    	//	Measure variance
   		double varnTotal = 0;
    	for (int i = 0; i < numToMeasure; i++)
    	{
      		final long genotype = members.get(i).genotype();   	
     		double varn = 0;
        	for (int b = 0; b < numBits; b++)
    		{
    			double diff = centroids[b] - (Utils.bitOn(genotype,  b) ? 1 : 0);
    			varn += diff * diff;
    		}
        	varn /= numBits;
        	varnTotal += varn;
    	}
    	varnTotal /= numToMeasure;
    	
    	System.out.printf("    Population (COI) diversity is %.3f.\n", Double.valueOf(varnTotal));
    }
     	
}
