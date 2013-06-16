package search;

import world.domain.Domain;
import world.Population;
import world.Stats;
import world.Utils;

import java.util.Random;

//-----------------------------------------------------------------------------

/**
 * AI search class.
 * @author Cameron Browne
 */
public abstract class Search 
{	
	/** Search identifier. */
	protected String name = "Search";

	/** Reference to current domain. */
	protected Domain domain = Utils.world.domain();	
	
	/** Reference to master population. */
	protected Population master = Utils.world.population();
	
	/** Computational budget = how long we can search. */
	protected int budget;
	
	/** Total number of individuals tried. */
	protected int numTried;
	
	/** Total number of typical individuals found. */
	protected int numTypical;
	
	/** Threshold for acceptance into population (0..1). */
	protected final double typicalityThreshold = 0.5;

	/** Random number generator. */
	protected static Random rng = new Random(System.currentTimeMillis());

	/** Facilitator for interactive -- has the user terminated */
	protected boolean userQuit = false;

	//-------------------------------------------------------------------------
		
	/**
	 * Constructor.
	 * @param name Search identifier.
	 */
	public Search(final String name)
	{
		this.name = name;
	}

	//-------------------------------------------------------------------------

	/**
	 * @return Name of this search.
	 */
	public String name()
	{
		return name;
	}
	
	/**
	 * @return Total number tried so far.
	 */
	public int numTried()
	{
		return numTried;
	}
	
	//-------------------------------------------------------------------------

    /**
     * Conduct search within the specified computational budget.
     * @param num Computational budget.
     */
 	@SuppressWarnings("boxing")
	public void conduct(final int num)
    {
 		budget = num;	
		
		//	Run the search
		System.out.print("\n" + name() + ": ");
		long startAt = System.currentTimeMillis();	
		
		numTried = 0;
		search();	
		
		final double searchTime = (System.currentTimeMillis() - startAt) / 1000.0;				
		System.out.printf("%d typical examples out of %d tried in %.3fs (%d collisions).\n", 
				numTypical, numTried, searchTime, master.numCollisions());
		
		//	Show the result
		final Stats stats = new Stats(name());
		for (int i = 0; i < master.size(); i++)
			stats.addSample(master.get(i).fitness());
		stats.measure();
		stats.show();
		
		master.measureDiversityCOI();
    }

	//---------------------------------------------------------------------

    /**
     * Override with desired search algorithm.
     */
    abstract public void search(); 

	//---------------------------------------------------------------------

}
