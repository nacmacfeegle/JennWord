package search;

import world.domain.Individual;

//-----------------------------------------------------------------------------

/**
 * Flat Monte Carlo (i.e. strictly random) search.
 * 
 * @author Cameron Browne
 */
public class SearchMC extends Search {
	
	private boolean userQuit = false;
	/**
	 * Constructor.
	 */
	public SearchMC() {
		super("MC");
	}

	// -------------------------------------------------------------------------

	/**
	 * Perform a flat Monte Carlo (i.e. strictly random) search.
	 */
	@Override
	public void search() {
		while (numTried < budget && !userQuit) // each iteration involves one measure()
		{
			// Create a random individual
			Individual indiv = domain.individual(domain.randomGenotype());
			indiv.measure();
			if (indiv.quality() == -1) {
				userQuit = true;
			}
			numTried++;

			if (indiv.typicality() >= typicalityThreshold) {
				master.insert(indiv); // add individual to master population
				numTypical++;
			}
		}
	}

}
