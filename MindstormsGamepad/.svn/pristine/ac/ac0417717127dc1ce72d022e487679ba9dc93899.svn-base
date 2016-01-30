package jp.ohwada.android.mindstormsgamepad.util;

import java.io.UnsupportedEncodingException;

import jp.ohwada.android.mindstormsgamepad.Constant;
import android.util.Log;

/**
 *  Byte Utility
 */
public class ByteUtility {
	/** Debug */
    private static final String TAG = Constant.TAG; 
	private static final boolean D = Constant.DEBUG;    
	private static String TAG_SUB = "ByteUtility: ";

	private static final int NUM_8BITS = 8;
	private static final int RADIX_HEX = 16;	
	private static final String CHARNAME_ASCII = "US-ASCII";
	private static final String CHAR_ZERO = "0";
	private static final String CHAR_ONE = "1";
	private static final String CHAR_SPACE = " ";
		
	/**
	 * === constructor ===
	 */	
	public ByteUtility() {
		// dummy
	} 

// --- Rotate Bits ----
	/**
	 * Rotate Bits 
	 * @param int n
	 * @param boolean[][] bits
	 * @return boolean[][]
	 */ 
	public boolean[][] nRotateBits( int n, boolean[][] bits ) {
		boolean[][] rotate_bits = new boolean[ n ][ n ];
		for ( int i=0; i < n ; i++ ) {
			for ( int j=0; j < n; j++ ) {
				rotate_bits[ i ][ j ] = bits[ j ][ i ];			
			}
		}
		return rotate_bits;
	}
	
	/**
	 * Rotate Bits Reverse
	 * @param int n
	 * @param boolean[][] bits
	 * @return boolean[][]
	 */ 
	public boolean[][] nRotateBitsReverse( int n, boolean[][] bits ) {
		boolean[][] rotate_bits = new boolean[ n ][ n ];
		for ( int i=0; i < n ; i++ ) {
			for ( int j=0; j < n; j++ ) {
				rotate_bits[ i ][ j ] = bits[ j ][ n - 1 - i ];			
			}
		}
		return rotate_bits;
	}

// --- Byte To Bits ---	
	/**
	 * n Bytes To Bits
	 * @param int n
	 * @param byte[] bytes
	 * @return boolean[][]
	 */     
	public boolean[][] nBytesToBits( int n, byte[] bytes ) {
		boolean[][] bits = new boolean[ n ][ NUM_8BITS ];
		for ( int i=0; i < n ; i++ ) {
			bits[ i ] = byteToBits( bytes[ i ] );
		}
		return bits;
	}
		
 	/**
	 * byte To 8Bits
	 * @param byte b
	 * @return boolean[]
	 */	  	
	public boolean[] byteToBits( byte b ) {
		boolean[] bits = new boolean[ NUM_8BITS ]; 
		int work = byteToUnsignedInteger( b );
		for ( int i=0; i < NUM_8BITS ; i++ ) {
			boolean bit = false;
			// if LSB is 1
			if ( (work & 0x01) == 1 ) {
				bit = true;
			}
			bits[ i ] = bit;
			work = work >> 1;
		}
		return bits;
	}
	
 	/**
	 * bytes To 8Bits
	 * @param byte b
	 * @return boolean[]
	 */	  	
	public boolean[] byteToReverseBits( byte b ) {
		boolean[] bits = new boolean[ NUM_8BITS ]; 
		int work = byteToUnsignedInteger( b );
		for ( int i=0; i < NUM_8BITS ; i++ ) {
			boolean bit = false;
			// if LSB is 1
			if ( (work & 0x01) == 1 ) {
				bit = true;
			}
			// LSB -> 7 bit : MSB -> 0 bit
			bits[ NUM_8BITS - 1 - i ] = bit;
			work = work >> 1;
		}
		return bits;
	}	

// --- Bits To Byte ---
	/**
	 * n x 8bits To bytes
	 * @param int n
	 * @param boolean[][]
	 * @return byte[]
	 */ 
	public byte[] nBitsToBytes( int n, boolean[][] bits ) {
		byte[] bytes = new byte[ n ];
		for ( int i=0; i < n ; i++ ) {
			bytes[ i ] = bitsToByte( bits[ i ] );
		}
		return bytes;
	}
	
	/**
	 * 8bits To int
	 * @param boolean[] bits
	 * @return byte
	 */ 
	public byte bitsToByte( boolean[] bits ) { 
		return (byte) bitsToInt( bits );	
	}
		
	/**
	 * reverse 8bits To int
	 * @param boolean[] bits
	 * @return byte
	 */ 
	public byte reverseBitsToByte( boolean[] bits ) { 
		return (byte) reverseBitsToInt( bits );	
	}
	
	/**
	 * 8bits To int
	 * @param boolean[] bits
	 * @return int
	 */ 
	public int bitsToInt( boolean[] bits ) { 
		int ret = 0;
		for ( int i=0; i < NUM_8BITS ; i++ ) {
			if ( bits[i] ) {
				ret += ( 0x01 << i );
			}
		}
		return ret;	
	}
	
