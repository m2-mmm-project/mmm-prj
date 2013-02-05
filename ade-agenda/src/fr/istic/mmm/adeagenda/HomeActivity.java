package fr.istic.mmm.adeagenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
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
		Intent intent = new Intent(getApplicationContext(), MapActivity.class);
		intent.putExtra(Config.MAP_POSITION_LAT, Config.CENTER_ISTIC.latitude);
		intent.putExtra(Config.MAP_POSITION_LNG, Config.CENTER_ISTIC.longitude);
		intent.putExtra(Config.MAP_PLACE_NAME, "ISTIC");
	}
}
