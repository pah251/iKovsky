package ikovsky;

/**
 * @author Paul Hudson
 * Interface to hold all constants
 */
public interface IKConstants {
	//--------- Scale Note Counts ---------
	public static final int NUM_CHROMATIC_NOTES = 12;
	public static final int NUM_MAJOR_SCALE_NOTES = 8;
	public static final int NUM_MINOR_SCALE_NOTES = 8;
	public static final int NUM_PENTATONIC_SCALE_NOTES = 5;
	public static final int NUM_SCALE_CHORDS = 7;
	
	//--------- Parameter Parsing Constants ---------
	public static final int FILE_SAVE_PARAM_INDEX = 0;
	public static final int KEY_PARAM_INDEX = 1;
	public static final int TEMPO_PARAM_INDEX = 2;
	public static final int TIME_SIG_PARAM_INDEX = 3;
	public static final int OCTAVE_LOWER_PARAM_INDEX = 4;
	public static final int OCTAVE_HIGHER_PARAM_INDEX = 5;
	public static final int CHORD_WEIGHTINGS_PARAM_INDEX = 6;
	public static final int DYNAMICS_LOWER_PARAM_INDEX = 13;
	public static final int DYNAMICS_HIGHER_PARAM_INDEX = 14;
	public static final int NOTE_DENSITY_PARAM_INDEX = 15;
	public static final int INSTRUMENT_PARAM_INDEX = 16;	
	
	//--------- General song constants ---------
	//Duration lower and upper boundaries
	public static final int SONG_DURATION_LOWER = 6;
	public static final int SONG_DURATION_UPPER = 12;
	//Number of different verse/choruses
	public static final int SONG_VERSE_CHORUS_LOWER = 2;
	public static final int SONG_VERSE_CHORUS_UPPER = 8;
	//Octave Boundaries
	public static final int SONG_OCTAVE_LOWER = 3;
	public static final int SONG_OCTAVE_UPPER = 7;
	
	//--------- Composer Constants ---------
	public static final double COMPOSER_MEASURES_1_BARS_4_PROBABILITY = 0.3;
	public static final double COMPOSER_MEASURES_2_BARS_2_PROBABILITY = 0.3;
	public static final double COMPOSER_MEASURES_1_BARS_8_PROBABILITY = 0.2;
	public static final double COMPOSER_MEASURES_2_BARS_1_PROBABILITY = 0.2;
	
	//--------- NotePicker Constants ---------
	public static final double NOTEPICKER_BASE_CHROMATIC_PROBABILITY = 0.005;
	public static final double NOTEPICKER_CURRENT_SCALE_PROBABILITY = 0.075;
	public static final double NOTEPICKER_CURRENT_PENTATONIC_SCALE_PROBABILITY = 0.125;
	public static final double NOTEPICKER_CURRENT_CHORD_PROBABILITY = 0.1625;
	public static final double NOTEPICKER_CURRENT_NOTE_PROBABILITY = 0.25;
	public static final double NOTEPICKER_OCTAVE_DECREASE_PROBABILITY = 0.05;
	public static final double NOTEPICKER_OCTAVE_INCREASE_PROBABILITY = 0.95;
	
	//--------- PartCreator Constants ----------
	public static final double CHORDPICKER_ALT_STRUCT_CHANCE = 0.15;
	
	//--------- PartCreator Constants ----------
	public static final double PARTCREATOR_CHORD_3_NOTES_PROBABILITY = 0.7;
	public static final double PARTCREATOR_CHORD_2_NOTES_PROBABILITY = 0.15;
	public static final double PARTCREATOR_CHORD_1_NOTES_PROBABILITY = 0.15;
	public static final double PARTCREATOR_MELODY_CHORD_PROBABILITY = 0.65;
	
	
	//--------- Default Structure Components ---------
	public static final int[]		DEFAULT_INSTRUMENTS = {
		jm.constants.ProgramChanges.GUITAR,
		jm.constants.ProgramChanges.PIANO,
		jm.constants.ProgramChanges.CHOIR,
		jm.constants.ProgramChanges.CHURCH_ORGAN,
		jm.constants.ProgramChanges.HARPSICHORD,
	};
	public static final int		DEFAULT_NOTE_DENSITY_LOWER = 40;
	public static final int		DEFAULT_NOTE_DENSITY_UPPER = 80;
	public static final int		DEFAULT_NUM_PARTS_LOWER = 1;
	public static final int		DEFAULT_NUM_PARTS_UPPER = 4;
	public static final double	DEFAULT_NUM_PARTS_1_PROBABILITY = 0.05;
	public static final double	DEFAULT_NUM_PARTS_2_PROBABILITY = 0.4;
	public static final double	DEFAULT_NUM_PARTS_3_PROBABILITY = 0.4;
	public static final double	DEFAULT_NUM_PARTS_4_PROBABILITY = 0.15;
	public static final double	DEFAULT_RHYTHM_CHANCE = 0.4;
	public static final int		DEFAULT_DYNAMIC_LOWER = 60;
	public static final int		DEFAULT_DYNAMIC_UPPER = 80;
	public static final double[]	DEFAULT_DURATIONS = 	{
		jm.constants.Durations.HALF_NOTE,
		jm.constants.Durations.CROTCHET,
		jm.constants.Durations.WHOLE_NOTE,
		jm.constants.Durations.EIGHTH_NOTE,
		jm.constants.Durations.SIXTEENTH_NOTE,
	};
}
