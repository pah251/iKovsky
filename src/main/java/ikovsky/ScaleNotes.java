package ikovsky;

/**
 * @author Paul Hudson
 * The ScaleNotes enum contains all the notes within the chromatic scale,
 * as well as their integer representation value
 */
public enum ScaleNotes {
	C(0),
	CS(1),
	D(2),
	DS(3),
	E(4),
	F(5),
	FS(6),
	G(7),
	GS(8),
	A(9),
	AS(10),
	B(11);
	
	private int noteVal;
	
	/**
	 * Constructor for a ScaleNotes enum
	 * @param noteVal The int representation of the note
	 */
	ScaleNotes(int noteVal) {
		this.noteVal = noteVal;
	}
	
	/**
	 * Method for getting the int representation of the note
	 * @return The int value of the note
	 */
	public int getNoteVal() {
		return noteVal;
	}
	
}
