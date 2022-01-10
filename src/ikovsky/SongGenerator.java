/*
= * SongGenerator class handles setting up the initial parameters for the song
 * This class also handles the output of the midi file
 */

package ikovsky;

import java.util.ArrayList;
import java.util.Random;

import jm.JMC;
import jm.gui.show.ShowScore;
import jm.music.data.Score;
import jm.util.Play;
import jm.util.Write;

/**
 * @author Paul Hudson
 * The Song Generator class is the top level class for handling music generation
 * including parsing of the input parameters, and output of the finished .midi file
 */
public class SongGenerator implements JMC {
	private boolean useDefaults;				//boolean field to denote debug mode
	
	private Score songScore;					//JMusic class for the score of the whole song (sum of all parts)
	
	private SongStructure songStructure;		//Class for storing all data to do with structure of song (key/tempo/verses etc)
	
	private String saveDir;						//File directory for saving
	
	public boolean correctParams;				//Boolean value to assert that parameters have been parsed succesfully
	private ArrayList<String> parameterErrors;	//ArrayList to contain all potential errors to display to the user
	
	private Random rand;						//Random Number Generator

	private String saveLocation;
	private String key;
	private Integer tempo;
	private String timeSig;
	private Integer octaveLow;
	private Integer octaveHigh;
	private Integer dynamicsLow;
	private Integer dynamicsHigh;
	private Integer noteDensity;
	private Integer instrument;

	public SongGenerator(boolean debug, String saveLocation, String key, Integer tempo, String timeSig, Integer octaveLow, Integer octaveHigh, Integer dynamicsLow, Integer dynamicsHigh, Integer noteDensity, Integer instrument) {
		//	1 - Save Location
		//	2 - Key
		//	3 - Tempo
		//	4 - Time Sig
		//	5 - Octave low
		//	6 - Octave high
		//	7 - 13 - Chord weightings
		//	14 - Dynamics low
		//	15 - Dynamics High
		//	16 - Note Densitygrad
		//	17 - Instrument

		useDefaults = debug;		
		
		songScore	= new Score("New Song"); //Song Title
		songStructure = new SongStructure();
		
		this.saveDir = saveLocation;
		this.key = key;
		//300 max
		this.tempo = tempo;
		this.timeSig = timeSig;
		// 10 max, low < high
		this.octaveLow = octaveLow;
		this.octaveHigh = octaveHigh;
		//100 max, low < high
		this.dynamicsLow = dynamicsLow;
		this.dynamicsHigh = dynamicsHigh;
		//TODO: this.chordWeightintgs
		//100 max
		this.noteDensity = noteDensity;
		//126 max
		this.instrument = instrument;

		parseSongParameters();
		
		correctParams = false;
		parameterErrors = new ArrayList<>();
		
		rand = new Random();
	}

	/**
	 * Function to set desired musical properties in songStructure
	 */
	private void parseSongParameters() {
		//Parse individual parameters, ensure that they are correct
		setTimeSig(this.timeSig);
		setSongKey(this.key);
		songStructure.tempo = tempo;
		songStructure.maxOctave = octaveHigh;
		songStructure.minOctave = octaveLow;
		songStructure.maxDynamic = dynamicsHigh;
		songStructure.minDynamic = dynamicsLow;
		songStructure.noteDensity = noteDensity;
		songStructure.instrument = instrument;

		//Roll between min and max default values for duration and numVerses
		songStructure.duration = rand.nextInt(IKConstants.SONG_DURATION_UPPER - IKConstants.SONG_DURATION_LOWER) + IKConstants.SONG_DURATION_LOWER;
		songStructure.numVerses = rand.nextInt(IKConstants.SONG_VERSE_CHORUS_UPPER - IKConstants.SONG_VERSE_CHORUS_LOWER) + IKConstants.SONG_DURATION_UPPER;

		songStructure.rhythmChance = IKConstants.DEFAULT_RHYTHM_CHANCE; //set to default

		//Use roulette selection for deciding the number of different parts to the song, between a solo part, and a quartet
		double partsP = rand.nextDouble();
		if (partsP < IKConstants.DEFAULT_NUM_PARTS_1_PROBABILITY) {
			songStructure.numParts = 1;
		} else if (partsP < (IKConstants.DEFAULT_NUM_PARTS_1_PROBABILITY + IKConstants.DEFAULT_NUM_PARTS_2_PROBABILITY)) {
			songStructure.numParts = 2;
		} else if (partsP < (IKConstants.DEFAULT_NUM_PARTS_1_PROBABILITY + IKConstants.DEFAULT_NUM_PARTS_2_PROBABILITY + IKConstants.DEFAULT_NUM_PARTS_3_PROBABILITY)) {
			songStructure.numParts = 3;
		} else {
			songStructure.numParts = 4;
		}

		songStructure.setNoteDurations(IKConstants.DEFAULT_DURATIONS); //Use default selection of note durations
		songStructure.calculateVerseStructure();
		songStructure.printStructureDetails(); //debug output
	}
	
