package fr.istic.mmm.adeagenda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import fr.istic.mmm.adeagenda.service.UpdateService;
import fr.istic.mmm.adeagenda.utils.Config;

public class HomeActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		SharedPreferences settings = getSharedPreferences(Config.ADE_PREF, 0);
		boolean configIsDone = settings.getBoolean(Config.PREF_CONFIG_DONE,
				false);

		// Mise en place du service de mise a jour du fichier ics
		startService(new Intent(getApplicationContext(), UpdateService.class));

		if (!configIsDone) {
			Log.v("HomeActivity", "Config not done");
			// startActivity(new Intent(getApplicationContext(),
			// ConfigActivity.class));
			Toast.makeText(getApplicationContext(),
					"Vous devriez configurer votre agenda", Toast.LENGTH_LONG)
					.show();
		} else {
			Toast.makeText(getApplicationContext(), "Config ok",
					Toast.LENGTH_SHORT).show();
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
	 * Called when Map btn is clicked
	 * 
	 * @param view
	 */
	public void onClickMap(View view) {
//		GPSPositionDB db = new GPSPositionDB(this);
//		LatLng position = db.getPositionByName("oo");
		 Intent intent = new Intent(getApplicationContext(),
		 MapActivity.class);
		 intent.putExtra(Config.MAP_POSITION_LAT,48.115671);
		 intent.putExtra(Config.MAP_POSITION_LNG,-1.63813);
		 startActivity(intent);
	}
}
