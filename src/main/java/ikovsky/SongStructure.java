package ikovsky;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Paul Hudson
 * The SongStructure class holds all information about the song's musical properties
 */
public class SongStructure {
    public int key;
    public boolean isMinorKey;

    public int tempo;

    public double timeSigNoteValue;
    public int timeSigBeatsPerBar;

    public int duration;  //Number of verses/choruses (e.g. ABACA = 5)
    public int numVerses; //Number of different verses (e.g. ABACA = 3)

    //TODO: Unused? What for?
    public int barsPerVerse; //Number of bars in a verse -> defaults to time sig multiplication

    public ArrayList<Integer> verseStructure; //Integer representation of verse/chorus structure (e.g. ABACA = {0,1,0,2,0})

    public int instrument; //Instrument for the track

    public int numParts; //Amount of parts to the track

    public double rhythmChance; //Chance of a part being a rhythm part

    //Dynamic range of a note
    public int minDynamic;
    public int maxDynamic;

    public double[] durations; //Different available durations

    //Min and Max octaves
    public int minOctave;
    public int maxOctave;

    public double[] chordWeightings; //Weightings for different chords in progression (I, ii, ..., vii(o))

    public int noteDensity; //Min and Max note densities

    /**
     * Constructor for SongStructure class
     */
    public SongStructure(String key, String tempo, String timeSig, String octaveLow,
                         String octaveHigh, String dynamicsLow, String dynamicsHigh, String noteDensity, String instrument,
                         String weightA, String weightB, String weightC, String weightD, String weightE, String weightF, String weightG) {
        Random rand = new Random();                        //Random Number Generator

        this.key = validateKey(key);
        this.isMinorKey = isMinorKey(key);
        this.tempo = validateTempo(tempo);

        this.timeSigNoteValue = validateTimeSigNoteValue(timeSig);
        this.timeSigBeatsPerBar = validateTimeSigBeatsPerBar(timeSig);

        this.durations = IKConstants.DEFAULT_DURATIONS;

        this.instrument = validateInstrument(instrument);

        this.minDynamic = intCappedAt100(dynamicsLow);
        int maxDynamic = intCappedAt100(dynamicsHigh);
        this.maxDynamic = Math.max(maxDynamic, minDynamic);

        this.minOctave = validateOctaveLower(octaveLow);
        int maxOctave = validateOctaveUpper(octaveHigh);
        this.maxOctave = Math.max(maxOctave, minOctave);

        this.chordWeightings = validateChordWeightings(weightA, weightB, weightC, weightD, weightE, weightF, weightG);
        this.noteDensity = intCappedAt100(noteDensity);

        //Roll between min and max default values for duration and numVerses
        this.duration = rand.nextInt(IKConstants.SONG_DURATION_UPPER - IKConstants.SONG_DURATION_LOWER) + IKConstants.SONG_DURATION_LOWER;
        this.numVerses = rand.nextInt(IKConstants.SONG_VERSE_CHORUS_UPPER - IKConstants.SONG_VERSE_CHORUS_LOWER) + IKConstants.SONG_DURATION_UPPER;

        this.verseStructure = generateVerseStructure();

        this.rhythmChance = IKConstants.DEFAULT_RHYTHM_CHANCE; //set to default

        //Use roulette selection for deciding the number of different parts to the song, between a solo part, and a quartet
        this.numParts = generateNumParts(rand);

        printStructureDetails(); //debug output
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
        System.out.println("Octaves: " + minOctave + " - " + maxOctave);
        System.out.println("Dynamics: " + minDynamic + " - " + maxDynamic);
        System.out.println("Note Density: " + noteDensity);
        System.out.println("Weights: " + Arrays.toString(chordWeightings));
        System.out.println("------------------------------------");
    }

    private ArrayList<Integer> generateVerseStructure() {
        ArrayList<Integer> verseStructure = new ArrayList<>();
        Random rand1 = new Random();
        for (int i = 0; i < duration; i++) {
            verseStructure.add(rand1.nextInt(numVerses));
        }
        return verseStructure;
    }

    private int generateNumParts(Random rand) {
        double partsP = rand.nextDouble();
        if (partsP < IKConstants.DEFAULT_NUM_PARTS_1_PROBABILITY) {
            return 1;
        } else if (partsP < (IKConstants.DEFAULT_NUM_PARTS_1_PROBABILITY + IKConstants.DEFAULT_NUM_PARTS_2_PROBABILITY)) {
            return 2;
        } else if (partsP < (IKConstants.DEFAULT_NUM_PARTS_1_PROBABILITY + IKConstants.DEFAULT_NUM_PARTS_2_PROBABILITY + IKConstants.DEFAULT_NUM_PARTS_3_PROBABILITY)) {
            return 3;
        } else {
            return 4;
        }
    }

