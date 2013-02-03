package fr.istic.mmm.adeagenda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.fortuna.ical4j.model.DateTime;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import fr.istic.mmm.adeagenda.calendar.CalendarReader;
import fr.istic.mmm.adeagenda.model.Event;
import fr.istic.mmm.adeagenda.utils.Config;

public class AgendaFragment extends Fragment {
	
	private static final String TAG = AgendaFragment.class.getSimpleName();
	private static final String ITEM_NAME = "name";
	private static final String ITEM_DATE = "date";
	private static final String ITEM_START = "start";
	private static final String ITEM_END = "end";
	
	public static final String ARG_DATE = "date";

	private Date date;
	private ListView eventList;
	private SimpleAdapter eventAdapter;
	private List<HashMap<String, String>> data;
	private String from[] = {ITEM_NAME, ITEM_DATE, ITEM_START, ITEM_END};
	private int to[] = {R.id.event_name, R.id.event_date, R.id.event_start, R.id.event_end};
	private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated properly
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);
        
        // retrieving args set from adapter
        Bundle args = getArguments();
        // definition de la date associée à cet onglet
        date = new Date(args.getLong(ARG_DATE));
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        android.util.Log.d(TAG, "WANTED DATE: "+df.format(date));

		eventList = (ListView) rootView.findViewById(R.id.eventList);
		eventList.setEmptyView(rootView.findViewById(R.id.emptyList));
		
		data = new ArrayList<HashMap<String, String>>();
		eventAdapter = new SimpleAdapter(getActivity(), data, R.layout.event_list_item, from, to);
		eventList.setAdapter(eventAdapter);
		
		try {
			FileInputStream is = new FileInputStream(Config.DOWNLOAD_DIRECTORY+"/"+Config.FILE_NAME);
			progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
			
			createCalendar(is);
			
		} catch (FileNotFoundException e) {
			showToast(getString(R.string.error_fnf, Config.FILE_NAME));
			e.printStackTrace();
		}
        
        return rootView;
    }
    
	private void createCalendar(final FileInputStream is) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				CalendarReader reader = new CalendarReader(is);
				DateTime wantedDate = new DateTime(date);
				List<Event> events = reader.eventsOfTheDay(wantedDate);

				if (events.isEmpty()) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							((TextView) getActivity().findViewById(R.id.emptyList)).setText(getActivity().getString(R.string.empty_agenda));
							progressBar.setVisibility(View.GONE);
						}
					});
					
					
				} else {
					DateFormat dfTime = DateFormat.getTimeInstance(DateFormat.SHORT);
					DateFormat dfDate = DateFormat.getDateInstance(DateFormat.SHORT);
					
					for (Event e : events) {
						final HashMap<String, String> item = new HashMap<String, String>();
						item.put(ITEM_NAME, e.getName());
						item.put(ITEM_DATE, dfDate.format(e.getStart()));
						item.put(ITEM_START, getString(R.string.event_start, dfTime.format(e.getStart())));
						item.put(ITEM_END, getString(R.string.event_end, dfTime.format(e.getEnd())));
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								data.add(item);
								progressBar.setVisibility(View.GONE);
								eventAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			}
		}).start();
	}
	
	private void showToast(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}
}
