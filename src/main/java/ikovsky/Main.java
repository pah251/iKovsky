package ikovsky;

import jm.JMC;

public class Main implements JMC {

    public static void main(String[] args) {

        SongGenerator song = new SongGenerator(false, "", "A",
                "120",
                "4/4",
                "3",
                "7",
                "50",
                "100",
                "90",
                "1",
                "0.3",
                "0.1",
                "0",
                "0.3",
                "0.3",
                "0",
                "0");

        //Params:
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
        final int numParams = 17;
        boolean debug = false;

        if (args.length < numParams && !debug) {
            System.out.println("Unable to generate music - not enough parameters");
        } else {
            //Debug output
            System.out.println("===========================================");
            System.out.println("Song Args: ");
            for (int i = 0; i < args.length; i++) {
                System.out.print(args[i] + " ");
            }
            System.out.println("");
            System.out.println("===========================================");


//			song.createSongComplex(); //write song
        }
    }
}
