package ikovsky;

import java.util.Random;

/**
 * @author Paul Hudson
 * ChordPicker Class
 * Used for deciding the next chord based in the chord sequence
 */
public class ChordPicker {
	public ChordStructs[] chords; //Array of available ChordStructs
	private double[] chordProbabilities; //Array of their corresponding probabolities

	private int[] currentChord; //Current chord intervals
	public int currentChordPosition; //Current position in the scale (1 - 7)
	private int	currentChordRoot; //root note of the current chord
	
	private int key;
	private boolean isMinorKey;

	Random rand;
	
	/**
	 * ChordPicker constructor
	 * @param newKey The current key of the song
	 * @param keyIsMinor The tonality of the current key
	 */
	public ChordPicker(int newKey, boolean keyIsMinor) {
		chords = new ChordStructs[7]; //7 Chords within scale
		chordProbabilities 	= new double[7]; 
		currentChordPosition = 1; //Initialise to root/"home" chord
		
		key = newKey;
		isMinorKey = keyIsMinor;
		
		rand = new Random();
		
		populateArrays();
	}
	

	/**
	 * Function for setting a new key
	 * Useful for key modulation, however this is not implemented
	 * @param newKey The new key integer value
	 * @param keyIsMinor The tonality of the new key
	 */
	public void setNewKey(int newKey, boolean keyIsMinor) {
		key = newKey;
		isMinorKey = keyIsMinor;
		
		populateArrays();
	}
	

	/**
	 * Get methods for the current key properties
	 * @return The integer value of the key
	 */
	public int getCurrentKey() {
		return key;
	}
	/**
	 * Get method for the current key tonality 
	 * @return True if the key is minor
	 */
	public boolean getCurrentKeyTonality() {
		return isMinorKey;
	}
	

	/**
	 * Function for setting new chord weightings
	 * @param newProbabilities The double array of new chord weighting values
	 */
	public void setProbabilities(double[] newProbabilities) {
		chordProbabilities = newProbabilities.clone();
	}
	/**
	 * Get method for the current chord weightings
	 * @return the double array of chord weightings
	 */
	public double[] getProbabilities() {
		return chordProbabilities;
	}
	
	/**
	 * Function to return the root chord of the progression
	 * @return int array of note values for the root chord
	 */
	public int[] getRootChord() {
		int[] rootChordArr = new int[3]; //Root chord is always a triad containing 3 notes
		int[] rootChordStruct = new int[3];
		
		//Get the chord from Major/Minor progressions
		if (!isMinorKey) {
			rootChordStruct = MajorChordProgression.I.getChordStruct().clone();
		} else {
			rootChordStruct = MajorChordProgression.I.getChordStruct().clone();
		}
		
		//Offset the chord intervals by the key
		for (int i = 0 ; i < rootChordStruct.length; i++) {
			rootChordArr[i] = rootChordStruct[i] + key;
		}
		
		return rootChordArr;
	}
	
	/**
	 * Function for deciding which chord index to use next
	 * @return the index of the new chord
	 */
	public int getNewChordIndex() {
		int index = 0;
		double p = rand.nextDouble(); //Random number generator for choosing chord index
		double pTotal = 0; //Accumlative total of probabilities
		
		//Use roulette selection to find the next chord
		for (int i = 0 ; i < chordProbabilities.length ; i++) {
			if (p < (pTotal + chordProbabilities[i])) {
				index = i;
				break;
			} else {
				pTotal += chordProbabilities[i];
			}
		}		
		
		return index;
	}
	