    /**
     * Function for setting the 7 chord weightings
     * Each weighting Arg should be a double or 0 value less than 5 characters in length
     * Regex for parsing double taken from: http://stackoverflow.com/questions/6754552/regex-to-find-a-float-probably-a-really-simple-question
     * @return
     */
    private double[] validateChordWeightings(String weightA, String weightB, String weightC, String weightD, String weightE, String weightF, String weightG) {
        double[] chordWeightings = new double[7];
        chordWeightings[0] = parseChordWeighting(weightA);
        chordWeightings[1] = parseChordWeighting(weightB);
        chordWeightings[2] = parseChordWeighting(weightC);
        chordWeightings[3] = parseChordWeighting(weightD);
        chordWeightings[4] = parseChordWeighting(weightE);
        chordWeightings[5] = parseChordWeighting(weightF);
        chordWeightings[6] = parseChordWeighting(weightG);
        
        return chordWeightings;
    }

    private double parseChordWeighting(String chordWeightingArg) {
        if (!chordWeightingArg.isEmpty() && chordWeightingArg.length() <= 4 && chordWeightingArg.matches("^([+-]?\\d*\\.?\\d*)$")) {
            return Double.parseDouble(chordWeightingArg);
        } else {
            throw new RuntimeException("Error setting chord weighting, input = " + chordWeightingArg);
        }
    }

    /**
     * Function for setting the instrument
     * @return
     */
    private int validateInstrument(String instrumentStr) {
        //Check the string contains a value which is less than 4 digits in length (Max instrument value = 126)
        if (!instrumentStr.isEmpty() && instrumentStr.length() < 4 && instrumentStr.matches("[0-9]+")) {
            int instrumentInt = Integer.parseInt(instrumentStr);

            //If the parsed instrument value is greater than the maximum (126), set it to 0 (Grand Piano)
            return instrumentInt < 126 ? instrumentInt : 0;
        } else {
            throw new RuntimeException("Error setting instrument, input = " + instrumentStr);
        }
    }

    /**
     * Function for setting the lower dynamics limit
     * Argument should be an integer less-than or equal to 100
     * @return
     */
    private int intCappedAt100(String dynamicsLowArg) {

        //Check the string is nonempty and within limits
        if (!dynamicsLowArg.isEmpty() && dynamicsLowArg.length() < 4 && dynamicsLowArg.matches("[0-9]+")) {
            int minDynamicInt = Integer.parseInt(dynamicsLowArg);

            //Ensure parameter is within range (0 - 100)
            return Math.min(minDynamicInt, 100);
        } else {
            throw new RuntimeException("Error setting dynamics lower, input = " + dynamicsLowArg);
        }
    }

    /**
     * Function to set the higher octave value
     * Arg should be an integer value lower than 10
     */
    private int validateOctaveUpper(String octaveUpperArg) {
        //Check the argument is a non-empty string containing an integer value less than 10
        if (octaveUpperArg.length() == 1 && octaveUpperArg.matches("[0-9]+") && !octaveUpperArg.contains(".")) {
            int maxOctaveInt = Integer.parseInt(octaveUpperArg);

            //Adjust the value to sensible parameters if needed
            return Math.max(maxOctaveInt, IKConstants.SONG_OCTAVE_UPPER);
        } else {
            throw new RuntimeException("Error setting octave higher, input = " + octaveUpperArg); //Add error message
        }
    }

    /**
     * Function to set the lower octave value
     * Arg should be an integer value lower than 10
     * @return
     */
    private int validateOctaveLower(String octaveLowerArg) {
        //Check the argument is a non-empty string containing an integer value less than 10
        if (octaveLowerArg.length() == 1 && octaveLowerArg.matches("[0-9]+") && !octaveLowerArg.contains(".")) {
            int minOctaveInt = Integer.parseInt(octaveLowerArg);

            //Adjust the value to sensible parameters if needed
            return Math.max(minOctaveInt, IKConstants.SONG_OCTAVE_LOWER);
        } else {
            throw new RuntimeException("Error setting octave lower, input = " + octaveLowerArg); //Add error message
        }
    }

