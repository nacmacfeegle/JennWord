package world.domain.biomino;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import world.domain.Domain;
import world.domain.Individual;
import world.Utils;

//-----------------------------------------------------------------------------

/**
 * Individual biomino: biologically-inspired polyomino  
 * (orthogonally connected set of squares) in an 8x8 grid.
 * @author Cameron Browne
 */
public class Biomino extends Individual
{	
	/**
	 * Constructor.
	 * @param domain Domain associated with this individual.
	 * @param genotype Genotype value.
	 */
	public Biomino(final Domain domain, final long genotype)
	{
		super(domain, genotype);
	}		

	//-------------------------------------------------------------------------

	/**
	 * Typicality is the distance of this individual from a perfect polyomino, 
	 * i.e. the ratio of orthogonally connected on-pixels to the total number of on-pixels.
	 * PRE: At least one bit is on.
	 */
	@Override
	public void measureTypicality()
	{
		final int numOn = Utils.countBits(genotype & domain.bitsMask());
		final int maxGroup = maxGroupSize();
		
		typicality = Math.pow(1 - (numOn - maxGroup) / (double)numOn, 2);
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Quality is a linear combination of:
	 * 1. Maximise horizontal symmetry.
	 * 2. Minimise vertical symmetry.
	 * 3. Maximise surface area (on-pixels with off-pixel neighbours).
	 * PRE: At least one bit is on.
	 */
	@Override
	public void measureQuality()
	{
		final double symmetryH = symmetry(true);
		final double symmetryV = symmetry(false);
		final double surface   = surfaceArea();
		
		quality = (symmetryH + (1 - symmetryV) + surface) / 3.0;	
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Export phenotype to file.
	 */
	@Override
	public String export(final String path)
	{
		final String fileName = path + "/" + String.format("0x%x.eps", Long.valueOf(genotype));
		try 
		{
			PrintStream out = new PrintStream(new FileOutputStream(fileName));
			
			// PostScript export (*.eps)
			out.print(BiominoDomain.postScriptHeader);
			for (int b = 0; b < domain.numBits(); b++)
				if (Utils.bitOn(genotype, b))
				{
					final int row = b / BiominoDomain.cols;
					final int col = b % BiominoDomain.cols;
					
					out.println(row + " " + col + " " + "On");
				}
			out.print(BiominoDomain.postScriptTrailer);

//			// Plain text export (*.txt)			
//			for (int b = 0; b < domain.numBits(); b++)
//			{
//				out.print(Utils.bitOn(genotype, b) ? "+" : ".");
//				if ((b + 1) % BiominoDomain.cols == 0)
//					out.println();
//			}

			out.close();
		} 
		catch (FileNotFoundException e) 
		{
			return "";  // failed to create or open file
	    }
		return fileName;
	}

	//-------------------------------------------------------------------------

	/**
	 * Measure symmetry in one direction.
	 * PRE: At least one bit is on.
	 * @param horz Direction (whether horz or vert).
	 * @return Ratio of matching pixels (0..1). 
	 */
	public double symmetry(final boolean horz)
	{	
		final int numOn = Utils.countBits(genotype & domain.bitsMask());
		
		int matches = 0;		
		for (int b = 0; b < domain.numBits(); b++)
		{		
			if (Utils.bitOn(genotype, b))
			{
				// Check if this on-bit has a symmetrically matching on-bit
				final int row = b / BiominoDomain.cols;
				final int col = b % BiominoDomain.cols;
				
				final int bb = horz 
						? (row * BiominoDomain.cols + BiominoDomain.cols - 1 - col)
						: ((BiominoDomain.rows - 1 - row) * BiominoDomain.cols + col);
				if (Utils.bitOn(genotype, bb))
					matches++;
			}
		}
		return matches / (double)numOn;
	}

	//-------------------------------------------------------------------------

	/**
	 * Measure surface area of on-bits.
	 * PRE: At least one bit is on.
	 * @return Ratio of on-pixels with off-pixel neighbours (0..1). 
	 */
	public double surfaceArea()
	{
		int totalOff   = 0;
		int totalNbors = 0;
		
		for (int b = 0; b < domain.numBits(); b++)
		{
			if (Utils.bitOn(genotype, b))
			{
				// Count number of off-pixel neighbours
				for (Integer nbor : BiominoDomain.cellNbors[b])
					if (!Utils.bitOn(genotype, nbor.intValue()))
						totalOff++;
				
				// Subtract 1 as at least one cell should connect to group
				totalNbors += BiominoDomain.cellNbors[b].size() - 1;
			}
		}
		return totalOff / (double)totalNbors;
	}

	//-------------------------------------------------------------------------

	/** For rough working. */
	long group;
	
	/**
	 * @return Maximum orthogonal group size.
	 */
	public int maxGroupSize()
	{
		long visited = 0;
		int maxGroupSize = 0;

		for (int b = 0; b < domain.numBits(); b++)
			if (Utils.bitOn(genotype, b) && !Utils.bitOn(visited, b))
			{
				// Visit this group
				group = 0;
				visitGroup(b);
				visited |= group;  // mark this entire group as "visited"
				
				final int groupSize = Utils.countBits(group & domain.bitsMask());			
				if (groupSize > maxGroupSize)
					maxGroupSize = groupSize;  // largest group so far
			}
		return maxGroupSize;
	}
	
	/**
	 * Visit the specified cell and floods unvisited passable nbors.
	 * @param bitIndex Bit to visit.
	 */
	protected void visitGroup(final int bitIndex)
	{
		group |= (1L << bitIndex);
		for (Integer nbor : BiominoDomain.cellNbors[bitIndex])
		{
			final int ni = nbor.intValue();
			if (Utils.bitOn(genotype, ni) && !Utils.bitOn(group, ni))
				visitGroup(ni);
		}
	}

	//-------------------------------------------------------------------------

}
