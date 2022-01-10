package ikovsky;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Paul Hudson
 * The SongStructure class holds all information about the song's musical properties
 */
public class SongStructure {
	public int key;
	public boolean isMinorKey;
	
	public int	tempo;
	
	public double timeSigNoteValue;
	public int	timeSigBeatsPerBar;
	
	public int	duration;  //Number of verses/choruses (e.g. ABACA = 5)
	public int	numVerses; //Number of different verses (e.g. ABACA = 3)
	
	public int	barsPerVerse; //Number of bars in a verse -> defaults to time sig multiplication
	
	public ArrayList<Integer> verseStructure; //Integer representation of verse/chorus structure (e.g. ABACA = {0,1,0,2,0})
	
	public int	instrument; //Instrument for the track
	
	public int	numParts; //Amount of parts to the track
	
	public double rhythmChance; //Chance of a part being a rhythm part
	
	//Dynamic range of a note
	public int	minDynamic;
	public int	maxDynamic;
	
	public double durations[]; //Different available durations
	
	//Min and Max octaves
	public int	minOctave;
	public int	maxOctave;	
	
	public double chordWeightings[]; //Weightings for different chords in progression (I, ii, ..., vii(o))
	
	public int	noteDensity; //Min and Max note densities
	
	/**
	 * Constructor for SongStructure class 
	 */
	public SongStructure() {
		key 				= 0;
		isMinorKey 		= false;
		
		tempo				= 0;
		
		timeSigNoteValue 	= 0;
		timeSigBeatsPerBar 	= 0;
		
		duration			= 0;
		numVerses			= 0;
		barsPerVerse 		= 0;
		
		instrument			= 0;
		
		numParts			= 0;
		
		rhythmChance		= 0;
		
		minDynamic			= 0;
		maxDynamic			= 0;
		
		minOctave			= 0;
		maxOctave			= 0;
		
		chordWeightings 	= new double[7];
		
		noteDensity		= 0;
		
		verseStructure = new ArrayList<>();
	}
	
	/**
	 * Function to set available durations of different notes
	 * @param dur The double array of note durations
	 */
	public void setNoteDurations(double[] dur) {
		durations = dur;
	}
	
	/**
	 * Function for randomly generating verse chorus structure
	 */
	public void calculateVerseStructure() {
		Random rand = new Random();
		for (int i = 0 ; i < duration; i++) {
			verseStructure.add(rand.nextInt(numVerses));
		}
	}
	
	/**
	 * Function to provide debug output, logging the musical properties of the song structure to the console=
	 */
	public void printStructureDetails() {
		System.out.println("---------- Song Structure ----------");
		System.out.println("Key: " + key + " isMinor: " + isMinorKey);
		System.out.println("Tempo: " + tempo);
		System.out.println("Duration: " + duration);
		System.out.println("Num Parts: " + numParts);
		System.out.println("Time Sig: " + timeSigBeatsPerBar + "/" + timeSigNoteValue);
		System.out.println("------------------------------------");
	}
}
