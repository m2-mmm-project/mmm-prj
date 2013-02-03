package fr.istic.mmm.adeagenda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockActivity;

import fr.istic.mmm.adeagenda.utils.Config;

public class HomeActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		

		SharedPreferences settings = getSharedPreferences(Config.ADE_PREF, 0);
		boolean configIsDone = settings.getBoolean(Config.PREF_CONFIG_DONE, false);
		
		if(!configIsDone){
			startActivity(new Intent(getApplicationContext(), ConfigActivity.class));
		}
		
	}
	
	/**
	 * Called when Config btn is clicked
	 * @param view
	 */
	public void onClickConfig(View view) {
		startActivity(new Intent(getApplicationContext(), ConfigActivity.class));
	}
	
	/**
	 * Called when Agenda btn is clicked
	 * @param view
	 */
	public void onClickAgenda(View view) {
		startActivity(new Intent(getApplicationContext(), AgendaActivity.class));
	}
	
	/**
	 * Called when Map btn is clicked
	 * @param view
	 */
	public void onClickMap(View view) {
		startActivity(new Intent(getApplicationContext(), MyMapActivity.class));
	}
}
