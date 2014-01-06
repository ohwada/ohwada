package jp.ohwada.android.midi;

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
	public final static int INST_ELECTRIC_PIANO = 0x0004;
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
	public final static int INST_HARMONICA	 = 0x0016;
	public final static int INST_TANGO_ACCORDION	 = 0x0017;
	
	// Guitar
	public final static int INST_ACOUSTIC_GUITAR_NYLON = 0x0018;
	public final static int INST_ACOUSTIC_GUITAR_STEEL = 0x0019;
	public final static int INST_ELECTRIC_GUITAR_JAZZ = 0x001A;
	public final static int INST_ELECTRIC_GUITAR_CLEAN = 0x001B;
	public final static int INST_ELECTRIC_GUITAR_MUTED = 0x001C;
	public final static int INST_OVERDRIVEN_GUITAR = 0x001D;
	public final static int INST_DISTORTION_GUITAR = 0x001E;
	public final static int INST_GUITAR_HARMONICS	 = 0x001F;

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

	// Special Chennel
	public final static int CH_DRUMS = 9;

	// Percussion Key Map
	public final static int DRUM_KEY_ACOUSTIC_BASS_DRUM = 35;
	public final static int DRUM_KEY_BASS_DRUM_1 = 36;	
	public final static int DRUM_KEY_SIDE_STICK = 37;
	public final static int DRUM_KEY_ACOUSTIC_SNARE = 38;	
	public final static int DRUM_KEY_HAND_CLAP = 39;
	public final static int DRUM_KEY_ELECTRIC_SNARE = 40;
	public final static int DRUM_KEY_LOW_FLOOR_TOM = 41;
	public final static int DRUM_KEY_CLOSED_HI_HAT = 42;	
	public final static int DRUM_KEY_HIGH_FLOOR_TOM = 43;
	public final static int DRUM_KEY_PEDAL_HI_HAT = 44;
	public final static int DRUM_KEY_LOW_TOM = 45;
	public final static int DRUM_KEY_OPEN_HI_HAT = 46;
	public final static int DRUM_KEY_LOW_MID_TOM = 47;
	public final static int DRUM_KEY_HI_MID_TOM = 48;
	public final static int DRUM_KEY_CRASH_CYMBAL_1 = 49;
	public final static int DRUM_KEY_HIGH_TOM = 50;
	public final static int DRUM_KEY_RIDE_CYMBAL_1 = 51;
	public final static int DRUM_KEY_CHINESE_CYMBAL = 52;
	public final static int DRUM_KEY_RIDE_BELL = 53;
	public final static int DRUM_KEY_TAMBOURINE = 54;	
	public final static int DRUM_KEY_SPLASH_CYMBAL = 55;
	public final static int DRUM_KEY_COWBELL = 56;
	public final static int DRUM_KEY_CRASH_CYMBAL_2 = 57;	
	public final static int DRUM_KEY_VIBRASLAP = 58;		
	public final static int DRUM_KEY_RIDE_CYMBAL_2 = 59;
	public final static int DRUM_KEY_HI_BONGO = 60;
	public final static int DRUM_KEY_LOW_BONGO = 61;
	public final static int DRUM_KEY_MUTE_HI_CONGA = 62;	
	public final static int DRUM_KEY_OPEN_HI_CONGA = 63;
	public final static int DRUM_KEY_LOW_CONGA = 64;
	public final static int DRUM_KEY_HIGH_TIMBALE = 65;
	public final static int DRUM_KEY_LOW_TIMBALE = 66;	
	public final static int DRUM_KEY_HIGH_AGOGO = 67;
	public final static int DRUM_KEY_LOW_AGOGO = 68;
	public final static int DRUM_KEY_CABASA = 69;
	public final static int DRUM_KEY_MARACAS = 70;
	public final static int DRUM_KEY_SHORT_WHISTLE = 71;
	public final static int DRUM_KEY_LONG_WHISTLE = 72;
	public final static int DRUM_KEY_SHORT_GUIRO = 73;
	public final static int DRUM_KEY_LONG_GUIRO = 74;
	public final static int DRUM_KEY_CLAVES = 75;
	public final static int DRUM_KEY_HI_WOOD_BLOCK = 76;	
	public final static int DRUM_KEY_LOW_WOOD_BLOCK = 77;	
	public final static int DRUM_KEY_MUTE_CUICA = 78;	
	public final static int DRUM_KEY_OPEN_CUICA = 79;
	public final static int DRUM_KEY_MUTE_TRIANGLE = 80;
	public final static int DRUM_KEY_OPEN_TRIANGLE = 81;
		
}
