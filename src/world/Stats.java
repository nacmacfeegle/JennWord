package world;

import java.util.List;
import java.util.ArrayList;

//------------------------------------------------------------------------

/**
 * Basic statistics of a list of (double) samples.
 * @author Cameron Browne
 */
public class Stats 
{
	/** Description of what these statistics describe. */
	protected String label = "?";
	
	/** Samples. */
	protected List<Double> samples = new ArrayList<Double>();
	
	/** CI constant, from: http://mathworld.wolfram.com/StandardDeviation.html */
	//final double ci = 1.64485;  // for 90%
	protected final double ci = 1.95996;  // for 95%

	/** Mean. */
	protected double mean;
	
	/** Variance. */
	protected double varn;
	
	/** Standard deviation. */
	protected double devn;
	
	/** Confidence interval. */
	protected double conf;
	
	/** Minimum value. */
	protected double min;
	
	/** Maximum value. */
	protected double max;

	//---------------------------------------------
		
	/**
	 * Constructor 
	 * @param str Label.
	 */
	public Stats(final String str)
	{
		label = str;
	}
	
	//---------------------------------------------
	
	/**
	 * @return What these statistics describe.
	 */
	public String label()
	{
		return label;
	}
		
	/**
	 * @param lbl Description of these statistics.
	 */
	public void setLabel(final String lbl)
	{
		label = lbl;
	}

	/**
	 * Add a sample.
	 * @param val Sample to add.
	 */
	public void addSample(double val)
	{
		samples.add(Double.valueOf(val));
	}
	
	/**
	 * @param index Sample to get.
	 * @return Specified sample.
	 */
	public double get(int index)
	{
		return samples.get(index).doubleValue();
	}

	/**
	 * @return Number of samples.
	 */
	public int numSamples()
	{
		return samples.size();
	}

	/**
	 * @return Mean.
	 */
	public double mean()
	{
		return mean;
	}

	/**
	 * @return Variation.
	 */
	public double varn()
	{
		return varn;
	}

	/**
	 * @return Standard deviation.
	 */
	public double devn()
	{
		return devn;
	}

	/**
	 * @return Confidence interval (95%).
	 */
	public double conf()
	{
		return conf;
	}

	/**
	 * @return Minimum value.
	 */
	public double min()
	{
		return min;
	}

	/**
	 * @return Maximum value.
	 */
	public double max()
	{
		return max;
	}
	
	/**
	 * @return Range of values.
	 */
	public double range()
	{
		return max() - min();
	}
	
	/**
	 * @param list Sample list.
	 */
	public void set(List<Double> list)
	{
		samples = list;
	}
	
	//---------------------------------------------
	
	/**
	 * Clears this set of statistics.
	 */
	public void clear()
	{
		samples.clear();
		
		mean = 0;
		varn = 0;
		devn = 0;
		conf = 0;
		min  = 0;
		max  = 0;
	}
	
	//---------------------------------------------
	
	/**
	 * Measures stats from samples.
	 */
	public void measure()
	{
		mean = 0;
		varn = 0;
		devn = 0;
		conf = 0;
		min  = 0;
		max  = 0;
				
		if (samples.size() == 0)
			return;
		
		min = Double.POSITIVE_INFINITY;
		max = Double.NEGATIVE_INFINITY;
		
		//	Calculate mean
		for (Double val : samples)
		{
			mean += val.doubleValue();
			if (val.doubleValue() < min)
				min = val.doubleValue();
			if (val.doubleValue() > max)
				max = val.doubleValue();
		}
		mean /= samples.size();
			
		//	Variance 
		for (Double val : samples)
		{
			final double diff = val.doubleValue() - mean;
			varn += diff * diff;
		}
		varn /= samples.size();
		
		//	Standard deviation
		devn = Math.sqrt(varn);
	
		//	Confidence interval
		conf = 2 * ci * devn / Math.sqrt(samples.size());
	}
	
	/** 
	 * Shows stats.
	 */
	@SuppressWarnings("boxing")
	public void show()
	{
		System.out.printf("    N=%d, min=%.3f, max=%.3f, mean=%.3f (+/-%.3f).\n", samples.size(), min, max, mean, devn, conf);
	}
	
}
