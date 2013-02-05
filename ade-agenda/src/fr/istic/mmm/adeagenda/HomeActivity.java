package fr.istic.mmm.adeagenda;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import fr.istic.mmm.adeagenda.db.AgendaDb;
import fr.istic.mmm.adeagenda.db.GPSPositionDB;
import fr.istic.mmm.adeagenda.model.Event;
import fr.istic.mmm.adeagenda.service.UpdateService;
import fr.istic.mmm.adeagenda.utils.Config;

public class HomeActivity extends SherlockActivity {

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
	public void onClickConfig(View view) {
		startActivity(new Intent(getApplicationContext(), ConfigActivity.class));
	}

	/**
	 * Called when Agenda btn is clicked
	 * 
	 * @param view
	 */
	public void onClickAgenda(View view) {
		startActivity(new Intent(getApplicationContext(),
				AgendaPagerActivity.class));
	}

	/**
	 * Called when Map btn is clickedDESC
	 * 
	 * @param view
	 */
	public void onClickMap(View view) {
		AgendaDb db = new AgendaDb(this);
//		db.reset();
//		db.add(new Event("name1", DateFormater.getSQLDate("2013-02-04 08:00"), DateFormater.getSQLDate("2013-02-04 10:00"), "place1", "description"));
//		db.add(new Event("name2", DateFormater.getSQLDate("2013-02-05 08:00"), DateFormater.getSQLDate("2013-02-05 10:00"), "place2", "description"));
//		db.add(new Event("name3", DateFormater.getSQLDate("2013-02-05 14:00"), DateFormater.getSQLDate("2013-02-05 16:00"), "place3", "description"));
//		db.add(new Event("name4", DateFormater.getSQLDate("2013-02-06 08:00"), DateFormater.getSQLDate("2013-02-06 10:00"), "place4", "description"));
		List<Event> e = db.getEventByDay(new Date());
//
		GPSPositionDB db2 = new GPSPositionDB(this);
		db2.add("LOCATION:B02B-E212\n", 48.115888,-1.637961);
		db2.add("LOCATION:B02B-E105", 48.116181,-1.638119);
		db2.add("LOCATION:B02B-E210", 48.115965,-1.637945);
//		db2.add("place4", 4.1, 4.2);
//		
		Log.v(TAG, "read");
		for (Event ev : e) {
			Log.v(TAG, ev.toString());
			Log.v(TAG, db2.getPositionByName(ev.getPlace()).toString());
		}

		 Intent intent = new Intent(getApplicationContext(),
		 MapActivity.class);
		 intent.putExtra(Config.MAP_POSITION_LAT,48.115671);
		 intent.putExtra(Config.MAP_POSITION_LNG,-1.63813);
		 startActivity(intent);
	}
}
