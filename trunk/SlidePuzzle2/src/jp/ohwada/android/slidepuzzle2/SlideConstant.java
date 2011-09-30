package jp.ohwada.android.slidepuzzle2;
  
public class SlideConstant 
{ 
	// limit
	public final static int MIN_GAME  = 1;
	public final static int MAX_GAME  = 200;
    public final static int MIN_WIDTH = 3;
    public final static int MAX_WIDTH = 6;
    public final static int MIN_HEIGHT = 3;
    public final static int MAX_HEIGHT = 6;
    public final static int MIN_PIECE = 1;
    public final static int MAX_PIECE = 36;
    public final static int MAX_SLIDE = 4;

	// board
    public final static int B_MIN = 1;
    public final static int B_BLANK = 0;
    public final static int B_WALL = -1;
	
    // blank 
    public static final int BLANK_N = 0;   
    public static final int BLANK_L = 1;   
    public static final int BLANK_R = 2; 
    public static final int BLANK_U = 3; 
    public static final int BLANK_D = 4; 

	// char for output	
    public final static String[] MOVE_CHAR = {"", "L", "R", "U", "D"};

	// square    
    public static final int SQUARE_UNDEFINED = -1; 
    	
    // messag
    public static final int MESSAGE_SWAP = 1;
    public static final int MESSAGE_PREV = 2;
    public static final int MESSAGE_CLOSE = 3;
    public static final int MESSAGE_NEXT = 4;
}