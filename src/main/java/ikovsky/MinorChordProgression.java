package ikovsky;

/**
 * @author Paul Hudson
 * MinorChordProgression Enum
 * Holds all the different chords, as well as an alternative structure for chords
 * that follow the Minor Chord Progression 
 */
public enum MinorChordProgression {
	//ScaleChords defaults to a Major chord progression
	I	(ChordStructs.MINOR_TRIAD, 	0, ChordStructs.FIFTH),
	II	(ChordStructs.FIFTH,		2, ChordStructs.DIMINISHED),
	III	(ChordStructs.MAJOR_TRIAD,	3, ChordStructs.MAJOR_SEVENTH),
	IV	(ChordStructs.MINOR_TRIAD,	5, ChordStructs.MINOR_SEVENTH),
	V	(ChordStructs.MINOR_TRIAD,	7, ChordStructs.MINOR_SEVENTH),
	VI	(ChordStructs.MAJOR_TRIAD,	8, ChordStructs.MAJOR_SEVENTH),
	VII	(ChordStructs.MAJOR_TRIAD,	10, ChordStructs.MAJOR_SEVENTH);
	
	private ChordStructs struct;
	private ChordStructs altStruct;
	
	private int root;
	
	/**
	 * Constructor for a MinorChordProgression enum
	 * @param struct, The associated ChordStruct enum
	 * @param root, The integer value of the root of the chord within the current key
	 * @param altStruct, The alternative ChordStruct enum
	 */
	private MinorChordProgression (ChordStructs struct, int root, ChordStructs altStruct) {
		this.struct = struct;
		this.altStruct = altStruct;

		this.root = root;
	}

	/**
	 * Function to get the chord intervals
	 * @return the integer array of the chord intervals
	 */
	public int[] getChordStruct() {
		if (Math.random() < IKConstants.CHORDPICKER_ALT_STRUCT_CHANCE) {
			return altStruct.getChordIntervals();
		}
		return struct.getChordIntervals();
	}
	
	/**
	 * Function to get the chord structure
	 * @return the corresponding ChordStruct enum
	 */
	public ChordStructs getChordValue() {
		if (Math.random() < IKConstants.CHORDPICKER_ALT_STRUCT_CHANCE) {
			return altStruct;
		}
		return struct;
	}
	
	/**
	 * Function to get the root note of the chord
	 * @return the integer value of the root of the chord
	 */
	public int getRoot() {
		return root;
	}
}