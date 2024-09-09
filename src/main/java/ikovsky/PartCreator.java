package ikovsky;

import java.util.Random;

import jm.music.data.CPhrase;

/**
 * @author Paul Hudson
 * The PartCreator class handles the writing of a single bar based on the current chord and scale
 */
public class PartCreator {
	boolean isRhythmPart; //booleans to store state of part
	
	SongStructure songStruct; //System wide songstructure
	
	public NotePicker notePicker; //Class for deciding individual notes
	
	int octave; //Octave offset for the new note(s)
	
	Random rand;	
	
	/**
	 * Constructor for the PartCreator class
	 * @param rhythm True if the part is a rhythm part, meaning the part will choose to favour chords over individual notes
	 * @param newStruct The SongStructure object containing the musical properties of the song
	 * @param instrument The int value of the desired midi instrument
	 * @param rootOctave The initial octave the part should add notes from
	 * @param dynMin The minimum dynamic that notes/chords can be played at
	 * @param dynMax The maximum dynamic that notes/chords can be played at
	 */
	public PartCreator(boolean rhythm, SongStructure newStruct, int instrument, int rootOctave, int dynMin, int dynMax) {
		isRhythmPart = rhythm;
		
		octave = rootOctave;
		
		songStruct = newStruct;
		
		notePicker = new NotePicker();
		
		rand = new Random();
	}
	
	/**
	 * Function for writing a single bar of music
	 * @param barChord The current chord in the chord progression
	 * @param chordType The ChordStruct enum of the chord
	 * @param newBar The CPhrase object that the music shall be written to
	 */
	public void writeBar(int[] barChord, ChordStructs chordType, CPhrase newBar) {
		boolean isRest = false;
		double beatCount = 0;
		
		//While the total duration of the bar is less than the number of beats in a bar
		while (beatCount < songStruct.timeSigBeatsPerBar) {
			int[] newChord;
			isRest = false;
			
			//Check to add rest
			if (rand.nextDouble() < ((double)songStruct.noteDensity/100)) {
				//If rhythm part -> Add chords / notes from current chord
				if (isRhythmPart) {					
					//Roll to add whole chord
					if (rand.nextDouble() < IKConstants.PARTCREATOR_CHORD_3_NOTES_PROBABILITY) {
						newChord = barChord.clone();
					} 					
					//else -> add 2 notes from the chord
					else if (rand.nextDouble() < (IKConstants.PARTCREATOR_CHORD_3_NOTES_PROBABILITY + IKConstants.PARTCREATOR_CHORD_2_NOTES_PROBABILITY)) {
						newChord = new int[2]; //Chord contains only 2 notes				
						newChord[0] = barChord[rand.nextInt(2)];
						newChord[1] = barChord[rand.nextInt(2)];
					} 
					//else -> add a single note from the chord
					else {
						newChord = new int[1]; //new "Chord" contains only 1 note
						newChord[0] = barChord[rand.nextInt(2)];
					}
				}
				//if melody part -> Add new note notes / chords from current chord
				else {
					//Check to add a single note
					if (rand.nextDouble() < IKConstants.PARTCREATOR_MELODY_CHORD_PROBABILITY) {
						newChord = new int[1]; //new "Chord" contains only 1 note
						
						//Reinitialise NotePicker class to the current chord/key
						setNotePickerProperties();
						setCurrentChord(songStruct.key, chordType);
						
						newChord[0] = notePicker.getNewNote(); //get next note
					} else {
						//Add a chord of size 0 - 3 from the current chord
						int numNotes = rand.nextInt(2)+1;
						newChord = new int[numNotes];
						
						for (int i = 0 ; i < numNotes; i++) {
							newChord[i] = barChord[rand.nextInt(2)];
						}
					}
				}
				
				//Roll to change octave
				if (isRhythmPart) {
					if (rand.nextDouble() < IKConstants.NOTEPICKER_OCTAVE_DECREASE_PROBABILITY && octave > songStruct.minOctave) {
						octave--;
					} else if (rand.nextDouble() > IKConstants.NOTEPICKER_OCTAVE_INCREASE_PROBABILITY && octave < songStruct.maxOctave) {
						octave++;
					}
				} else {
					if (rand.nextDouble() < IKConstants.NOTEPICKER_OCTAVE_DECREASE_PROBABILITY && octave > songStruct.minOctave) {
						octave--;
					} else if (rand.nextDouble() > IKConstants.NOTEPICKER_OCTAVE_INCREASE_PROBABILITY && octave < songStruct.maxOctave) {
						octave++;
					}
				}
				
				//Add the octave offset to each note within the chord
				for (int i = 0 ; i < newChord.length; i++) {
					newChord[i] = newChord[i] + (octave * IKConstants.NUM_CHROMATIC_NOTES);
				}
			}
			//else add rest
			else {
				newChord = new int[1];
				newChord[0] = 0;
				isRest = true;
			}
			
			//Pick duration
			double chordDuration = 0;
			
			if (!isRest) {
				chordDuration = songStruct.durations[rand.nextInt(songStruct.durations.length)];
			} else {
				chordDuration = songStruct.timeSigNoteValue;
			}
			
			//Check the new chord/note duration is within the limit
			if ((beatCount + chordDuration) > songStruct.timeSigBeatsPerBar) {
				chordDuration = songStruct.timeSigBeatsPerBar - beatCount;
			}
			
			//Randomise dynamics
			int newDynamic = rand.nextInt(songStruct.maxDynamic - songStruct.minDynamic + 1) + songStruct.minDynamic;
			
			//Add to CPhrase
			if (isRest) {
				newBar.addChord(newChord, chordDuration, 0); //0 dynamic = silent note
			} else {
				newBar.addChord(newChord, chordDuration, newDynamic);
			}
			
			beatCount += chordDuration;
		}
	}
	
	/**
	 * Function to set the current properties of the NotePicker object
	 */
	private void setNotePickerProperties() {
		if (!songStruct.isMinorKey) {
			notePicker.setScaleBaseline(Scales.MAJOR, Scales.MAJOR_PENTATONIC, 0, songStruct.key, octave);
		} else {
			notePicker.setScaleBaseline(Scales.MINOR, Scales.MINOR_PENTATONIC, 0, songStruct.key, octave);
		}
	}
	
	/**
	 * Function to set the current chord to be the underlying chord for the note picker class
	 * @param root The int value of the root of the chord within the scale/chord progresion
	 * @param newChordStruct The ChordStructs enum of the chord type
	 */
	public void setCurrentChord(int root, ChordStructs newChordStruct) {
		notePicker.setChord(newChordStruct);
	}
}
