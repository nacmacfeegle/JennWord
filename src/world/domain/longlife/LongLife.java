package world.domain.longlife;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import world.domain.Domain;
import world.domain.Individual;
import world.Utils;

//-----------------------------------------------------------------------------

/**
 * Long Life: miniature 8x8 version of Conway's Game of Life.
 * @author Cameron Browne
 */
public class LongLife extends Individual
{	
	/** First bit register for accumulating neighbor count. */
	protected long bit1; 
	
	/** Second bit register for accumulating neighbor count. */
	protected long bit2;
	
	/** Overflow bit (overflow into third bit means 4 or more nbors = dead). */
	protected long bit3;

	/** List of states for evaluation. */
	protected List<Long> states = new ArrayList<Long>();
	
	/** Maximum cycle period. */
	protected int maxPeriod = 512;
	
	/** Whether genotype defines a cyclic state. */
	protected boolean cyclic;
	
	/** Whether genotype defines a perfect cycle (returns to initial state). */
	protected boolean perfect;
	
	//-------------------------------------------------------------------------

	/**
	 * Constructor.
	 * @param domain Domain associated with this individual.
	 * @param genotype Genotype value.
	 */
	public LongLife(final Domain domain, final long genotype)
	{
		super(domain, genotype);
	}		

	//-------------------------------------------------------------------------

