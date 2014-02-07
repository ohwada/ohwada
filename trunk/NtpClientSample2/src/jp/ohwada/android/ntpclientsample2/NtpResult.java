package jp.ohwada.android.ntpclientsample2;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ntp.NtpUtils;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

/**
 * NtpResult
 */
public class NtpResult {

	private final static String LF = "\n" ; 
	
	private final static SimpleDateFormat mDateFormat = 
		new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss SSS" );
    private final static NumberFormat mNumberFormat = 
    	new java.text.DecimalFormat( "0.00" );
		
	/**
	 * === Constractor ===
	 */
	public NtpResult( ) {
		// duumy
	}

	/**
	 * getOffset
	 * @param TimeInfo info
	 * @return long
	 */
	public static long getOffset( TimeInfo info ) {  	
		info.computeDetails();
		return info.getOffset();
	}

	/**
	 * getOffset
	 * @param TimeInfo info
	 * @return String
	 */	
	public static String getDetail( TimeInfo info ) {    								
		info.computeDetails();
		long offset = info.getOffset();
		long delay = info.getDelay();
		NtpV3Packet message = info.getMessage();
    	String str = "";
		str += "Stratum: " + message.getStratum() ;
		str += " ( " + getType( message ) + " ) "+ LF;
        str += "Leap: " + message.getLeapIndicator() + LF;
        str += "Version: " + message.getVersion() + LF;
        str += "Precision: " + message.getPrecision() + LF;
		str += "Mode: " + message.getModeName(); 
		str += " (" + message.getMode() + ")" + LF;
        str += "Poll: " + getPoll( message ) + " seconds" + LF;
		str += "Root delay: " + getRootDelay( message ) + LF;
        str += "Root dispersion (ms): " + getRootDispersion( message ) + LF;
        str += "Reference Identifier: " + getAddr( message ) + LF;
		str += "Reference time: " + getReference( message ) + LF;
		str += "Originate time: " + getOriginate( message ) + LF;
		str += "Receive time: " + getReceive( message ) + LF;
		str += "Transmit time: " + getTransmit( message ) + LF;
        str += "Destination time: " + getDestination( info ) +LF;
        str += "Roundtrip delay (ms): " + delay + LF;
        str += "Clock offset(ms) (ms): " + offset + LF;
		return str;       
    }	
		
	/**
	 * poll value typically btwn MINPOLL (4) and MAXPOLL (14)
	 * @param NtpV3Packet message 
	 * @return int
	 */
	private static int getPoll( NtpV3Packet message ) {
        int poll = message.getPoll();
        int pow = (poll <= 0 ) ? 1 : (int) Math.pow( 2, poll );
        return pow;
	}        

	/**
	 * getAddr
	 * @param NtpV3Packet message 
	 * @return String
	 */
	private static String getAddr( NtpV3Packet message ) {
		return NtpUtils.getHostAddress( 
			message.getReferenceId() );
	}

	/**
	 * getRootDispersion
	 * @param NtpV3Packet message 
	 * @return String
	 */                
	private static String getRootDispersion( NtpV3Packet message ) {
        return mNumberFormat.format( 
        	message.getRootDispersionInMillisDouble() );
	}

	/**
	 * getRootDelay
	 * @param NtpV3Packet message 
	 * @return String
	 */ 
	private static String getRootDelay( NtpV3Packet message ) {	 
        return mNumberFormat.format( 
        	message.getRootDelayInMillisDouble() );
	}

	/**
	 * getReference
	 * @param NtpV3Packet message 
	 * @return String
	 */ 
	private static String getReference( NtpV3Packet message ) {
		return formatTime( 
			message.getReferenceTimeStamp() );
	}

	/**
	 * Originate Time is time request sent by client (t1)
	 * @param NtpV3Packet message 
	 * @return String
	 */ 	        
	private static String getOriginate( NtpV3Packet message ) {
		return formatTime( 
			message.getOriginateTimeStamp() );
	}

	/**
	 * Receive Time is time request received by server (t2)
	 * @param NtpV3Packet message 
	 * @return String
	 */ 	        
    private static String getReceive( NtpV3Packet message ) {
		return formatTime( 
			message.getReceiveTimeStamp() );
	}        

	/**
	 * Transmit time is time reply sent by server (t3)
	 * @param NtpV3Packet message 
	 * @return String
	 */         
    private static String getTransmit( NtpV3Packet message ) {
		return formatTime( 
			message.getTransmitTimeStamp() );
	}

	/**
	 * Destination time is time reply received by client (t4)
	 * @param TimeInfo info
	 * @return String
	 */ 
    private static String getDestination( TimeInfo info ) {
		return formatTime( 
			info.getReturnTime() );
	}

	/**
	 * getType
	 * @param NtpV3Packet message
	 * @return String
	 */ 	        	                
    private static String getType( NtpV3Packet message ) {
    	return getType( message.getStratum() );
    }
    	
	/**
	 * getType
	 * @param int stratum
	 * @return String
	 */ 	        	                
    private static String getType( int stratum ) {
        String type = "";
        if (stratum <= 0) {
            type = "Unspecified or Unavailable";
        } else if (stratum == 1) {
            type = "Primary Reference; e.g., GPS";
        } else {
            type = "Secondary Reference; e.g. via NTP or SNTP";
        }
        return type;
	}

	/*
	 * formatTime
	 * @param TimeStamp time
	 * @return String
	 */
    private static String formatTime( TimeStamp time ) {
		return formatTime( time.getTime() );
	}

	/*
	 * formatTime
	 * @param long time
	 * @return String
	 */
	private static String formatTime( long time ) {
		Date date = new Date( time );
		String str = mDateFormat.format( date );
		return str;
	}

}
