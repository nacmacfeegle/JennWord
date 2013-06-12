package world.domain.jenn;

import world.domain.Domain;
import world.domain.Individual;

public class JennDomain extends Domain {
			
	/**
	 * Default constructor.
	 */
	public JennDomain() {
		super("Jenn", 64);  // give your class an appropriate name and bit size
		init();
	}

	//-------------------------------------------------------------------------
	
	@Override
	public Individual individual(final long genotype) {
		Jenn indiv = new Jenn(this, genotype);  // <-- replace with your derived class
		indiv.repair();  // ensure is complete within this domain
		return indiv;
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Domain-specific initialisation.
	 */
	@SuppressWarnings({ "cast", "unchecked" })
	public void init() {	
	}
	
	//-------------------------------------------------------------------------

}
