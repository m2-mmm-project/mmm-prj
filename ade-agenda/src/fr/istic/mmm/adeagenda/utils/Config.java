package fr.istic.mmm.adeagenda.utils;

import com.google.android.gms.maps.model.LatLng;

import android.os.Environment;

public abstract class Config {

	// Identifiant des champs dans les SharedPreferences
	public static final String ADE_PREF = "ADEPrefs";
	public static final String PREF_CONFIG_DONE = "isConfigDone";
	public static final String PREF_PROJECT_ID = "projectId";
	public static final String PREF_RESOURCES_ID = "resources";
	public static final String PREF_LOGIN = "login";
	public static final String PREF_PASSWORD = "password";
	public static final String PREF_START_DATE = "firstDate";
	public static final String PREF_END_DATE = "lastDate";
	public static final String PREF_ALARM_TIME = "alarmTime";
	public static final String PREF_ALARM_REC = "alarmRec";
	public static final String PREF_NOTIFICATION = "notifications";  
	

	// Stockage
	public static final String FILE_NAME = "ADECal.ics";
	public static final String DOWNLOAD_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/ADECalendar";
	
	// Noms des paramètres des Bundles
	public static final String MAP_POSITION_LAT = "positionLat";
	public static final String MAP_POSITION_LNG = "positionLng";
	public static final String MAP_PLACE_NAME = "positionName";
	
	// Position du centre de la map
	public static final LatLng CENTER_ISTIC = new LatLng(48.115671, -1.63813);
	
	// Période d'éxécution de l'UpdateService
	public static final long TASK_PERIOD = 60000*60;  // Toutes les heures
	public static final long NOTIF_TASK_PERIOD = 60000*1;
}
