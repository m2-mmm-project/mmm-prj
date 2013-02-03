package fr.istic.mmm.adeagenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockActivity;

public class HomeActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
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
		startActivity(new Intent(getApplicationContext(), AgendaPagerActivity.class));
	}
	
	/**
	 * Called when Map btn is clicked
	 * @param view
	 */
	public void onClickMap(View view) {
		startActivity(new Intent(getApplicationContext(), MyMapActivity.class));
	}
}