	/**
	 * Function for parsing the key for the song
	 * Parameter should be of format Key + Sharp? + Minor? e.g. CSM = C# Minor, C = C Major
	 * @return True if correct, false if parameter is incorrect
	 */
	private boolean setSongKey(String keyArg) {
		boolean success = false;
		boolean isSharp = false;
		boolean isMinor = false;
		
 		int maxKeyArgLength = 3; //longest string = e.g. DSM = 3 chars in length
		
 		//Check the string isn't empty and within the max length
		if (!keyArg.isEmpty() && keyArg.length() <= maxKeyArgLength) {
			//when length is 2, work out if the key is either a Minor or a Sharp
			if (keyArg.length() == 2) {
				switch (keyArg.toUpperCase().charAt(1)) {
				case 'S': //Sharp
					if (keyArg.toUpperCase().charAt(0) != 'B' && keyArg.toUpperCase().charAt(0) != 'E') {
						isSharp = true;
						success = true;
					} else {
						success = false;
					}
					break;
				case 'M': //Minor
					isMinor = true;
					success = true;
					break;
				default:
					success = false;
					break;
				}
			}
			//When length is 3, the key should be both a Sharp and a Minor
			if (keyArg.length() == 3) {
				//Assert that the 2nd character denotes a sharp
				//		 that the 3rd character denotes a minor key
				//		 that the 1st character is not B or E (no key such as B# or E#)
				if (	keyArg.toUpperCase().charAt(1) == 'S' && //Sharp
						keyArg.toUpperCase().charAt(2) == 'M' && //Minor
						keyArg.toUpperCase().charAt(0) != 'B' && //Not B or E
						keyArg.toUpperCase().charAt(0) != 'E') {
					isSharp = true;
					isMinor = true;
					success = true;
				} else {
					success = false;
				}
			}
			//Set the key from the first character
			if (success || keyArg.length() == 1) {
				success = true;
				switch (keyArg.toUpperCase().charAt(0)) {
				case 'A':
					songStructure.key = ScaleNotes.A.getNoteVal();
					break;
				case 'B':
					songStructure.key = ScaleNotes.B.getNoteVal();
					break;
				case 'C':
					songStructure.key = ScaleNotes.C.getNoteVal();
					break;
				case 'D':
					songStructure.key = ScaleNotes.D.getNoteVal();
					break;
				case 'E':
					songStructure.key = ScaleNotes.E.getNoteVal();
					break;
				case 'F':
					songStructure.key = ScaleNotes.F.getNoteVal();
					break;
				case 'G':
					songStructure.key = ScaleNotes.G.getNoteVal();
					break;
				default:
					success = false;
					break;
				}
				
				if (isSharp) {
					songStructure.key++; //Increment by 1 for the relative sharp key
				}
				
				if (isMinor) {
					songStructure.isMinorKey = true;
				}
				
			} else {
				parameterErrors.add("Error parsing key [key should be in format Key+Tonality e.g. C, Cm, CSm]"); //Add error message
			}
		}
		return success;
	}

