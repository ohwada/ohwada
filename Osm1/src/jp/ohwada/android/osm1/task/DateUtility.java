package jp.ohwada.android.osm1.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DateUtility
 */
public class DateUtility {

	public static final long TIME_MSEC_ONE_DAY = 24 * 60 * 60 * 1000; // 1 day
	private static final long TIME_MSEC_ONE_MONTH = 30 * TIME_MSEC_ONE_DAY ;
	
	private SimpleDateFormat mSdfDate = null;
	private SimpleDateFormat mSdfLabel = null;
				
    /**
     * === constractor ===	 
     */
	public DateUtility() {
		mSdfDate = new SimpleDateFormat( "yyyy-MM-dd" );
		mSdfLabel = new SimpleDateFormat( "yyyyMMdd" );
	}

    /**
     * create  
	 * @param int year
	 * @param int month
	 * @param int day
	 * @return Date	 
     */
	public Date createDate( int year, int month, int day ) {
		Calendar cal = Calendar.getInstance();
		cal.set( year, month, day );
		return cal.getTime();
	}

    /**
     * getDateToday   
	 * @return Date	 
     */
	public Date getDateToday() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
 	}

    /**
     * getTimeToday   
	 * @return long	 
     */
	public long getTimeToday() {
		Calendar cal= Calendar.getInstance();
		Date date = cal.getTime();
		return date.getTime();
	}

    /**
     * addMonth
     * @param Date date
     * @param int month     
	 * @return Date	 
     */
	public Date addMonth( Date date, int month ) {
		long time = date.getTime() + month * TIME_MSEC_ONE_MONTH;
		Date d = new Date( time );
		return d;
 	}

    /**
     * addDay
     * @param Date date
     * @param int day  
	 * @return Date	 
     */
	public Date addDay( Date date, int day ) {
		long time = date.getTime() + day * TIME_MSEC_ONE_DAY;
		Date d = new Date( time );
		return d;
 	}
		                	
    /**
     * formatDate fot title
	 * @param Date date	
	 * @return String	 
     */
	public String formatDate( Date date ) {
		return mSdfDate.format( date );
	}	
	
	/**
     * formatLabel for file name
	 * @param Date date	
	 * @return String	 
     */
	public String formatLabel( Date date ) {
		return mSdfLabel.format( date );
	}	  	
}
