package fr.istic.mmm.adeagenda.task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import fr.istic.mmm.adeagenda.ConfigActivity;
import fr.istic.mmm.adeagenda.HomeActivity;
import fr.istic.mmm.adeagenda.calendar.CalendarReader;
import fr.istic.mmm.adeagenda.db.AgendaDb;
import fr.istic.mmm.adeagenda.db.DbManager;
import fr.istic.mmm.adeagenda.model.Event;
import fr.istic.mmm.adeagenda.utils.Config;

public class ParseCalendarTask extends AsyncTask<URL, Integer, Boolean> {
	
	private static final String TAG = ConfigActivity.class.getSimpleName();
	
	@Override
	protected Boolean doInBackground(final URL... urls) {
		Log.v(TAG, "Parsing calendar ...");
		
		// Check if the file exists
		FileInputStream is = null;
		try {
			is = new FileInputStream(Config.DOWNLOAD_DIRECTORY+"/"+Config.FILE_NAME);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "File not found :(");
			return false;
		}
		
		CalendarReader reader = new CalendarReader(is);
		
		List<Event> events = reader.allEvents();
		Log.v(TAG, "Parsing complete : " + events.size() + " events found");
		Log.v(TAG, "Updating database ...");
		
		AgendaDb db = new AgendaDb(HomeActivity.APPLICATION_CONTEXT);
		db.clear();
		for (Event event : events) {
			db.add(event);
		}

		Log.v(TAG, "Update complete ! Realy ?");
		return true;

	}

}
