/*
= * SongGenerator class handles setting up the initial parameters for the song
 * This class also handles the output of the midi file
 */

package ikovsky;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

import jm.JMC;
import jm.gui.show.ShowScore;
import jm.music.data.Score;
import jm.util.Play;
import jm.util.Write;
import org.apache.commons.io.FileUtils;

/**
 * @author Paul Hudson
 * The Song Generator class is the top level class for handling music generation
 * including parsing of the input parameters, and output of the finished .midi file
 */
public class SongGenerator implements JMC {
    private boolean useDefaults;                //boolean field to denote debug mode

    private Score songScore;                    //JMusic class for the score of the whole song (sum of all parts)

    private SongStructure songStructure;        //Class for storing all data to do with structure of song (key/tempo/verses etc)

    private String saveDir;                        //File directory for saving

    private ArrayList<String> parameterErrors;    //ArrayList to contain all potential errors to display to the user

    private Random rand;                        //Random Number Generator

    public SongGenerator(boolean debug, String saveLocation, String key, String tempo, String timeSig, String octaveLow,
                         String octaveHigh, String dynamicsLow, String dynamicsHigh, String noteDensity, String instrument,
                         String weightA, String weightB, String weightC, String weightD, String weightE, String weightF, String weightG) {
        useDefaults = debug;

        songScore = new Score("New Song"); //Song Title
        SongStructure structure = new SongStructure(key, tempo, timeSig, octaveLow, octaveHigh, dynamicsLow, dynamicsHigh,
                noteDensity, instrument, weightA, weightB, weightC, weightD, weightE, weightF, weightG);

        this.saveDir = saveLocation;
        this.songStructure = structure;

        parameterErrors = new ArrayList<>();

        rand = new Random();
    }

//	/**
//	 * Function for writing the new song
//	 */
//	public void createSongComplex() {
//		Composer newComp = new Composer(songStructure); //Create the composer
//
//		//Create all the required parts
//		for (int i = 0 ; i < songStructure.numParts; i++) {
//			boolean isRhythmPart = false;
//			int partOctave = rand.nextInt(songStructure.maxOctave - songStructure.minOctave) + songStructure.minOctave + 1; //Randomise the octave within limits
//
//			//Decide rhythm part
//			if (rand.nextDouble() < songStructure.rhythmChance) {
//				isRhythmPart = true;
//			}
//
//			newComp.addPart(isRhythmPart, songStructure.instrument, partOctave, songStructure.minDynamic, songStructure.maxDynamic);
//		}
//
//		newComp.writeSong(songScore); //Write the song to the score
//
//		saveSong(); //Save the midi file
//	}

    public String generateSongString() {
        Composer newComp = new Composer(songStructure); //Create the composer

        //Create all the required parts
        for (int i = 0; i < songStructure.numParts; i++) {
            boolean isRhythmPart = false;
            int partOctave;
            if(songStructure.maxOctave == songStructure.minOctave) {
                partOctave = songStructure.maxOctave;
            } else {
                partOctave = rand.nextInt(songStructure.maxOctave - songStructure.minOctave) + songStructure.minOctave + 1; //Randomise the octave within limits
            }

            //Decide rhythm part
            if (rand.nextDouble() < songStructure.rhythmChance) {
                isRhythmPart = true;
            }

            newComp.addPart(isRhythmPart, songStructure.instrument, partOctave, songStructure.minDynamic, songStructure.maxDynamic);
        }

        newComp.writeSong(songScore); //Write the song to the score
        System.out.println(songScore.toString());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Write.midi(songScore, byteArrayOutputStream);

//        return Base64.getEncoder().encodeToString(saveSong().getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    /**
     * Function for saving the song to the input save location
     * @return
     */
    public String saveSong() {
        //Build the file location string
        String fileLoc =  "/tmp/midioutput.mid";
//        fileLoc +=;

        //Write the file
        Write.midi(songScore, fileLoc);
        System.out.println(fileLoc);
        System.out.println("MIDI file saved");

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(fileLoc);
//        File file = new File(classLoader.getResource("/tmp/midioutput.mid").getFile());
        try {
            String data = FileUtils.readFileToString(file, "UTF-8");
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //For debugging / development --> play the song locally
        if (useDefaults) {
            new ShowScore(songScore);
            Play.midi(songScore);
        }
        return "500";
    }
}