	/**
	 * A state is typical if it is cyclic.
	 * If not cyclic, then encourage longer sequences.
	 * PRE: At least one bit is on.
	 */
	@Override
	public void measureTypicality()
	{
		if (states.isEmpty())
			generateStates();
				
		if (perfect)
			typicality = 1;  // perfectly typical
		else if (cyclic)
			typicality = Math.log10(states.size() + 1) / 4;  // cyclic but not perfect
		else
			typicality = Math.log10(states.size() + 1) / 8;  // acyclic -- reward slightly
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Quality is based on dynamicism, i.e. the total number of bit changes between states.
	 */
	@Override
	public void measureQuality()
	{
		if (states.isEmpty())
			generateStates();

		int changes = 0;
		for (int s = 0; s < states.size(); s++)
		{
			final long stateA = states.get(s).longValue();
			final long stateB = states.get((s + 1) % states.size()).longValue();
			changes += Utils.countBits((stateA ^ stateB) & domain.bitsMask());
		}
		quality = Math.log10(changes + 1) / 4;
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Export phenotype to file.
	 */
	@Override
	public String export(final String path)
	{
		final String fileName = path + "/" + String.format("0x%x.gif", Long.valueOf(genotype));
		try 
		{
			exportAnimatedGif(fileName);
		} 
		catch (Exception e) 
		{
			return "";  // failed to create or open file
	    }
		return fileName;
	}

	/**
	 * Renders the generated states to an animated gif.
	 * @param fileName
	 * @throws Exception
	 */
	void exportAnimatedGif(final String fileName) throws Exception
	{
		BufferedImage img = render(genotype);

		ImageOutputStream output = new FileImageOutputStream(new File(fileName));
		GifSequenceWriter writer = new GifSequenceWriter(output, img.getType(), 500, false);

		writer.writeToSequence(img);
		for (int i = 1; i < states.size(); i++)
		{
			img = render(states.get(i).longValue());
			writer.writeToSequence(img);
		}
		writer.close();
		output.close();
	}

	/**
	 * Renders the specified state to image.
	 * @param state State to render.
	 * @return Rendered image.
	 */
	BufferedImage render(final long state)
	{
		final int u = 15;  // unit size per cell
		
		final int x0 = 1;
		final int y0 = 1;
		
		final int sx = u * LongLifeDomain.dim + 3;
		final int sy = u * LongLifeDomain.dim + 3;
				
		BufferedImage img = new BufferedImage(sx, sy, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D)img.getGraphics();
       
		g2d.setColor(new Color(255, 255, 255));
		g2d.fillRect(0, 0, sx, sy);   
		
		final Color color0 = new Color(245, 240, 215);  // off-pixel
		final Color color1 = new Color(  0,   0,   0);  // on-pixel
	    
		for (int b = 0; b < domain.numBits(); b++)
		{
			final int x = x0 + u * (b % LongLifeDomain.dim);
			final int y = y0 + u * (b / LongLifeDomain.dim);
			g2d.setColor(Utils.bitOn(state,  b) ? color1 : color0);
			g2d.fillRect(x+1, y+1, u-1, u-1);
		}		
		return img;
	}

	//-------------------------------------------------------------------------

	/**
	 * @return Whether genotype defines a cyclic state.
	 */
	public boolean cyclic()
	{
		return cyclic;
	}
	
	/**
	 * @return Whether genotype defines a perfect cycle (returns to initial state).
	 */
	public boolean perfect()
	{
		return perfect;
	}
	
	//-------------------------------------------------------------------------

	/**
	 * Generates states until cycle found or maxPeriod exceeded.
	 */
	void generateStates()
	{
		cyclic  = false;
		perfect = false;

		long state = genotype;  // initial state
		states.clear();
		states.add(Long.valueOf(state));
				
		// Apply B2/S23 Life rules until cycle found, all cells dead or maxPeriod reached
		for (int s = 0; s < maxPeriod; s++)
		{
			state = step(state);
			if (states.contains(Long.valueOf(state)))
			{
				// Cycle found
				cyclic = true;
				if (state == genotype)
					perfect = true;  // perfect cycle -- genotype is part of cycle
				break;
			}
			states.add(Long.valueOf(state));
			if (state == 0)
				break;  // all dead -- nothing to see, move along...
		}
	}
	
	//-------------------------------------------------------------------------
	//	Bitwise-parallel implementation of 8x8 Life.
	//	See: http://www.aifactory.co.uk/newsletter/index.htm
	
	/**
	 * Add neighbor cXX to the current bit register values, with carry.
	 * @param cXX Input value to add.
	 */
	void add(final long cXX)
	{
		final long carry1 = bit1 & cXX;
		final long carry2 = bit2 & carry1;
		
		bit1 ^= cXX;
		bit2 ^= carry1;
		bit3 |= carry2;
	}
	
	/**
	 * Perform one step of the B2/S23 Life algorithm on an 8x8 grid, with wraparound.
	 * 
	 * Fast bitwise-parallel approach by Stephen Tavener.
	 * 	
	 * @param c11 Input state.
	 * @return Resulting state.
	 */
	long step(final long c11) 
	{
		//	Shift neighbors into position, with wraparound
		final long c10 = c11 >>> 8 | ((c11 & LongLifeDomain.TOP) << 56);
		final long c12 = c11  << 8 | ((c11 & LongLifeDomain.BOTTOM) >>> 56);
		final long c00 = (c10 & LongLifeDomain.NOT_LEFT)   << 1 | ((c10 & LongLifeDomain.LEFT) >>> 7);
		final long c01 = (c11 & LongLifeDomain.NOT_LEFT)   << 1 | ((c11 & LongLifeDomain.LEFT) >>> 7);
		final long c02 = (c12 & LongLifeDomain.NOT_LEFT)   << 1 | ((c12 & LongLifeDomain.LEFT) >>> 7);
		final long c20 = (c10 & LongLifeDomain.NOT_RIGHT) >>> 1 | ((c10 & LongLifeDomain.RIGHT) << 7);
		final long c21 = (c11 & LongLifeDomain.NOT_RIGHT) >>> 1 | ((c11 & LongLifeDomain.RIGHT) << 7);
		final long c22 = (c12 & LongLifeDomain.NOT_RIGHT) >>> 1 | ((c12 & LongLifeDomain.RIGHT) << 7);

		// Reset the bit registers 
		bit1 = 0;  
		bit2 = 0;
		bit3 = 0; 
		
		//	Accumulate the neighbor count
		add(c00);
		add(c01);
		add(c02);
		add(c10);
		add(c12);
		add(c20);
		add(c21);
		add(c22);
		
		// bit1, bit2 and overflow now hold the results of the addition of the
		// 8 neighbors. If overflow is 1 then this total is >= 4 (i.e. dead).
		//
		// Now, recall the basic algorithm:
		// -- If alive (1), stay alive IFF neighbors sum to 2 or 3
		// -- If dead (0), become alive IFF neighbors sum to 3
		//
		// Each cell is therefore alive in the following cases, otherwise dead:
		//    c11  bit1 bit2
		//     0    1    1
		//     1    0    1
		//     1    1    1
		//
		// (c11  |  bit1) gives 1 if alive...
		//       &  bit2  gives 1 if alive... 
		//       & ~bit3  in case of overflow beyond 3 neighbors.	
		
		return ((c11 | bit1) & bit2 & ~bit3);
	}

	//-------------------------------------------------------------------------

	// Make every cycle perfect.
	@Override
	public void repair()
	{
		generateStates();
		if (cyclic && !perfect)
		{
			// Make perfect by setting the genotype to a state within the cycle
			genotype = states.get(states.size()-1).longValue();
		}
		states.clear();
 
		if (genotype == 0)
			genotype |= (1L << domain.rng.nextInt(domain.numBits()));  // turn a random bit on
	}

	//-------------------------------------------------------------------------

}
