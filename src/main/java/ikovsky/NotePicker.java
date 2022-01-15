package ikovsky;

import java.util.Random;

/**
 * @author Paul Hudson
 * NotePicker class handles picking the next note to be used in the melody
 * based upon the current underlying chord and scale patterns
 */
public class NotePicker {
	public ScaleNotes[] notes; //The notes within the current scale
	public double[] noteProbabilities; //Weightings of different notes
	public double baseNoteProbability; //Initial values set to weightings
	
	public int	currentNote;
	public Scales currentScale;
	public Scales currentPentatonic; //Pentatonic version of the current scale
	public ChordStructs currentChord; //Current underlying chord
	
	public int key;
	Random rand;
	
	/**
	 * Constructor for NotePicker class
	 */
	public NotePicker() {
		notes = new ScaleNotes[IKConstants.NUM_CHROMATIC_NOTES];
		noteProbabilities = new double[IKConstants.NUM_CHROMATIC_NOTES];
		
		populateArrays();
		
		rand = new Random();
	}
	
	/**
	 * Function to get the next note in the melody
	 * @return the integer value of the note within the scale
	 */
	public int getNewNote() {
		int newNote = currentNote;
		setProbabilities();

		//Roulette selection of different notes
		//Construct Roulette
		double rouletteP[] = new double[IKConstants.NUM_CHROMATIC_NOTES];
		double currentP = 0.0; //Accumulative total of probabilities
		

		for (int i = 0 ; i < noteProbabilities.length ; i++) {
			currentP += noteProbabilities[i];
			rouletteP[i] = currentP;
		}

		//Roll the RNG for the next index
		double newDouble = rand.nextDouble();
		int newNoteIndex = 0;
		
		//Find the next note index
		for (int i = 0 ; i < rouletteP.length ; i++) {
			if (i > 0) {
				if (newDouble > rouletteP[i-1] && newDouble < rouletteP[i]) {
					newNoteIndex = i;
				}
			} else {
				if (newDouble < rouletteP[i]) {
					newNoteIndex = i;
				}
			}
		}
	
		newNote = notes[newNoteIndex].getNoteVal(); //Get the integer value
		currentNote = newNote; //Set the current note to the new value
		
		return newNote;
	}
	
	/**
	 * Function for reseting and rebuilding the probabilities for every note
	 * Weightings for notes are layered up, giving all notes within the current scale a baseline value
	 * Upon that value the notes from the relative Pentatonic scale are added to
	 * Furthermore, the current underlying chord of the song's notes are then added to as well
	 */
	public void setProbabilities() {
		resetProbabilities();
		//Set current scale probabilities (Natural Major / Minor scales)
		int[] scaleIntervals = currentScale.getScaleValues();
		
		for (int i = 0 ; i < scaleIntervals.length ; i++) {
			int index = key + scaleIntervals[i];
			
			//Wrap around for octaves
			if (index >= IKConstants.NUM_CHROMATIC_NOTES) {
				index-=IKConstants.NUM_CHROMATIC_NOTES;
			}
			
			noteProbabilities[index] = IKConstants.NOTEPICKER_CURRENT_SCALE_PROBABILITY; //Set to base probability
		}
		
		//Set Pentatonic probabilities
		scaleIntervals = currentPentatonic.getScaleValues();
		
		for (int i = 0 ; i < scaleIntervals.length ; i++) {
			int index = key + scaleIntervals[i];
			
			//Wrap around for octaves
			if (index >= IKConstants.NUM_CHROMATIC_NOTES) {
				index-=IKConstants.NUM_CHROMATIC_NOTES;
			}
			
			noteProbabilities[index] += IKConstants.NOTEPICKER_CURRENT_PENTATONIC_SCALE_PROBABILITY; //Add Pentatonic Modifier
		}
		
		//Set Chord probabilities		
		scaleIntervals = currentChord.getChordIntervals();
		
		for (int i = 0 ; i < scaleIntervals.length ; i++) {
			int index = key + scaleIntervals[i];
			
			//Wrap around for octaves
			if (index >= IKConstants.NUM_CHROMATIC_NOTES) {
				index-=IKConstants.NUM_CHROMATIC_NOTES;
			}
			
			noteProbabilities[index] += IKConstants.NOTEPICKER_CURRENT_CHORD_PROBABILITY; //Add chord modifier
		}
	}
	
	/**
	 * Function to set the relative scales, key and octaves
	 * @param newNaturalScale The current natural major or minor scale
	 * @param newPentatonicScale The corresponding pentatonic scale
	 * @param newProbability Default probability for each note within the natural scale
	 * @param newKey The int value of the current key 
	 * @param newOctave The current octave
	 */
	public void setScaleBaseline(Scales newNaturalScale, Scales newPentatonicScale, double newProbability, int newKey, int newOctave) {
		currentScale = newNaturalScale;
		currentPentatonic = newPentatonicScale;
		
		baseNoteProbability = newProbability;
		
		key = newKey;
	}
	
	/**
	 * Function to set current underling chord
	 * @param c The ChordStructs enum of the chord
	 */
	public void setChord(ChordStructs c) {
		currentChord = c;
	}
	
	/**
	 * Function to reset all note probabilities to the minimum base probability for the chromatic scale
	 */
	private void resetProbabilities() {
		for (int i = 0 ; i < noteProbabilities.length; i++) {
			noteProbabilities[i] = IKConstants.NOTEPICKER_BASE_CHROMATIC_PROBABILITY;
		}
	}
	
	/**
	 * Function to populate the Scale array
	 */
	private void populateArrays() {
		int itr = 0;
		
		for (ScaleNotes s : ScaleNotes.values()) {
			notes[itr] = s;
			noteProbabilities[itr] = 0; //Initialise to 0		
			
			itr++;
		}
	}
}
