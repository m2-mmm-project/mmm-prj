package fr.istic.mmm.adeagenda;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import fr.istic.mmm.adeagenda.model.Event;

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
		setContentView(R.layout.fragment_event);
		// retrieving args set from adapter
		Bundle args = getIntent().getExtras();
		String name = args.getString(EVENT_NAME);
		long start = args.getLong(EVENT_START);
		long end = args.getLong(EVENT_END);
		String place = args.getString(EVENT_PLACE);
		String description = args.getString(EVENT_DESCRIPTION);
		event = new Event(name, new Date(start), new Date(end), place, description);
	}

}