	/**
	 * reverse 8bits To int
	 * @param boolean[] bits
	 * @return int
	 */ 
	public int reverseBitsToInt( boolean[] bits ) { 
		int ret = 0;
		for ( int i=0; i < NUM_8BITS ; i++ ) {
			if ( bits[i] ) {
				ret += ( 0x80 >> i );
			}
		}
		return ret;	
	}

// --- formated string ---
	/**
	 * byte to formated string.
	 * convert the characters that can be displayed , 
	 * because byte is binary
	 * @param byte b
	 * @param String prefix
	 * @param String postfix
	 * @return String
	 */
	public String nByteToFormatedString( int n, byte[] bytes, String prefix, String postfix ) { 
		String str = "";
		for ( int i = 0; i < n; i++ ) {
			str += byteToFormatedString( bytes[i], prefix, postfix );
    	}
		return str;
	}
	
	/**
	 * byte to formated string.
	 * convert the characters that can be displayed , 
	 * because byte is binary
	 * @param byte b
	 * @param String prefix
	 * @param String postfix
	 * @return String
	 */
	public String byteToFormatedString( byte b, String prefix, String postfix ) { 
		int i = byteToUnsignedInteger( b );
		String str = "";
		// if asccii character, chage asccii style
		// space 0x20 , tilde 0x7e
		if (( i >= 0x20 )&&( i <= 0x7e )) {
			str = byteToCharString( b );
		// otherwise, chage hex style
		} else {
			str = prefix + byteToHexString( b ) + postfix;
		}
		return str;
	}
	
	/**
	 * byte To unsigned Integer
	 * @param byte b
	 * @return int
	 */ 	
	public int byteToUnsignedInteger( byte b ) { 
		int i = b & 0xff;
		return i;
	}

// --- Byte To HexString ---
	/**
	 * Bytes To HexString
	 * @param byte[] bytes
	 */ 
	public String bytesToHexString( byte[] bytes ) {
		if ( bytes == null ) return "null";
		int length = bytes.length;
		if ( length == 0 ) return "empty";
		return nByteToHexString( length, bytes, " " );
	}
	
	/**
	 * n Bytes To HexString
	 * @param int n
	 * @param byte[] bytes
	 * @param String glue
	 */ 
	public String nByteToHexString( int n, byte[] bytes, String glue ) {
		String str = "";
		for ( int i=0; i < n ; i++ ) {
			str += byteToHexString( bytes[ i ] );
			str += glue;
		}
		return str;
	}
						
	/**
	 * byte To HexString
	 * @param byte b
	 * @return String
	 */ 
	public String byteToHexString( byte b ) {
		return String.format( "%02X", b );
	}

	/**
	 * byte To CharString
	 * @param byte b
	 * @return String
	 */ 
	public String byteToCharString( byte b ) {
		byte[] bytes = {b};
		return bytesToString( bytes );
	}

	/**
	 * byte To CharString
	 * @param byte b
	 * @return String
	 */ 
	public String bytesToString( byte[] bytes ) {
		String str = "";
		try {
			str = new String( bytes, CHARNAME_ASCII );
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
		}
		return str;
	}
		
	/**
	 * strByte To byte
	 * @param String str
	 * @return byte
	 */ 	
	public byte strByteToByte( String str ) { 
		return (byte) strByteToInt( str );
	}
		
	/**
	 * strByte To Int
	 * @param String str
	 * @return int
	 */ 	
	public int strByteToInt( String str ) { 
		int ret = 0;
		if ( str == null ) {
			log_d( "strByteToInt null "  ); 
			return ret;
		}
		if ( ( str.length() % 2 ) != 0 ) {
			log_d( "strByteToInt : not multiple of 2: " + str  ); 
			return ret;
		}
		try {
			ret = Integer.parseInt( str, RADIX_HEX );
		} catch ( NumberFormatException e ) {
			e.printStackTrace(); 
		}
		return ret;
	}

// --- debug ---
	/**
	 * debug Bytes
	 * @param int n
	 * @param byte[] bytes
	 */ 
	public void debugBytes( int n, byte[] bytes ) {
		String str = nByteToHexString( n, bytes, " " );
		log_d( str );
	}

	/**
	 * debug Bits
	 * @param int n
	 * @param boolean[][] bits
	 */ 
	public void debugBits( int n, boolean[][] bits ) {
		for ( int i=0; i < n ; i++ ) {
			String str = bitsToStr( bits[ i ] );
			log_d( str );
		}
	}

	/**
	 * debug Bits Reverse
	 * @param int n
	 * @param boolean[][] bits
	 */ 
	public void debugBitsReverse( int n, boolean[][] bits ) {
		for ( int i=0; i < n ; i++ ) {
			String str = bitsToStrReverse( bits[ i ] );
			log_d( str );
		}
	}

	/**
	 * bits To Str
	 * @param boolean[][] bits
	 * @return String
	 */ 
	public String bitsToStr( boolean[] bits ) {
		String str = "";
		for ( int i=0; i < NUM_8BITS; i++ ) {
			if ( bits[ i ] ) {
				str += CHAR_ONE;
			} else {
				str += CHAR_ZERO;	
			}
			str += CHAR_SPACE;	
		}
		return str;
	}

	/**
	 * bits To Str Reverse
	 * @param boolean[][] bits
	 * @return String
	 */ 
	public String bitsToStrReverse( boolean[] bits ) {
		String str = "";
		for ( int i=0; i < NUM_8BITS; i++ ) {
			if ( bits[ NUM_8BITS - 1 - i ] ) {
				str += CHAR_ONE;
			} else {
				str += CHAR_ZERO;	
			}
			str += CHAR_SPACE;	
		}
		return str;
	}
	    					
	/**
	 * write log
	 * @param String msg
	 */ 
	protected void log_d( String msg ) {
	    if (D) Log.d( TAG, TAG_SUB + msg );
	}    
}
