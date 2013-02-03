package fr.istic.mmm.adeagenda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import fr.istic.mmm.adeagenda.calendar.CalendarReader;
import fr.istic.mmm.adeagenda.model.Event;
import fr.istic.mmm.adeagenda.utils.Config;

/**
 * @author leiko
 *
 */
public class AgendaActivity extends Activity {

	private static final String TAG = AgendaActivity.class.getSimpleName();
	private static final String ITEM_NAME = "name";
	private static final String ITEM_DATE = "date";
	private static final String ITEM_START = "start";
	private static final String ITEM_END = "end";
	
	private ListView eventList;
	private SimpleAdapter eventAdapter;
	private List<HashMap<String, String>> data;
	private String from[] = {ITEM_NAME, ITEM_DATE, ITEM_START, ITEM_END};
	private int to[] = {R.id.event_name, R.id.event_date, R.id.event_start, R.id.event_end};
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agenda);
		
		eventList = (ListView) findViewById(R.id.eventList);
		eventList.setEmptyView(findViewById(R.id.emptyList));
		
		data = new ArrayList<HashMap<String, String>>();
		eventAdapter = new SimpleAdapter(this, data, R.layout.event_list_item, from, to);
		eventList.setAdapter(eventAdapter);
		
		try {
			FileInputStream is = new FileInputStream(Config.DOWNLOAD_DIRECTORY+"/"+Config.FILE_NAME);
			progressBar = (ProgressBar) findViewById(R.id.progress);
			
			createCalendar(is);
			
		} catch (FileNotFoundException e) {
			showToast(getString(R.string.error_fnf, Config.FILE_NAME));
			e.printStackTrace();
		}
	}
	
	private void createCalendar(final FileInputStream is) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				android.util.Log.d(TAG, "before createing READER");
				CalendarReader reader = new CalendarReader(is);
				android.util.Log.d(TAG, "after READER is created");
				List<Event> events = reader.allEvents();
				android.util.Log.d(TAG, "after READER read all events");
				DateFormat dfTime = DateFormat.getTimeInstance(DateFormat.SHORT);
				DateFormat dfDate = DateFormat.getDateInstance(DateFormat.SHORT);
				
				for (Event e : events) {
					final HashMap<String, String> item = new HashMap<String, String>();
					item.put(ITEM_NAME, e.getName());
					item.put(ITEM_DATE, dfDate.format(e.getStart()));
					item.put(ITEM_START, getString(R.string.event_start, dfTime.format(e.getStart())));
					item.put(ITEM_END, getString(R.string.event_end, dfTime.format(e.getEnd())));
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							data.add(item);
							progressBar.setVisibility(View.GONE);
							eventAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		}).start();
	}
	
	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
