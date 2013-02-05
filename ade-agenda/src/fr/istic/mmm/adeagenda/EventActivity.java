package fr.istic.mmm.adeagenda;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import fr.istic.mmm.adeagenda.db.GPSPositionDB;
import fr.istic.mmm.adeagenda.model.Event;
import fr.istic.mmm.adeagenda.utils.Config;
import fr.istic.mmm.adeagenda.utils.DateFormater;

public class EventActivity extends Activity {

	private static final String TAG = EventActivity.class.getSimpleName();
	
	public static final String EVENT_NAME = "name";
	public static final String EVENT_START = "start";
	public static final String EVENT_END = "end";
	public static final String EVENT_PLACE = "place";
	public static final String EVENT_DESCRIPTION = "description";

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
		((TextView) findViewById(R.id.event_date)).setText(DateFormater.getDateString(event.getStart()));
		((TextView) findViewById(R.id.event_start)).setText(getString(R.string.event_start, DateFormater.getTimeString(event.getStart())));
		((TextView) findViewById(R.id.event_end)).setText(getString(R.string.event_end, DateFormater.getTimeString(event.getEnd())));
		((TextView) findViewById(R.id.event_place)).setText(event.getPlace());
		((TextView) findViewById(R.id.event_desc)).setText(event.getDescription().replaceAll("\\\\n", "\n"));
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