	/**
	 * Function for parsing the Time Signature arg
	 * Should be of format (X/Y) where X and Y are integers
	 * @return False if parameters are incorrect
	 */
	private boolean setTimeSig(String timeSigArg) {
		//Check the string isn't empty, is greater than the minimum length (X/X = 3 chars in length)
		//Also assert that the two integers are split with a "/" character and the numbers entered are not float values
		if (	!timeSigArg.isEmpty() && timeSigArg.length() >= 3 && 
				timeSigArg.matches("[0-9]+/[0-9]+") && timeSigArg.contains("/") &&
				!timeSigArg.endsWith("/") && !timeSigArg.startsWith("/") &&
				!timeSigArg.contains(".")) {
			String[] timeSigStr = timeSigArg.split("/"); //Get the two integers
			
			songStructure.timeSigBeatsPerBar = Integer.parseInt(timeSigStr[0]);
			
			//Set the relative beat value for the time signature
			int timeSigNoteValue = Integer.parseInt(timeSigStr[1]);
			
			switch (timeSigNoteValue) {
			case 1:
				songStructure.timeSigNoteValue = jm.constants.Durations.WHOLE_NOTE;
				break;
			case 2:
				songStructure.timeSigNoteValue = jm.constants.Durations.HALF_NOTE;
				break;
			case 4:
				songStructure.timeSigNoteValue = jm.constants.Durations.QUARTER_NOTE;
				break;
			case 8:
				songStructure.timeSigNoteValue = jm.constants.Durations.EIGHTH_NOTE;
				break;
			case 16:
				songStructure.timeSigNoteValue = jm.constants.Durations.SIXTEENTH_NOTE;
				break;
			default:
				//Default to standard quarter note
				songStructure.timeSigNoteValue = jm.constants.Durations.QUARTER_NOTE;
				break;
			}
			return true;
		}
		
		parameterErrors.add("Unable to parse time signature, ensure the value is a string of format [4/4] or [3/8] etc."); //Add error message
		return false;
	}

	/**
	 * Function for setting the 7 chord weightings
	 * Each weighting Arg should be a double or 0 value less than 5 characters in length
	 * Regex for parsing double taken from: http://stackoverflow.com/questions/6754552/regex-to-find-a-float-probably-a-really-simple-question
	 * @return False if parameter is invalid
	 */
	private boolean setChordWeightings() {
		boolean success = true;
		int chordIndex = 0;
		
		//Loop through all args
		for (int i = IKConstants.CHORD_WEIGHTINGS_PARAM_INDEX ; i < (IKConstants.CHORD_WEIGHTINGS_PARAM_INDEX + IKConstants.NUM_SCALE_CHORDS); i++) {
			String chordWeightingArg = songParameters[i];
			
			//Check the string is nonempty and within correct bounds
			if (!chordWeightingArg.isEmpty() && chordWeightingArg.length() <= 4 && chordWeightingArg.matches("^([+-]?\\d*\\.?\\d*)$")) {
				songStructure.chordWeightings[chordIndex] = Double.parseDouble(chordWeightingArg);
				
				//Weightings should be within the range of 0 - 1
				if (songStructure.chordWeightings[chordIndex] > 1) {
					songStructure.chordWeightings[chordIndex] = 1;
				}				
			} else {
				success = false;
			}
			
			chordIndex++;
		}
		
		if (!success) {
			parameterErrors.add("Error setting chord weightings"); //Add error message
		}
		
		return success;
	} 

	/**
	 * Function for writing the new song
	 */
	public void createSongComplex() {
		if (correctParams) {
			Composer newComp = new Composer(songStructure); //Create the composer

			//Create all the required parts
			for (int i = 0 ; i < songStructure.numParts; i++) {
				boolean isRhythmPart = false;
				int partOctave = rand.nextInt(songStructure.maxOctave - songStructure.minOctave) + songStructure.minOctave + 1; //Randomise the octave within limits
				
				//Decide rhythm part
				if (rand.nextDouble() < songStructure.rhythmChance) {
					isRhythmPart = true;
				}
				
				newComp.addPart(isRhythmPart, songStructure.instrument, partOctave, songStructure.minDynamic, songStructure.maxDynamic);
			}
			
			newComp.writeSong(songScore); //Write the song to the score
			
			saveSong(); //Save the midi file
		}
	}
	
	/**
	 * Function for saving the song to the input save location
	 */
	public void saveSong() {
		//Build the file location string
		String fileLoc = saveDir;
		fileLoc += "/midioutput.mid";
		
		//Write the file
		Write.midi(songScore, fileLoc);
		System.out.println(fileLoc);
		System.out.println("MIDI file saved");

		//For debugging / development --> play the song locally
		if (useDefaults) {
			new ShowScore(songScore);
			Play.midi(songScore);
		}
	}
}
