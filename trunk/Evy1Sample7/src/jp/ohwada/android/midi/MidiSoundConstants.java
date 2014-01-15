package jp.ohwada.android.midi;

import java.util.ArrayList;
import java.util.List;

/**
 * MIDI Sound Constants
 *
 * http://www.midi.org/techspecs/gm1sound.php
 * http://ja.wikipedia.org/wiki/General_MIDI
 */
public class MidiSoundConstants {
	
 	// Instrument Patch Map
	// Piano
	public final static int INST_ACOUSTIC_PIANO = 0x0000;
	public final static int INST_BRIGHT_PIANO = 0x0001;
	public final static int INST_ELECTRIC_GRAND_PIANO = 0x0002;
	public final static int INST_HONKY_TONK_PIANO = 0x0003;
	public final static int INST_ELECTRIC_PIANO_1 = 0x0004;
	public final static int INST_ELECTRIC_PIANO_2 = 0x0005;	
	public final static int INST_HARPSICHORD = 0x0006;
	public final static int INST_CLAVI = 0x0007;

	// Chromatic Percussion
	public final static int INST_CELESTA = 0x0008;
	public final static int INST_GLOCKENSPIEL = 0x0009;
	public final static int INST_MUSICAL_BOX = 0x000A;
	public final static int INST_VIBRAPHONE = 0x000B;
	public final static int INST_MARIMBA	 = 0x000C;
	public final static int INST_XYLOPHONE	 = 0x000D;
	public final static int INST_TUBULAR_BELL = 0x000E;
	public final static int INST_DULCIMER = 0x000F;

	// Organ
	public final static int INST_DRAWBAR_ORGAN = 0x0010;
	public final static int INST_PERCUSSIVE_ORGAN = 0x0011;
	public final static int INST_ROCK_ORGAN = 0x0012;
	public final static int INST_CHURCH_ORGAN = 0x0013;
	public final static int INST_REED_ORGAN = 0x0014;
	public final static int INST_ACCORDION = 0x0015;
	public final static int INST_HARMONICAN = 0x0016;
	public final static int INST_TANGO_ACCORDION = 0x0017;
	
	// Guitar
	public final static int INST_ACOUSTIC_NYLON_GUITAR = 0x0018;
	public final static int INST_ACOUSTIC_STEEL_GUITAR = 0x0019;
	public final static int INST_ELECTRIC_JAZZ_GUITAR = 0x001A;
	public final static int INST_ELECTRIC_CLEAN_GUITAR = 0x001B;
	public final static int INST_ELECTRIC_MUTED_GUITAR = 0x001C;
	public final static int INST_OVERDRIVEN_GUITAR = 0x001D;
	public final static int INST_DISTORTION_GUITAR = 0x001E;
	public final static int INST_HARMONICS_GUITAR = 0x001F;

	// Bass
	public final static int INST_ACOUSTIC_BASS = 0x0020;
	public final static int INST_ELECTRIC_BASS_FINGER = 0x0021;
	public final static int INST_ELECTRIC_BASS_PICK = 0x0022;
	public final static int INST_FRETLESS_BASS = 0x0023;
	public final static int INST_SLAP_BASS_1 = 0x0024;
	public final static int INST_SLAP_BASS_2 = 0x0025;
	public final static int INST_SYNTH_BASS_1 = 0x0026;
	public final static int INST_SYNTH_BASS_2 = 0x0027;

	// Strings
	public final static int INST_VIOLIN	 = 0x0028;
	public final static int INST_VIOLA = 0x0029;
	public final static int INST_CELLO = 0x002A;
	public final static int INST_DOUBLE_BASS = 0x002B;
	public final static int INST_TREMOLO_STRINGS = 0x002C;
	public final static int INST_PIZZICATO_STRINGS = 0x002D;
	public final static int INST_ORCHESTRAL_HARP = 0x002E;
	public final static int INST_TIMPANI = 0x002F;
	
