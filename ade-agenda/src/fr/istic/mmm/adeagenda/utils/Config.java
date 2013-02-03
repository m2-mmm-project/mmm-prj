package fr.istic.mmm.adeagenda.utils;

import android.os.Environment;

public abstract class Config {

	public static final String FILE_NAME = "ADECal.ics";
	public static final String DOWNLOAD_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/ADECalendar";
}
