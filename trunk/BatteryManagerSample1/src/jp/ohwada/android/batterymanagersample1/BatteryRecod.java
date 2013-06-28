package jp.ohwada.android.batterymanagersample1;

import android.content.Intent;
import android.os.BatteryManager;

/**
 * BatteryRecod
 */
public class BatteryRecod {

	private final static String LF = "\n";

	public int status = 0;
	public int health = 0;
	public int level = 0;
	public int scale = 0;
	public int plugged = 0;
	public int voltage = 0;
	public int temperature = 0;
	public int icon_small = 0;
	public boolean present = false;
	public String technology = "";
	public String str_status = "";
	public String str_health = "";
	public String str_plugged = "";

	/**
	 * constractor
	 */
	public BatteryRecod() {
		// dummy
	}

	/**
	 * constractor
	 */	
	public BatteryRecod( Intent intent ) {		
		status = intent.getIntExtra( BatteryManager.EXTRA_STATUS, 0 );
		health = intent.getIntExtra( BatteryManager.EXTRA_HEALTH, 0 );
		level = intent.getIntExtra( BatteryManager.EXTRA_LEVEL, 0 );
		scale = intent.getIntExtra( BatteryManager.EXTRA_SCALE, 0 );
		plugged = intent.getIntExtra( BatteryManager.EXTRA_PLUGGED, 0 );
		voltage = intent.getIntExtra( BatteryManager.EXTRA_VOLTAGE, 0 );
		temperature = intent.getIntExtra( BatteryManager.EXTRA_TEMPERATURE, 0 );
		icon_small = intent.getIntExtra( BatteryManager.EXTRA_ICON_SMALL, 0 );
		present = intent.getBooleanExtra( BatteryManager.EXTRA_PRESENT, false );
		technology = intent.getStringExtra( BatteryManager.EXTRA_TECHNOLOGY );
                
		switch (status) {
			case BatteryManager.BATTERY_STATUS_UNKNOWN:
				str_status = "unknown";
				break;
			case BatteryManager.BATTERY_STATUS_CHARGING:
				str_status = "charging";
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
				str_status = "discharging";
				break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				str_status = "not charging";
				break;
			case BatteryManager.BATTERY_STATUS_FULL:
				str_status = "full";
				break;
		}

		switch (health) {
			case BatteryManager.BATTERY_HEALTH_UNKNOWN:
				str_health = "unknown";
				break;
			case BatteryManager.BATTERY_HEALTH_GOOD:
				str_health = "good";
				break;
			case BatteryManager.BATTERY_HEALTH_OVERHEAT:
				str_health = "overheat";
				break;
			case BatteryManager.BATTERY_HEALTH_DEAD:
				str_health = "dead";
				break;
			case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
				str_health = "voltage";
				break;
			case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
				str_health = "unspecified failure";
				break;
		}
                
		switch (plugged) {
			case BatteryManager.BATTERY_PLUGGED_AC:
				str_plugged = "ac";
				break;
			case BatteryManager.BATTERY_PLUGGED_USB:
				str_plugged = "usb";
			break;
		}
	}

	/**
	 * toString
	 * @return String 
	 */
	public String toString() {    
		String text = "";
		text += "status: " + str_status+ LF;
		text += "health: " + str_health + LF;
		text += "plugged: " + str_plugged + LF;
		text += "technology: " + technology + LF;
		text += "present: " + present + LF;
		text += "level: " + level + LF;
		text += "scale: " + scale + LF;
		text += "voltage: " + voltage + LF;
		text += "temperature: " + temperature + LF;
		return text;
	}
   
}
