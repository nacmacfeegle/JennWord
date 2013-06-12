package world.domain.jenn;

import world.domain.Domain;
import world.domain.Individual;

//-----------------------------------------------------------------------------

public class Jenn extends Individual {	
	/**
	 * Constructor.
	 * @param domain Domain associated with this individual.
	 * @param genotype Genotype value.
	 */
	public Jenn(final Domain domain, final long genotype) {
		super(domain, genotype);
	}		

	//-------------------------------------------------------------------------

	/**
	 * 
	 */
	@Override
	public void measureTypicality() {
		// TODO
		typicality = 0.5;
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Quality is interactively assigned
	 */
	@Override
	public void measureQuality() {
		
		// TODO
		// make system call, asking Jenn to store the result at a specific location
		// wait for file to exist at the location (use timeout and default to score 0 in that case)
		// once file available, read fitness score and return
		
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Export phenotype to file.
	 */
	@Override
	public String export(final String path) {
		final String fileName = path + "TODO" ;
		return fileName;
	}

	//-------------------------------------------------------------------------

}
