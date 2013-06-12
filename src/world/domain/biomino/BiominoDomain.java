package world.domain.biomino;

import world.domain.Individual;
import world.domain.Domain;

import java.util.ArrayList;
import java.util.List;

//-----------------------------------------------------------------------------

/**
 * Biomino domain: biologically-inspired polyominoes  
 * (orthogonally connected sets of squares) in an 8x8 grid.
 * @author Cameron Browne
 */
public class BiominoDomain extends Domain 
{
	//-------------------------------------------------------------------------
	//	** Domain-specific details shared by all individuals. **
	
	/** Number of grid rows (8x8). */
	protected static int rows = 8;
	
	/** Number of gid columns (8x8). */
	protected static int cols = 8;

	/** List of orthogonal cell neighbours. */
	protected static List<Integer>[] cellNbors;

	/** Header for PostScript export. */
	protected static final String postScriptHeader = 
			"%!PS-Adobe-3.0\n" + 
			"%%BoundingBox: 0 0 170 170\n" + 
			"%%EndComments\n\n" + 
	 		"%------------------------------------\n\n" +
	 		"/u 20 def\n" + 
	 		"/dim 8 def\n\n" +
	 		"5 5 translate \n" +
	 		"0 setgray\n" +
	 		"0.5 setlinewidth\n\n" +
	 		"%------------- On cells -------------\n\n" +
	 		"0 setgray \n" +
	 		"newpath\n\n" +
	 		"/On \n" +
	 		"{   \n" +
	 		"    /col exch def\n" + 
	 		"    /row exch def\n" + 
	 		"    col u mul row u mul moveto 0 u rlineto u 0 rlineto 0 u neg rlineto closepath\n" +
	 		"} def\n";

	/** Trailer for Postcript export. */
	protected static final String postScriptTrailer = 
			"\nfill\n\n" +
			"%--------------- Grid ---------------\n\n" +
			"0.5 setgray\n" +
			"newpath\n" +
			"0 1 dim\n" +
			"{\n" +
			"   /n exch def\n" +
			"   0 n u mul moveto dim u mul 0 rlineto\n" + 
			"   n u mul 0 moveto 0 dim u mul rlineto\n" + 
			"} for\n" +
			"stroke\n\n" +
			"showpage\n"; 

	//-------------------------------------------------------------------------
			
	/**
	 * Default constructor.
	 */
	public BiominoDomain() 
	{
		super("Biomino", 64);  // give your class an appropriate name and bit size
		init();
	}

	//-------------------------------------------------------------------------
	
	@Override
	public Individual individual(final long genotype) 
	{
		Biomino indiv = new Biomino(this, genotype);  // <-- replace with your derived class
		indiv.repair();  // ensure is complete within this domain
		return indiv;
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Domain-specific initialisation.
	 */
	@SuppressWarnings({ "cast", "unchecked" })
	public void init()
	{	
		// Create list of orthogonal neighbour indices for each cell.
		cellNbors = (List<Integer>[]) new ArrayList[numBits];
		for (int cell = 0; cell < numBits; cell++)
		{
			cellNbors[cell] = new ArrayList<Integer>();
			
			final int row = cell / cols;
			final int col = cell % cols;
			
			if (row > 0)
				cellNbors[cell].add(Integer.valueOf((row - 1) * cols + col));
			
			if (row < rows - 1)
				cellNbors[cell].add(Integer.valueOf((row + 1) * cols + col));
			
			if (col > 0)
				cellNbors[cell].add(Integer.valueOf(row * cols + col - 1));
			
			if (col < cols - 1)
				cellNbors[cell].add(Integer.valueOf(row * cols + col + 1));
		}

	}
	
	//-------------------------------------------------------------------------

}
