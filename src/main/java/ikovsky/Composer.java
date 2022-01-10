package ikovsky;

import jm.music.data.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Paul Hudson
 * Composer Class
 * The composer class handles the creation of all different parts to the song, and stitches them together
 */
public class Composer {
	private SongStructure songStruct; //System wide songstructure
	private ArrayList<PartCreator> songParts; //The array of different instrument parts (e.g. Piano melody, Piano rhythm, drums)
	private ArrayList<Part> songTracks; //ArrayList for storing each of the song tracks (e.g. rythm track / treble track)
	
	private ChordPicker	chordPicker; //Class for deciding chords
	
	private Random rand;
	
	private ArrayList<Measure[]> verses; //ArrayList for all verses

	/**
	 * Default constructor for Composer class
	 * @param newSongStruct The SongStructure object containing the song's musical properties
	 */
	public Composer(SongStructure newSongStruct) {
		songParts = new ArrayList<PartCreator>();
		songTracks = new ArrayList<>();
		songStruct = newSongStruct;
		
		chordPicker = new ChordPicker(songStruct.key, songStruct.isMinorKey);
		chordPicker.setProbabilities(songStruct.chordWeightings);
		
		rand = new Random();
		
		verses = new ArrayList<>();
	}
	

	/**
	 * Function for adding part to the composition
	 * @param isRhythm True if the part is a Rhythm part, this means the part will choose to play chords rather than single notes
	 * @param instrument Integer value of the midi instrument
	 * @param octave Initial octave of the part
	 * @param dMin Minimum dynamic that a note/chord can be played
	 * @param dMax Maximum dynamic that a note/chord can be played
	 */
	public void addPart(boolean isRhythm, int instrument, int octave, int dMin, int dMax) {
		songParts.add(new PartCreator(isRhythm, songStruct, instrument, octave, dMin, dMax)); //Add new part creator object
		
		//Add the musical part that will be added to the score
		Part newPart = new Part(); 
		newPart.setInstrument(instrument);
		newPart.setTempo(songStruct.tempo);	
		songTracks.add(newPart);
	}
	
	/**
	 * Get function to get access to a single part
	 * @param index The integer index of the part within the songTracks array
	 * @return The part at the index from the songTracks array
	 */
	public Part getPart(int index) {
		return songTracks.get(index);
	}

	/**
	 * Setter function for song structure
	 * @param newStruct The new SongStructure object containing the new musical properties
	 */
	public void setSongStruct(SongStructure newStruct) {
		songStruct = newStruct;
		chordPicker.setProbabilities(songStruct.chordWeightings); //Set chord weightings
	}
	
	/**
	 * Getter function to access the private SongStructure
	 * @return The current SongStructure object
	 */
	public SongStructure getSongStruct() {
		return songStruct;
	}

	/**
	 * Function to write the song
	 * Writes all different verses first for all individual parts,
	 * Then stitches them all together as different tracks which are added to the Score
	 * @param songScore The Score object to be written to
	 */
	public void writeSong(Score songScore) {
		writeVerses(); //Verses
		writeTracks(); //Tracks
		
		//Write to score
		for (int i = 0 ; i < songTracks.size(); i++) {
			songScore.add(songTracks.get(i));
		}
	}
	
	/**
	 * Function to stitch all written verses together in accordance with verse/chorus structure for each song part
	 */
	private void writeTracks() {
		for (int i = 0 ; i < songStruct.verseStructure.size(); i++) {
			for (int j = 0 ; j < songTracks.size(); j++) {
				for (int k = 0 ; k < verses.get(songStruct.verseStructure.get(i))[j].iterations; k++) {
					songTracks.get(j).addCPhrase(verses.get(songStruct.verseStructure.get(i))[j].phrase.copy());
				}
			}
		}
	}
	
	/**
	 * Function for writing all verses
	 */
	public void writeVerses() {
		//Write all verses
		for (int i = 0 ; i < songStruct.numVerses; i++) {
			writeVerse();
		}
	}
	
	/**
	 * Method for writing all different verses (e.g. in ABACA it will write the A, B and C verses)
	 * For all instruments:
	 * 1. choose verse structure (number of iterations of measures)
	 * 2. choose current chord
	 * 3. add notes based off chord and key
	 */
	public void writeVerse() {		
		int numBars = 0;		
		int numMeasures = 0;
		
		//Use roulette selection to determine number of measures and bars
		double numMeasuresP = rand.nextDouble();
		
		if (numMeasuresP < IKConstants.COMPOSER_MEASURES_1_BARS_4_PROBABILITY) {
			numMeasures = 1;
			numBars = 4;
		} else if (numMeasuresP < IKConstants.COMPOSER_MEASURES_1_BARS_4_PROBABILITY + IKConstants.COMPOSER_MEASURES_2_BARS_2_PROBABILITY) {
			numMeasures = 2;
			numBars = 2;
		} else if (numMeasuresP < IKConstants.COMPOSER_MEASURES_1_BARS_4_PROBABILITY + 
				IKConstants.COMPOSER_MEASURES_2_BARS_2_PROBABILITY + IKConstants.COMPOSER_MEASURES_2_BARS_1_PROBABILITY) {
			numMeasures = 2;
			numBars = 1;
		} else {
			numMeasures = 1;
			numBars = 8;
		}
		
		Measure[] newVerse = new Measure[songTracks.size()]; //create the new verse
				
		for (int i = 0; i < newVerse.length; i++) {
			newVerse[i] = new Measure();
			newVerse[i].iterations = numMeasures;
			newVerse[i].phrase = new CPhrase();
			newVerse[i].phrase.setTempo(songStruct.tempo);
		}
	
		int[] newChord = chordPicker.getNewChord(); //Choose new chord
		
		//Write all bars and measures
		for (int j = 0 ; j < numBars; j++) {
			ChordStructs newChordStruct = chordPicker.chords[chordPicker.currentChordPosition-1];
			for (int k = 0; k < newVerse.length; k++) {
				songParts.get(k).writeBar(newChord, newChordStruct, newVerse[k].phrase);
			}
			newChord = chordPicker.getNewChord(); //Choose next chord in progression
		}
		
		verses.add(newVerse); //Add the verse
	}
}
