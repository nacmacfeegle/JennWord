package search;

import world.domain.Individual;
import world.Population;

//-----------------------------------------------------------------------------

/**
 * Standard GA search.
 * 
 * @author Cameron Browne
 */
public class SearchGA extends Search {
	/** Tournament size for selection. */
	protected int tournamentSize = 4;

	/** Crossover rate as a percentage (0..100). */
	protected int crossoverRate = 50;

	/** Crossover type (0=uniform, 1=single-point, 2=double-point). */
	protected int crossoverType = 0;

	/** Local populations, alternate with each generation. */
	protected Population[] pops;

	/** Local population size. */
	protected int popSize = 10;

	/** Generation count during search. */
	protected int generation;
	

	// -------------------------------------------------------------------------

	/**
	 * Constructor.
	 */
	public SearchGA() {
		super("GA");

		pops = new Population[2];
		pops[0] = new Population();
		pops[1] = new Population();
	}

	// -------------------------------------------------------------------------

	@Override
	public void search() {
		// Seed the population
		generation = 0;
		while (pops[0].size() < popSize & !userQuit) {
			Individual indiv = domain.individual(domain.randomGenotype());
			indiv.measure();
			if (indiv.quality() == -1) {
				userQuit = true;
				continue;
			}
			numTried++;
			addToPopulation(indiv, pops[0]);
		}

		// Breed until budget met
		while (numTried < budget && !userQuit) {
			// Breed the next generation
			final Population nextPop = pops[(generation + 1) % 2];
			nextPop.clear();

			while (nextPop.size() < popSize && numTried < budget) {
				Individual indiv = breedChild();
				indiv.measure();
				if (indiv.quality() == -1) {
					userQuit = true;
					break;
				}
				numTried++;
				addToPopulation(indiv, nextPop);
			}
			generation++;
		}
	}

	// -------------------------------------------------------------------------

	/**
	 * Add the specified individual to the specified population.
	 * 
	 * @param indiv
	 *            Individual to add.
	 * @param pop
	 *            Population.
	 */
	void addToPopulation(final Individual indiv, final Population pop) {
		pop.insert(indiv);

		if (indiv.typicality() >= typicalityThreshold) {
			master.insert(indiv); // also add individual to master population
			numTypical++;
		}
	}

	// -------------------------------------------------------------------------

	/**
	 * @return Individual bred from current population.
	 */
	Individual breedChild() {
		long genotype;

		final long genotypeA = selectParent().genotype();

		if (rng.nextInt(100) < crossoverRate) {
			// Crossover with second parent (don't care if same individual)
			final long genotypeB = selectParent().genotype();
			genotype = crossOver(genotypeA, genotypeB, crossoverType);
			do {
				genotype = mutate(genotype);
			} while (genotype == genotypeA || genotype == genotypeB);
		} else {
			// No crossover
			do {
				genotype = mutate(genotypeA);
			} while (genotype == genotypeA);
		}
		return domain.individual(genotype);
	}

	// -------------------------------------------------------------------------

	/**
	 * @return Parent from population using tournament selection.
	 */
	Individual selectParent() {
		Individual parent = null;
		double maxScore = -1000;

		final Population pop = pops[generation % 2];

		for (int t = 0; t < tournamentSize; t++) {
			final int pid = rng.nextInt(pop.size());
			Individual indiv = pop.get(pid);
			if (indiv.fitness() > maxScore) {
				parent = indiv;
				maxScore = indiv.fitness();
			}
		}
		return parent;
	}

	// -------------------------------------------------------------------------

	/**
	 * Perform crossover between two parents.
	 * 
	 * @param genotypeA
	 *            Genotype from first parent.
	 * @param genotypeB
	 *            Genotype from second parent.
	 * @param xType
	 *            Crossover type (0=uniform, 1=single-point, 2=double-point).
	 * @return Bits resulting from crossover.
	 */
	public long crossOver(final long genotypeA, final long genotypeB,
			final int xType) {
		long mask = 0;

		switch (xType) {
		case 0: // Uniform crossover
			mask = rng.nextLong();
			break;
		case 1: // Single-point crossover
			mask = (1L << (rng.nextInt() % domain.numBits()));
			break;
		case 2: // Double-point crossover
			mask = (1L << (rng.nextInt() % domain.numBits()))
					^ (1L << (rng.nextInt() % domain.numBits()));
			break;
		default:
			System.out.println("bad crossover type " + xType + ".");
		}

		if (rng.nextInt(2) == 0)
			mask = ~mask; // randomly invert to swap order of parents

		return ((genotypeA & mask) | (genotypeB & ~mask)) & domain.bitsMask();
	}

	// -------------------------------------------------------------------------

	/**
	 * Mutate B genotype bits with frequency 1/B.
	 * 
	 * @param genotype
	 *            Genotype to mutate.
	 * @return Mutated genotype.
	 */
	public long mutate(final long genotype) {
		long result = genotype;
		for (int b = 0; b < domain.numBits(); b++)
			if (rng.nextInt(domain.numBits()) == 0)
				result ^= (1L << b);
		return result;
	}

	// -------------------------------------------------------------------------

}
