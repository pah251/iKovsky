package ikovsky;

/**
 * @author Paul Hudson
 * ChordStructs Enum
 * Holds information on different chord structures and their intervals
 */
public enum ChordStructs {
	MAJOR_TRIAD(new int[]{0,4,7}), //First, Third and Fifth
	MINOR_TRIAD(new int[]{0,3,7}), //First, Flat Third and Fifth
	FIFTH(new int[]{0,7}), //First and Fifth
	MAJOR_SEVENTH(new int[]{0,4,7,10}), //First, Third, Fifth and Seventh
	MINOR_SEVENTH(new int[]{0,3,7,9}),	//First, Flat Third, Fifth and Seventh
	DIMINISHED(new int[]{0,3,6}); //First, Flat Third, Flat Seventh
	
	private int[] chordIntervals; //Array of intervals
	
	/**
	 * Constructor for a ChordStructs enum
	 * @param chordIntervals The int array of intervals in the chord
	 */
	ChordStructs(int[] chordIntervals) {
		this.chordIntervals = chordIntervals;
	}
	
	/**
	 * Function to get the chord intervals
	 * @return integer array chord of the intervals
	 */
	public int[] getChordIntervals() {
		int[] newChordIntervals = new int[this.chordIntervals.length];
		
		for (int i = 0 ; i < newChordIntervals.length; i++) {
			newChordIntervals[i] = this.chordIntervals[i];
		}
		
		return newChordIntervals;
	}	
}
