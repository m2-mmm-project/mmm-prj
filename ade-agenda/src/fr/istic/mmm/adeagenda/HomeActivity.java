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
	
	public void onClickConfig(View view) {
		startActivity(new Intent(getApplicationContext(), ConfigActivity.class));
	}
	
	public void onClickAgenda(View view) {
		// TODO : Faire un truc
	}
	
	public void onClickMap(View view) {
		startActivity(new Intent(getApplicationContext(), MapActivity.class));
	}
}
