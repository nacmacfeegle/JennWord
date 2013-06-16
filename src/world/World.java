package world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import search.Search;
import search.SearchGA;
import search.SearchMC;
import world.domain.Domain;
import world.domain.Individual;
import world.domain.jenn.JennDomain;

//-----------------------------------------------------------------------------

/**
 * Main application for the "World in a Word" competition.
 * 
 * @author Cameron Browne
 */
public class World {
	/** Current domain. */
	protected Domain domain = new JennDomain();

	/** Master population. */
	protected Population population = new Population();

	/** List of search objects. */
	private List<Search> searches = new ArrayList<Search>();

	// -------------------------------------------------------------------------

	/**
	 * @return Currently active domain.
	 */
	public Domain domain() {
		return domain;
	}

	/**
	 * @return Master population.
	 */
	public Population population() {
		return population;
	}

	// -------------------------------------------------------------------------

	/**
	 * Perform non-static initialisation.
	 */
	private void init() {
		Utils.world = this; // set before constructing Search objects

		// searches.add(new SearchMC());
		searches.add(new SearchGA());
	}

	// -------------------------------------------------------------------------

	/**
	 * Run the experimental trials.
	 */
	void generate() {
		// final int budget = 100000; // number of individuals to measure
		final int budget = 10; // number of individuals to measure

		System.out.println("Domain: " + domain.name());

		for (int s = 0; s < searches.size(); s++) {
			final Search search = searches.get(s);
			population.clear();
			search.conduct(budget);
		}
		exportExamples();
	}

	// -------------------------------------------------------------------------

	/**
	 * Export representative individuals to file.
	 */
	@SuppressWarnings("boxing")
	void exportExamples() {
		final int numToExport = 10;

		// Create "out" directory if needed
		final String path = new String("out");
		File dir = new File(path);
		if (!dir.exists() && !dir.mkdir()) {
			System.out.println("Failed to create directory " + path + ".");
			return;
		}

		// Empty the directory
		final String[] files = dir.list();
		for (int i = 0; i < files.length; i++) {
			File file = new File(dir, files[i]);
			file.delete();
		}

		// Create summary file and export
		final String fileName = path + "/summary.txt";
		try {
			PrintStream out = new PrintStream(new FileOutputStream(fileName));

			// Export representative individuals
			for (int i = 0; i < numToExport && i < population.size(); i++) {
				final Individual indiv = population.get(i);
				final String exportedTo = indiv.export(path);
				if (exportedTo == "") {
					System.out.println("Failed to save file " + fileName + ".");
				} else {
					out.printf("%s: typicality=%.3f, quality=%.3f.\n",
							exportedTo, indiv.typicality(), indiv.quality());
				}
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------

	/**
	 * Main routine.
	 * 
	 * @param args
	 *            program arguments.
	 */
	public static void main(String[] args) {
		System.out.println("WordWorld64 1.0\n");

		World world = new World();
		world.init();
		world.generate();

		System.out.println("\nDone.");
	}

}
