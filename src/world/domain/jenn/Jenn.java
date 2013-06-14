package world.domain.jenn;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;

import world.domain.Domain;
import world.domain.Individual;

//-----------------------------------------------------------------------------

public class Jenn extends Individual {	
	
	public static final String JENN_DIR = File.separator + "GEMapJenn";

	private double cachedFitness = 0;
	
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
	 * Derived from the fitness. If the individual doesn't map, it gets a zero value.
	 * If it maps but crashes Jenn, it gets 0.5. Otherwise it gets a value of 1.
	 */
	@Override
	public void measureTypicality() {
		if (cachedFitness == 0) {
			typicality = 0;
		} else if (cachedFitness == 0.5) {
			typicality = 0.5;
		} else {
			typicality = 1.0;
		}
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
		
		DefaultExecutor executor = new DefaultExecutor();
		String workingDir = executor.getWorkingDirectory().getAbsolutePath() + JENN_DIR;
		String command = workingDir + File.separator + "fakejenn";
		CommandLine commandLine = CommandLine.parse(command);
		commandLine.addArgument(convertGenotype());
		executor.setWorkingDirectory(new File(workingDir));
		try {
			int exitValue = executor.execute(commandLine);
			
			File fitnessFile = new File(workingDir + File.separator + "fitness.txt");
			String ftnsStr = FileUtils.readFileToString(fitnessFile).trim();
			quality = Double.parseDouble(ftnsStr);
			
		} catch (IOException ioe) {
			quality = 0;
		} 
	
		// save last value
		cachedFitness = quality;
	}

	/**
	 * Converts the genotype to a Jenn-friendly input
	 * @return a space-separated 64-bit binary string 
	 */
	private String convertGenotype() {
		String tmp = Long.toBinaryString(genotype);
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<tmp.length(); i++) {
			sb.append(tmp.charAt(i));
			sb.append(" ");
		}
		return sb.toString();
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
	public void measure() {
		// changing the standard order for this subclass since typicality is derived from the quality
		measureQuality();
		measureTypicality();
	}

}