	/**
	 * Function to get the next chord in the progression
	 * @return int array of chord intervals offset by the current key
	 */
	public int[] getNewChord() {
		int[] newChordArr;
		int[] newChordStruct;
		int	newChordRoot = 0;

		int newChordPosition = getNewChordIndex() + 1; //Get the new chord index

		//Get the chord from the progression based on the key's tonality (Major/Minor)
		switch (newChordPosition) {
			case 1:
				if (!isMinorKey) {
					newChordStruct = MajorChordProgression.I.getChordValue().getChordIntervals();
					newChordRoot = MajorChordProgression.I.getRoot();
				} else {
					newChordStruct = MinorChordProgression.I.getChordValue().getChordIntervals();
					newChordRoot = MinorChordProgression.I.getRoot();
				}
				break;
			case 2:
				if (!isMinorKey) {
					newChordStruct = MajorChordProgression.II.getChordValue().getChordIntervals();
					newChordRoot = MajorChordProgression.II.getRoot();
				} else {
					newChordStruct = MinorChordProgression.II.getChordValue().getChordIntervals();
					newChordRoot = MinorChordProgression.II.getRoot();
				}
				break;
			case 3:
				if (!isMinorKey) {
					newChordStruct = MajorChordProgression.III.getChordValue().getChordIntervals();
					newChordRoot = MajorChordProgression.III.getRoot();
				} else {
					newChordStruct = MinorChordProgression.III.getChordValue().getChordIntervals();
					newChordRoot = MinorChordProgression.III.getRoot();
				}
				break;
			case 4:
				if (!isMinorKey) {
					newChordStruct = MajorChordProgression.IV.getChordValue().getChordIntervals();
					newChordRoot = MajorChordProgression.IV.getRoot();
				} else {
					newChordStruct = MinorChordProgression.IV.getChordValue().getChordIntervals();
					newChordRoot = MinorChordProgression.IV.getRoot();
				}
				break;
			case 5:
				if (!isMinorKey) {
					newChordStruct = MajorChordProgression.V.getChordValue().getChordIntervals();
					newChordRoot = MajorChordProgression.V.getRoot();
				} else {
					newChordStruct = MinorChordProgression.V.getChordValue().getChordIntervals();
					newChordRoot = MinorChordProgression.V.getRoot();
				}
				break;
			case 6:
				if (!isMinorKey) {
					newChordStruct = MajorChordProgression.VI.getChordValue().getChordIntervals();
					newChordRoot = MajorChordProgression.VI.getRoot();
				} else {
					newChordStruct = MinorChordProgression.VI.getChordValue().getChordIntervals();
					newChordRoot = MinorChordProgression.VI.getRoot();
				}
				break;
			case 7:
				if (!isMinorKey) {
					newChordStruct = MajorChordProgression.VII.getChordValue().getChordIntervals();
					newChordRoot = MajorChordProgression.VII.getRoot();
				} else {
					newChordStruct = MinorChordProgression.VII.getChordValue().getChordIntervals();
					newChordRoot = MinorChordProgression.VII.getRoot();
				}
				break;
			default:
				newChordStruct = ChordStructs.FIFTH.getChordIntervals(); //Default to a Fifth chord (Neither Major or Minor)
				break;
		}
		
		//Update current chord attributes
		currentChordRoot = newChordRoot;
		currentChord = newChordStruct;
		currentChordPosition = newChordPosition;

		newChordArr = currentChord; //Set the array
		
		//Offset the intervals by the current key
		for (int i = 0 ; i < newChordArr.length; i++) {
			newChordArr[i] += (currentChordRoot + key);
		}

		return newChordArr;
	}
	
	/**
	 * Function to populate the arrays with the correct enum values from the ChordProgression enums
	 */
	private void populateArrays() {
		int itr = 0;
		//IF MAJOR_KEY -> Add Major chord progression chords
		if (!isMinorKey) {
			for (MajorChordProgression c : MajorChordProgression.values()) {
				chords[itr] = c.getChordValue();
				chordProbabilities[itr] = 0;
				
				itr++;
			}
		} 
		//ELSE -> Add Minor chord progression chords
		else {
			for (MinorChordProgression c : MinorChordProgression.values()) {
				chords[itr] = c.getChordValue();
				chordProbabilities[itr] = 0;
				
				itr++;
			}
		}
	}
}
