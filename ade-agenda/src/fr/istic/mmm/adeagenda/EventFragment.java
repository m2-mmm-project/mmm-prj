package fr.istic.mmm.adeagenda;

import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.istic.mmm.adeagenda.model.Event;

public class EventFragment extends Fragment {

	private static final String TAG = EventFragment.class.getSimpleName();
	
	public static final String EVENT_NAME = "name";
	public static final String EVENT_START = "start";
	public static final String EVENT_END = "end";
	public static final String EVENT_PLACE = "place";
	public static final String EVENT_DESCRIPTION = "description";

	private Context context;
	private Event event;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated properly
		View rootView = inflater.inflate(R.layout.fragment_event, container, false);

		this.context = this.getActivity().getApplicationContext();

		// retrieving args set from adapter
		Bundle args = getArguments();
		String name = args.getString(EVENT_NAME);
		long start = args.getLong(EVENT_START);
		long end = args.getLong(EVENT_END);
		String place = args.getString(EVENT_PLACE);
		String description = args.getString(EVENT_DESCRIPTION);
		event = new Event(name, new Date(start), new Date(end), place, description);

		return rootView;
	}
}
