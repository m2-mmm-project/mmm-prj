package fr.istic.mmm.adeagenda;

import java.io.File;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import fr.istic.mmm.adeagenda.db.GPSPositionDB;
import fr.istic.mmm.adeagenda.model.Event;
import fr.istic.mmm.adeagenda.utils.Config;

public class EventActivity extends Activity {

	private static final String TAG = EventActivity.class.getSimpleName();
	
	public static final String EVENT_NAME = "name";
	public static final String EVENT_START = "start";
	public static final String EVENT_END = "end";
	public static final String EVENT_PLACE = "place";
	public static final String EVENT_DESCRIPTION = "description";

	private Context context;
	private Event event;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		// retrieving args set from adapter
		Bundle args = getIntent().getExtras();
		String name = args.getString(EVENT_NAME);
		long start = args.getLong(EVENT_START);
		long end = args.getLong(EVENT_END);
		String place = args.getString(EVENT_PLACE);
		String description = args.getString(EVENT_DESCRIPTION);
		event = new Event(name, new Date(start), new Date(end), place, description);
		
		((TextView) findViewById(R.id.event_name)).setText(event.getName());
		((TextView) findViewById(R.id.event_date)).setText("Demain");
		((TextView) findViewById(R.id.event_start)).setText(" de 10h00");
		((TextView) findViewById(R.id.event_end)).setText(" à 12h15");
		((TextView) findViewById(R.id.event_place)).setText(event.getPlace());
		((TextView) findViewById(R.id.event_desc)).setText(event.getDescription());
	}
	
	/**
	 * Called when the button to see on map has been clicked
	 * @param v
	 */
	public void onClickSeeOnMap(View v) {
		GPSPositionDB positionDb = new GPSPositionDB(this);
		LatLng position = positionDb.getPositionByName(event.getPlace());

		Intent intent = new Intent(getApplicationContext(), MapActivity.class);
		intent.putExtra(Config.MAP_POSITION_LAT, position.latitude);
		intent.putExtra(Config.MAP_POSITION_LNG, position.longitude);
		startActivity(intent);
	}

}
