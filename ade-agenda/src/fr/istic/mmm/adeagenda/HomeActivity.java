package fr.istic.mmm.adeagenda;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import fr.istic.mmm.adeagenda.db.GPSPositionDB;
import fr.istic.mmm.adeagenda.service.AlarmReceiver;
import fr.istic.mmm.adeagenda.service.UpdateService;
import fr.istic.mmm.adeagenda.utils.Config;

public class HomeActivity extends Activity {

	private static final String TAG = HomeActivity.class.getSimpleName();
	public static Context APPLICATION_CONTEXT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		APPLICATION_CONTEXT = this.getApplicationContext();

		SharedPreferences settings = getSharedPreferences(Config.ADE_PREF, 0);
		boolean configIsDone = settings.getBoolean(Config.PREF_CONFIG_DONE, false);

		// Mise en place du service de mise a jour du fichier ics
		startService(new Intent(getApplicationContext(), UpdateService.class));

		if (!configIsDone) {
			Log.v(TAG, "Config not done");
			Toast.makeText(getApplicationContext(), "Vous devriez configurer votre agenda", Toast.LENGTH_LONG).show();
			
		} else {
			Toast.makeText(getApplicationContext(), "Votre agenda est configuré", Toast.LENGTH_SHORT).show();
		}
	}

	
	/**
	 * Called when Config btn is clicked
	 * 
	 * @param view
	 */
	public void onClickDebug(View view) {
		Log.v(TAG, "Setting up alarm for notification");

		// get a Calendar object with current time
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 10);

		Intent intent = new Intent(this, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 13454, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + 1000, pendingIntent);

		Log.v(TAG, "Alarm setted");
	}
	
	/**
	 * Called when Config btn is clicked
	 * 
	 * @param view
	 */
	public void onClickConfig(View view) {
		startActivity(new Intent(getApplicationContext(), ConfigActivity.class));
	}

	/**
	 * Called when Agenda btn is clicked
	 * 
	 * @param view
	 */
	public void onClickAgenda(View view) {
		startActivity(new Intent(getApplicationContext(), AgendaPagerActivity.class));
	}

	/**
	 * Called when Map btn is clickedDESC
	 * 
	 * @param view
	 */
	public void onClickMap(View view) {
		String name = "ADEAgenda.sqlite";
		String androidDBPath = "/data/"
				+ this.getPackageName() + "/databases/";
		File androidDB = new File(androidDBPath, name);

		if (androidDB.exists())
			Log.v("DbManager", "Andro DB Exist");
		else
			Log.v("DbManager", "Andro DB not exist");
		
		GPSPositionDB db2 = new GPSPositionDB(this);

		db2.add("B02B-E212", 48.115888, -1.637961);

		Intent intent = new Intent(getApplicationContext(), MapActivity.class);
		intent.putExtra(Config.MAP_POSITION_LAT, 48.115671);
		intent.putExtra(Config.MAP_POSITION_LNG, -1.63813);
		startActivity(intent);
	}
}