	// Ensemble
	public final static int INST_STRING_ENSEMBLE_1	 = 0x0030;
	public final static int INST_STRING_ENSEMBLE_2	 = 0x0031;	
	public final static int INST_SYNTH_STRINGS_1 = 0x0032;	
	public final static int INST_SYNTH_STRINGS_2 = 0x0033;	
	public final static int INST_VOICE_AAHS = 0x0034;	
	public final static int INST_VOICE_OOHS = 0x0035;
	public final static int INST_SYNTH_VOICE = 0x0036;
	public final static int INST_ORCHESTRA_HIT = 0x0037;
	
	// Brass
	public final static int INST_TRUMPET = 0x0038;
	public final static int INST_TROMBONE = 0x0039;
	public final static int INST_TUBA = 0x003A;
	public final static int INST_MUTED_TRUMPET = 0x003B;
	public final static int INST_FRENCH_HORN = 0x003C;
	public final static int INST_BRASS_SECTION = 0x003D;
	public final static int INST_SYNTH_BRASS_1 = 0x003E;
	public final static int INST_SYNTH_BRASS_2 = 0x003F;	

	// Reed
	public final static int INST_SOPRANO_SAX = 0x0040;	
	public final static int INST_ALTO_SAX = 0x0041;	
	public final static int INST_TENOR_SAX = 0x0042;
	public final static int INST_BARITONE_SAX = 0x0043;
	public final static int INST_OBOE = 0x0044;
	public final static int INST_ENGLISH_HORN = 0x0045;
	public final static int INST_BASSOON = 0x0046;
	public final static int INST_CLARINET = 0x0047;

	// Pipe
	public final static int INST_PICCOLO = 0x0048;
	public final static int INST_FLUTE = 0x0049;
	public final static int INST_RECORDER = 0x004A;
	public final static int INST_PAN_FLUTE = 0x004B;
	public final static int INST_BLOWN_BOTTLE = 0x004C;
	public final static int INST_SHAKUHACHI = 0x004D;
	public final static int INST_WHISTLE = 0x004E;
	public final static int INST_OCARINA = 0x004F;
	
	// Synth Lead
	public final static int INST_LEAD_SQUARE = 0x0050;
	public final static int INST_LEAD_SAWTOOTH = 0x0051;
	public final static int INST_LEAD_CALLIOPE = 0x0052;
	public final static int INST_LEAD_CHIFF = 0x0053;
	public final static int INST_LEAD_CHARANG = 0x0054;
	public final static int INST_LEAD_VOICE = 0x0055;
	public final static int INST_LEAD_FIFTHS = 0x0056;
	public final static int INST_LEAD_BASS = 0x0057;
	
	// Synth Pad
	public final static int INST_PAD_FANTASIA = 0x0058;
	public final static int INST_PAD_WARM = 0x0059;
	public final static int INST_PAD_POLYSYNTH = 0x005A;
	public final static int INST_PAD_CHOIR = 0x005B;
	public final static int INST_PAD_BOWED = 0x005C;
	public final static int INST_PAD_METALLIC = 0x005D;
	public final static int INST_PAD_HALO = 0x005E;
	public final static int INST_PAD_SWEEP = 0x005F;
	
	// Synth Effects
	public final static int INST_FX_RAIN = 0x0060;
	public final static int INST_FX_SOUNDTRACK = 0x0061;
	public final static int INST_FX_CRYSTAL = 0x0062;
	public final static int INST_FX_ATMOSPHERE = 0x0063;
	public final static int INST_FX_BRIGHTNESS = 0x0064;
	public final static int INST_FX_GOBLINS = 0x0065;
	public final static int INST_FX_ECHOES = 0x0066;
	public final static int INST_FX_SCI_FI = 0x0067;
	
	// Ethnic
	public final static int INST_SITAR = 0x0068;
	public final static int INST_BANJO	 = 0x0069;
	public final static int INST_SHAMISEN = 0x006A;
	public final static int INST_KOTO = 0x006B;
	public final static int INST_KALIMBA = 0x006C;
	public final static int INST_BAGPIPE = 0x006D;
	public final static int INST_FIDDLE = 0x006E;
	public final static int INST_SHANAI = 0x006F;
	
	// Percussive
	public final static int INST_TINKLE_BELL = 0x0070;
	public final static int INST_AGOGO = 0x0071;
	public final static int INST_STEEL_DRUMS = 0x0072;
	public final static int INST_WOODBLOCK = 0x0073;
	public final static int INST_TAIKO_DRUM = 0x0074;
	public final static int INST_MELODIC_TOM = 0x0075;
	public final static int INST_SYNTH_DRUM = 0x0076;
	public final static int INST_REVERSE_CYMBAL = 0x0077;
	
