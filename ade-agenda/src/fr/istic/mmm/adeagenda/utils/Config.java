package fr.istic.mmm.adeagenda.utils;

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

	// Stockage
	public static final String FILE_NAME = "ADECal.ics";
	public static final String DOWNLOAD_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/ADECalendar";
	
	// Noms des paramètres des Bundles
	public static final String MAP_POSITION_LAT = "positionLat";
	public static final String MAP_POSITION_LNG = "positionLng";
}
