package ikovsky;

import jm.music.data.CPhrase;

/**
 * @author Paul Hudson
 * The Measure class is a data structure to hold the note/chord sequence
 * and the number of iterations for a single measure
 */
public class Measure {
	CPhrase phrase;	
	int		iterations;
	
	/**
	 * Constructor for Measure class
	 * @param newPhrase The musical phrase that will be repeated
	 * @param iter The integer value representing how many iterations of the corresponding phrase
	 */
	public Measure(CPhrase newPhrase, int iter) {
		phrase = newPhrase;		
		iterations = iter;
	}
	
	/**
	 * Blank constructor for Measure class
	 */
	public Measure() {
		
	}
}