    /**
     * Function for parsing the Time Signature arg
     * Should be of format (X/Y) where X and Y are integers
     *
     * @return
     */
    private double validateTimeSigNoteValue(String timeSigArg) {
        //Check the string isn't empty, is greater than the minimum length (X/X = 3 chars in length)
        //Also assert that the two integers are split with a "/" character and the numbers entered are not float values
        if (!timeSigArg.isEmpty() && timeSigArg.length() >= 3 &&
                timeSigArg.matches("[0-9]+/[0-9]+") && timeSigArg.contains("/") &&
                !timeSigArg.endsWith("/") && !timeSigArg.startsWith("/") &&
                !timeSigArg.contains(".")) {
            String[] timeSigStr = timeSigArg.split("/"); //Get the two integers

            //Set the relative beat value for the time signature
            int timeSigNoteValueInt = Integer.parseInt(timeSigStr[1]);

            switch (timeSigNoteValueInt) {
                case 1:
                    return jm.constants.Durations.WHOLE_NOTE;
                case 2:
                    return jm.constants.Durations.HALF_NOTE;
                case 4:
                    return jm.constants.Durations.QUARTER_NOTE;
                case 8:
                    return jm.constants.Durations.EIGHTH_NOTE;
                case 16:
                    return jm.constants.Durations.SIXTEENTH_NOTE;
                default:
                    //Default to standard quarter note
                    return jm.constants.Durations.QUARTER_NOTE;
            }
        } else {
            throw new RuntimeException("Unable to parse time signature, ensure the value is a string of format [4/4] or [3/8] etc.: " + timeSigArg); //Add error message
        }
    }

    private int validateTimeSigBeatsPerBar(String timeSigArg) {
        //Check the string isn't empty, is greater than the minimum length (X/X = 3 chars in length)
        //Also assert that the two integers are split with a "/" character and the numbers entered are not float values
        if (!timeSigArg.isEmpty() && timeSigArg.length() >= 3 &&
                timeSigArg.matches("[0-9]+/[0-9]+") && timeSigArg.contains("/") &&
                !timeSigArg.endsWith("/") && !timeSigArg.startsWith("/") &&
                !timeSigArg.contains(".")) {
            String[] timeSigStr = timeSigArg.split("/"); //Get the two integers

            return Integer.parseInt(timeSigStr[0]);
        } else {
            throw new RuntimeException("Unable to parse time signature, ensure the value is a string of format [4/4] or [3/8] etc.: " + timeSigArg); //Add error message
        }
    }

    /**
     * Function for parsing Tempo arg
     * Arg should be an integer value between 0 and 300
     */
    private int validateKey(String keyArg) {
        boolean success = false;
        boolean isSharp = false;

        int keyToReturn = 0;

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
                if (keyArg.toUpperCase().charAt(1) == 'S' && //Sharp
                        keyArg.toUpperCase().charAt(2) == 'M' && //Minor
                        keyArg.toUpperCase().charAt(0) != 'B' && //Not B or E
                        keyArg.toUpperCase().charAt(0) != 'E') {
                    isSharp = true;
                    success = true;
                } else {
                    success = false;
                }
            }
            //Set the key from the first character
            if (success || keyArg.length() == 1) {
                switch (keyArg.toUpperCase().charAt(0)) {
                    case 'A':
                        keyToReturn = ScaleNotes.A.getNoteVal();
                        break;
                    case 'B':
                        keyToReturn = ScaleNotes.B.getNoteVal();
                        break;
                    case 'C':
                        keyToReturn = ScaleNotes.C.getNoteVal();
                        break;
                    case 'D':
                        keyToReturn = ScaleNotes.D.getNoteVal();
                        break;
                    case 'E':
                        keyToReturn = ScaleNotes.E.getNoteVal();
                        break;
                    case 'F':
                        keyToReturn = ScaleNotes.F.getNoteVal();
                        break;
                    case 'G':
                        keyToReturn = ScaleNotes.G.getNoteVal();
                        break;
                    default:
                        success = false;
                        break;
                }

                if (isSharp) {
                    keyToReturn++; //Increment by 1 for the relative sharp key
                }
                return keyToReturn;
            } else {
                throw new RuntimeException("Key format invalid:" + keyArg);
            }
        }
        return 0;
    }

    private boolean isMinorKey(String keyArg) {
        if (keyArg.length() == 2) {
            //Minor
            return keyArg.toUpperCase().charAt(1) == 'M';
        }
        //When length is 3, the key should be both a Sharp and a Minor
        if (keyArg.length() == 3) {
            //Assert that the 2nd character denotes a sharp
            //		 that the 3rd character denotes a minor key
            //		 that the 1st character is not B or E (no key such as B# or E#)
            return keyArg.toUpperCase().charAt(1) == 'S' && //Sharp
                    keyArg.toUpperCase().charAt(2) == 'M' && //Minor
                    keyArg.toUpperCase().charAt(0) != 'B' && //Not B or E
                    keyArg.toUpperCase().charAt(0) != 'E';
        }
        return false;
    }

    private int validateTempo(String tempoArg) {
        //Check the string isn't empty and contains only integers
        if (!tempoArg.isEmpty() && tempoArg.matches("[0-9]+") && !tempoArg.contains(".") && tempoArg.length() < 4) {
            return Integer.parseInt(tempoArg);
        } else {
            throw new RuntimeException("Tempo format invalid:" + tempoArg);
        }
    }
}