	// Sound effects
	public final static int INST_GUITAR_FRET_NOISE = 0x0078;
	public final static int INST_BREATH_NOISE = 0x0079;
	public final static int INST_SEASHORE = 0x007A;
	public final static int INST_BIRD_TWEET = 0x007B;
	public final static int INST_TELEPHONE_RING = 0x007C;
	public final static int INST_HELICOPTER	 = 0x007D;
	public final static int INST_APPLAUSE = 0x007E;
	public final static int INST_GUNSHOT = 0x007F;

	// Percussion Key Map
	public final static int PERC_ACOUSTIC_BASS_DRUM = 35;
	public final static int PERC_BASS_DRUM_1 = 36;	
	public final static int PERC_SIDE_STICK = 37;
	public final static int PERC_ACOUSTIC_SNARE = 38;	
	public final static int PERC_HAND_CLAP = 39;
	public final static int PERC_ELECTRIC_SNARE = 40;
	public final static int PERC_LOW_FLOOR_TOM = 41;
	public final static int PERC_CLOSED_HI_HAT = 42;	
	public final static int PERC_HIGH_FLOOR_TOM = 43;
	public final static int PERC_PEDAL_HI_HAT = 44;
	public final static int PERC_LOW_TOM = 45;
	public final static int PERC_OPEN_HI_HAT = 46;
	public final static int PERC_LOW_MID_TOM = 47;
	public final static int PERC_HI_MID_TOM = 48;
	public final static int PERC_CRASH_CYMBAL_1 = 49;
	public final static int PERC_HIGH_TOM = 50;
	public final static int PERC_RIDE_CYMBAL_1 = 51;
	public final static int PERC_CHINESE_CYMBAL = 52;
	public final static int PERC_RIDE_BELL = 53;
	public final static int PERC_TAMBOURINE = 54;	
	public final static int PERC_SPLASH_CYMBAL = 55;
	public final static int PERC_COWBELL = 56;
	public final static int PERC_CRASH_CYMBAL_2 = 57;	
	public final static int PERC_VIBRASLAP = 58;		
	public final static int PERC_RIDE_CYMBAL_2 = 59;
	public final static int PERC_HI_BONGO = 60;
	public final static int PERC_LOW_BONGO = 61;
	public final static int PERC_MUTE_HI_CONGA = 62;	
	public final static int PERC_OPEN_HI_CONGA = 63;
	public final static int PERC_LOW_CONGA = 64;
	public final static int PERC_HIGH_TIMBALE = 65;
	public final static int PERC_LOW_TIMBALE = 66;	
	public final static int PERC_HIGH_AGOGO = 67;
	public final static int PERC_LOW_AGOGO = 68;
	public final static int PERC_CABASA = 69;
	public final static int PERC_MARACAS = 70;
	public final static int PERC_SHORT_WHISTLE = 71;
	public final static int PERC_LONG_WHISTLE = 72;
	public final static int PERC_SHORT_GUIRO = 73;
	public final static int PERC_LONG_GUIRO = 74;
	public final static int PERC_CLAVES = 75;
	public final static int PERC_HI_WOOD_BLOCK = 76;	
	public final static int PERC_LOW_WOOD_BLOCK = 77;	
	public final static int PERC_MUTE_CUICA = 78;	
	public final static int PERC_OPEN_CUICA = 79;
	public final static int PERC_MUTE_TRIANGLE = 80;
	public final static int PERC_OPEN_TRIANGLE = 81;

	// Special Chennel
	public final static int CH_PERCUSSION = 9;
	
 	// Instrument
	public final static int INST_KEY_MIN = INST_ACOUSTIC_PIANO;
	public final static int INST_KEY_MAX = INST_GUNSHOT;

	// Percussion Key
	public final static int PERC_KEY_MIN = PERC_ACOUSTIC_BASS_DRUM;
	public final static int PERC_KEY_MAX = PERC_OPEN_TRIANGLE;

