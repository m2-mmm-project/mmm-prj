package fr.istic.mmm.adeagenda;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import fr.istic.mmm.adeagenda.calendar.CalendarReader;
import fr.istic.mmm.adeagenda.model.Event;

public class HomeActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	@Override
	public boolean onTouchEvent(MotionEvent mevent) {
		// TODO Tests a l'arrache
		new Thread(new Runnable() {
			public void run() {
				try {
					// Chargement fichier ics
					InputStream in = getResources().getAssets().open("ADECal.ics");
					CalendarReader reader = new CalendarReader(in);
					showToast("Reading calendar...");
					List<Event> events = reader.allEvents();

					for (Event event : events) {
						Log.v("Calendar", " " + event.getName() + " " + event.getDescription());
					}
				} catch (IOException e) {
					showToast("Error " + e.getMessage());
					e.printStackTrace();
				}

			}
		}).start();
		return super.onTouchEvent(mevent);
	}

	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(HomeActivity.this, toast, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
}
