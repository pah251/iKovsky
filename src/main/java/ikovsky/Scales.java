package ikovsky;

/**
 * @author Paul Hudson
 * The Scales enum contains the different musical scales that iKovsky generates
 * must based upon, as well as their integer representations
 */
public enum Scales {
	MAJOR(new int[]{0,2,4,5,7,9,11}), //Natural Major
	MINOR(new int[]{0,2,3,5,7,8,10}), //Natural Minor
	
	MAJOR_PENTATONIC(new int[]{0,2,4,7,9}), //Major Pentatonic
	MINOR_PENTATONIC(new int[]{0,3,5,7,10}); //Minor Pentatonic
	
	private final int[] intervals;
	
	/**
	 * Constructor for a Scales enum entry
	 * @param intervals The array of int values of intervals within the scale
	 */
	Scales(int[] intervals) {
		this.intervals = intervals;
	}
	
	/**
	 * Function to get the scale intervals
	 * @return The int array of the scale intervals
	 */
	public int[] getScaleValues() {
		return intervals;
	}
}