 	// Instrument Names	
	public final static String[] INSTRUMENT_NAMES = new String[]{
		// Piano
		"Acoustic Piano",
		"Bright Piano", 
		"Electric Grand Piano", 
		"Honky Tonk Piano", 
		"Electric Piano 1", 
		"Electric Piano 2", 
		"Harpsichord", 
		"Clavi", 
		// Chromatic Percussion
		"Celesta", 
		"Glockenspiel", 
		"Musical Box", 
		"Vibraphone", 
		"Marimba", 
		"Xylophone", 
		"Tubular Bell", 
		"Dulcimer", 
		// Organ
		"Drawbar Organ", 
		"Percussive Organ", 
		"Rock Organ", 
		"Church Organ", 
		"Reed Organ", 
		"Accordion", 
		"Harmonican", 
		"Tango Accordion", 
		// Guitar
		"Acoustic Nylon Guitar", 
		"Acoustic Steel Guitar", 
		"Electric Jazz Guitar", 
		"Electric Clean Guitar", 
		"Electric Muted Guitar", 
		"Overdriven Guitar", 
		"Distortion Guitar", 
		"Harmonics Guitar", 
		// Bass
		"Acoustic Bass", 
		"Electric Bass Finger", 
		"Electric Bass Pick", 
		"Fretless Bass", 
		"Slap Bass 1", 
		"Slap Bass 2", 
		"Synth Bass 1", 
		"Synth Bass 2", 
		// Strings
		"Violin", 
		"Viola", 
		"Cello", 
		"Double Bass", 
		"Tremolo Strings", 
		"Pizzicato Strings", 
		"Orchestral Harp", 
		"Timpani", 
		// Ensemble
		"String Ensemble 1", 
		"String Ensemble 2", 
		"Synth Strings 1", 
		"Synth Strings 2", 
		"Voice Aahs", 	
		"Voice Oohs", 
		"Synth Voice", 
		"Orchestra Hit", 
		// Brass
		"Trumpet", 
		"Trombone", 
		"Tuba", 
		"Muted Trumpet", 
		"French Horn", 
		"Brass Section", 
		"Synth Brass 1", 
		"Synth Brass 2", 
		// Reed
		"Soprano Sax", 	
		"Alto Sax", 	
		"Tenor Sax", 
		"Baritone Sax", 
		"Oboe", 
		"English Horn", 
		"Bassoon", 
		"Clarinet", 
		// Pipe
		"Piccolo", 
		"Flute", 
		"Recorder", 
		"Pan Flute", 
		"Blown Bottle", 
		"Shakuhachi", 
		"Whistle", 
		"Ocarina", 	
		// Synth Lead
		"Lead Square", 
		"Lead Sawtooth", 
		"Lead Calliope", 
		"Lead Chiff", 
		"Lead Charang", 
		"Lead Voice", 
		"Lead Fifths", 
		"Lead Bass", 	
		// Synth Pad
		"Pad Fantasia", 
		"Pad Warm", 
		"Pad Polysynth", 
		"Pad Choir", 
		"Pad Bowed", 
		"Pad Metallic", 
		"Pad Halo", 
		"Pad Sweep", 	
		// Synth Effects
		"Fx Rain", 
		"Fx Soundtrack", 
		"Fx Crystal", 
		"Fx Atmosphere", 
		"Fx Brightness", 
		"Fx Goblins", 
		"Fx Echoes", 
		"Fx Sci Fi", 	
		// Ethnic
		"Sitar", 
		"Banjo", 
		"Shamisen", 
		"Koto", 
		"Kalimba", 
		"Bagpipe", 
		"Fiddle", 
		"Shanai", 	
		// Percussive
		"Tinkle Bell", 
		"Agogo", 
		"Steel Drums", 
		"Woodblock", 
		"Taiko Drum", 
		"Melodic Tom", 
		"Synth Drum", 
		"Reverse Cymbal", 	
		// Sound Effects
		"Guitar Fret Noise", 
		"Breath Noise", 
		"Seashore", 
		"Bird Tweet", 
		"Telephone Ring", 
		"Helicopter", 
		"Applause", 
		"Gunshot" };

