package fr.istic.mmm.adeagenda;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.fortuna.ical4j.model.DateTime;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import fr.istic.mmm.adeagenda.db.AgendaDb;
import fr.istic.mmm.adeagenda.model.Event;
import fr.istic.mmm.adeagenda.utils.DateFormater;

public class AgendaFragment extends Fragment {

	private static final String TAG = AgendaFragment.class.getSimpleName();
	private static final String ITEM_NAME = "name";
	private static final String ITEM_DATE = "date";
	private static final String ITEM_START = "start";
	private static final String ITEM_END = "end";
	private static final String ITEM_PLACE = "place";

	public static final String ARG_DATE = "date";

	private List<Event> events;
	private Date date;
	private ListView eventList;
	private SimpleAdapter eventAdapter;
	private List<HashMap<String, String>> data;
	private String from[] = {
			ITEM_NAME,
			ITEM_DATE,
			ITEM_START,
			ITEM_END,
			ITEM_PLACE
	};
	private int to[] = {
			R.id.event_name,
			R.id.event_date,
			R.id.event_start,
			R.id.event_end,
			R.id.event_place
	};
	private LinearLayout progress;
	private TextView emptyList;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated properly
		View rootView = inflater.inflate(R.layout.fragment_agenda, container,
				false);

		this.context = this.getActivity().getApplicationContext();

		// retrieving args set from adapter
		Bundle args = getArguments();
		// definition de la date associée à cet onglet
		date = new Date(args.getLong(ARG_DATE));

		eventList = (ListView) rootView.findViewById(R.id.eventList);
		eventList.setEmptyView(rootView.findViewById(R.id.emptyList));
		eventList.setOnItemClickListener(eventListListener);

		data = new ArrayList<HashMap<String, String>>();
		eventAdapter = new SimpleAdapter(getActivity(), data,
				R.layout.event_list_item, from, to);
		eventList.setAdapter(eventAdapter);

		progress = (LinearLayout) rootView.findViewById(R.id.progress);
		emptyList = (TextView) rootView.findViewById(R.id.emptyList);
		emptyList.setVisibility(View.GONE);
		
		createCalendar();

		return rootView;
	}

	private void createCalendar() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				AgendaDb db = new AgendaDb(context);
				DateTime wantedDate = new DateTime(date);
				events = db.getEventByDay(wantedDate);
				
				if (events.isEmpty()) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progress.setVisibility(View.GONE);
							emptyList.setVisibility(View.VISIBLE);
						}
					});
				} else {
					for (Event e : events) {
						final HashMap<String, String> item = new HashMap<String, String>();
						item.put(ITEM_NAME, e.getName());
						item.put(ITEM_DATE, DateFormater.getDateString(e.getStart()));
						item.put(ITEM_START, getString(R.string.event_start, DateFormater.getTimeString(e.getStart())));
						item.put(ITEM_END, getString(R.string.event_end, DateFormater.getTimeString(e.getEnd())));
						item.put(ITEM_PLACE, e.getPlace());
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								data.add(item);
								progress.setVisibility(View.GONE);
								eventAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			}
		}).start();
	}
	
	private OnItemClickListener eventListListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adpt, View view, int pos, long id) {
			Event e = events.get(pos);
			
			Bundle eventInfo = new Bundle();
			eventInfo.putString(EventActivity.EVENT_NAME, e.getName());
			eventInfo.putLong(EventActivity.EVENT_START, e.getStart().getTime());
			eventInfo.putLong(EventActivity.EVENT_END, e.getEnd().getTime());
			eventInfo.putString(EventActivity.EVENT_PLACE, e.getPlace());
			eventInfo.putString(EventActivity.EVENT_DESCRIPTION, e.getDescription());
			
			Intent fragIntent = new Intent(getActivity(), EventActivity.class);
			fragIntent.putExtras(eventInfo);
			startActivity(fragIntent);
		}
	};
}
