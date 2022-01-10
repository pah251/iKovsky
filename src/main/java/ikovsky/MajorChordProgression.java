package ikovsky;

/**
 * @author Paul Hudson
 * MajorChordProgression Enum
 * Holds all the different chords, as well as an alternative structure for chords
 * that follow the Major Chord Progression 
 */
public enum MajorChordProgression {
	//ScaleChords defaults to a Major chord progression (I, ii, iii, IV, V, vi, vii(dim))
	I	(ChordStructs.MAJOR_TRIAD,	0, ChordStructs.FIFTH),
	II	(ChordStructs.MINOR_TRIAD,	2, ChordStructs.MINOR_SEVENTH),
	III	(ChordStructs.MINOR_TRIAD,	4, ChordStructs.MINOR_SEVENTH),
	IV	(ChordStructs.MAJOR_TRIAD,	5, ChordStructs.MAJOR_SEVENTH),
	V	(ChordStructs.MAJOR_TRIAD,	7, ChordStructs.MAJOR_SEVENTH),
	VI	(ChordStructs.MINOR_TRIAD,	9, ChordStructs.MINOR_SEVENTH),
	VII	(ChordStructs.FIFTH,		11, ChordStructs.DIMINISHED);
	
	private ChordStructs struct;
	private ChordStructs altStruct;
	
	private int root;
	
	/**
	 * Constructor for a MajorChordProgression enum
	 * @param struct The associated ChordStruct enum
	 * @param root The integer value of the root of the chord within the current key
	 * @param altStruct The alternative ChordStruct enum
	 */
	private MajorChordProgression (ChordStructs struct, int root, ChordStructs altStruct) {
		this.struct = struct;
		this.altStruct = altStruct;
		
		this.root = root;
	}
	
	/**
	 * Function to get the chord intervals
	 * @return The integer array of the chord intervals
	 */
	public int[] getChordStruct() {
		if (Math.random() < IKConstants.CHORDPICKER_ALT_STRUCT_CHANCE) {
			return altStruct.getChordIntervals();
		}
		return struct.getChordIntervals();
	}

	/**
	 * Function to get the chord structure
	 * @return The corresponding ChordStruct enum
	 */
	public ChordStructs getChordValue() {
		if (Math.random() < IKConstants.CHORDPICKER_ALT_STRUCT_CHANCE) {
			return altStruct;
		}
		return struct;
	}
	
	/**
	 * Function to get the root note of the chord
	 * @return The integer value of the root of the chord
	 */
	public int getRoot() {
		return root;
	}
}