	// Percussion Names
	public final static String[] PERCUSSION_NAMES = new String[]{
		"Acoustic Bass Drum",
		"Bass Drum 1",	
		"Side Stick",
		"Acoustic Snare",
		"Hand Clap",
		"Electric Snare",
		"Low Floor Tom",
		"Closed Hi Hat",	
		"High Floor Tom",
		"Pedal Hi Hat",
		"Low Tom",
		"Open Hi Hat",
		"Low Mid Tom",
		"Hi Mid Tom",
		"Crash Cymbal 1",
		"High Tom",
		"Ride Cymbal 1",
		"Chinese Cymbal",
		"Ride Bell",
		"Tambourine",
		"Splash Cymbal",
		"Cowbell",
		"Crash Cymbal 2",
		"Vibraslap",	
		"Ride Cymbal 2",
		"Hi Bongo",
		"Low Bongo",
		"Mute Hi Conga",
		"Open Hi Conga",
		"Low Conga",
		"High Timbale",
		"Low Timbale",	
		"High Agogo",
		"Low Agogo",
		"Cabasa",
		"Maracas",
		"Short Whistle",
		"Long Whistle",
		"Short Guiro",
		"Long Guiro",
		"Claves",
		"Hi Wood Block",
		"Low Wood Block",
		"Mute Cuica",	
		"Open Cuica",
		"Mute Triangle",
		"Open Triangle" };

 	// Instrument Group
	public static List<InstGroup> INST_GROUP_LIST = new ArrayList<InstGroup>();

 	// initialize Instrument Group
	static {
		 addGroupList( "Piano", INST_ACOUSTIC_PIANO, INST_CLAVI );
		 addGroupList( "Chromatic Percussion", INST_CELESTA, INST_DULCIMER );
		 addGroupList( "Organ", INST_DRAWBAR_ORGAN, INST_TANGO_ACCORDION );
		 addGroupList( "Guitar", INST_ACOUSTIC_NYLON_GUITAR, INST_HARMONICS_GUITAR );
		 addGroupList( "Bass", INST_ACOUSTIC_BASS, INST_SYNTH_BASS_2 );
		 addGroupList( "Strings", INST_VIOLIN, INST_TIMPANI );
		 addGroupList( "Ensemble", INST_STRING_ENSEMBLE_1, INST_ORCHESTRA_HIT );
		 addGroupList( "Brass", INST_TRUMPET, INST_SYNTH_BRASS_2 );
		 addGroupList( "Reed", INST_SOPRANO_SAX, INST_CLARINET );
		 addGroupList( "Pipe", INST_PICCOLO, INST_OCARINA );
		 addGroupList( "Synth Lead", INST_LEAD_SQUARE, INST_LEAD_BASS );
		 addGroupList( "Synth Pad", INST_PAD_FANTASIA, INST_PAD_SWEEP );
		 addGroupList( "Synth Effects", INST_FX_RAIN, INST_FX_SCI_FI );
		 addGroupList( "Ethnic", INST_SITAR, INST_SHANAI );
		 addGroupList( "Percussive", INST_TINKLE_BELL, INST_REVERSE_CYMBAL );
		 addGroupList( "Sound Effects", INST_GUITAR_FRET_NOISE, INST_GUNSHOT );
	}

	/*
	 * getPercussionName
	 * @param int key
	 * @return String
	 */ 	
	public static String getPercussionName( int key ) {
		if ( key < PERC_KEY_MIN ) return null;
		if ( key > PERC_KEY_MAX ) return null;
		return PERCUSSION_NAMES[ key - PERC_KEY_MIN ] ;
	}

	/*
	 * addGroupList
	 * @param String name
	 * @param int start
	 * @param int end 
	 */ 
	private static void addGroupList( String name, int start, int end ) {
 		INST_GROUP_LIST.add( new InstGroup( name, start, end ));
	}

	/*
	 * --- class InstGroup ---
	 */  	
	public static class InstGroup {
		public String name = "";
		public int start = 0;
		public int end = 0;	

		/*
		 * 	--- Constructor ---
		 * @param String name
		 * @param int start
		 * @param int end 
		 */  
		public InstGroup( String _name, int _start, int _end ) {	
			name = _name;
			start = _start;
			end = _end;
		}	
	}

}
