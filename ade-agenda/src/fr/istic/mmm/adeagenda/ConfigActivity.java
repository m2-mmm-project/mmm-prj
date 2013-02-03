package fr.istic.mmm.adeagenda;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import fr.istic.mmm.adeagenda.utils.Config;
import fr.istic.mmm.adeagenda.utils.DateFormater;

public class ConfigActivity extends Activity {

	private static final String TAG = ConfigActivity.class.getSimpleName();
	
	private EditText etDateStart;
	private EditText etDateEnd;

	private DatePickerDialog dialogDateStart;
	private DatePickerDialog dialogDateEnd;
	private Spinner spinnerAlarmTime, spinnerAlarmRecurrence;

	
	private int projectId;
	private String resources;
	private int startYear;
	private int startMounth;
	private int startDay;
	private int endYear;
	private int endMounth;
	private int endDay;

	// DialogPicker Callback
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			if (view == findViewById(R.id.editText_date_start)) {
				startYear = year;
				startMounth = monthOfYear;
				startDay = dayOfMonth;
				etDateStart.setText(DateFormater.dateToDisplayString(ConfigActivity.this, startYear,
						startMounth, startDay));
			} else if (view == findViewById(R.id.editText_date_end)) {
				endYear = year;
				endMounth = monthOfYear;
				endDay = dayOfMonth;
				etDateEnd.setText(DateFormater.dateToDisplayString(ConfigActivity.this, endYear,
						endMounth, endDay));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);

		startYear = Calendar.getInstance().get(Calendar.YEAR);
		startMounth = Calendar.SEPTEMBER;
		startDay = 1;

		endYear = Calendar.getInstance().get(Calendar.YEAR) + 1;
		endMounth = Calendar.SEPTEMBER;
		endDay = 31;

		if (Calendar.getInstance().get(Calendar.MONTH) < Calendar.AUGUST) {
			startYear--;
			endYear--;
		}

		// Edit text date
		etDateStart = (EditText) findViewById(R.id.editText_date_start);
		etDateStart.setText(DateFormater.dateToDisplayString(this, startYear,
				startMounth, startDay));

		etDateEnd = (EditText) findViewById(R.id.editText_date_end);
		etDateEnd.setText(DateFormater.dateToDisplayString(this, endYear, endMounth,
				endDay));

		dialogDateStart = new DatePickerDialog(this, mDateSetListener,
				startYear, startMounth, startDay);
		dialogDateEnd = new DatePickerDialog(this, mDateSetListener, endYear,
				endMounth, endDay);

		spinnerAlarmTime = (Spinner) findViewById(R.id.spinner_alarm_time);
		spinnerAlarmRecurrence = (Spinner) findViewById(R.id.spinner_alarm_recurence);
		spinnerAlarmTime.setEnabled(false);
		spinnerAlarmRecurrence.setEnabled(false);
	}

	public void onClickDateStart(View view) {
		dialogDateStart.show();
	}

	public void onClickDateEnd(View view) {
		dialogDateEnd.show();
	}

	public void onClickCheckBoxAlarm(View view) {
		if (((CheckBox) view).isChecked()) {
			spinnerAlarmTime.setEnabled(true);
			spinnerAlarmRecurrence.setEnabled(true);	
		} else {
			spinnerAlarmTime.setEnabled(false);
			spinnerAlarmRecurrence.setEnabled(false);
		}
	}

	public void onClickValidate(View view) {

		SharedPreferences settings = getSharedPreferences(Config.ADE_PREF, 0);
		Editor edit = settings.edit();
		
		resources = "129"; // TODO
		projectId = 31;
		
		edit.putBoolean(Config.PREF_CONFIG_DONE, true);
		edit.putInt(Config.PREF_PROJECT_ID, projectId);
		edit.putString(Config.PREF_RESOURCES_ID, resources);
		edit.putString(Config.PREF_LOGIN, "cal");
		edit.putString(Config.PREF_PASSWORD, "visu");
		edit.putString(Config.PREF_START_DATE, DateFormater.dateToURLString(startYear, startMounth, startDay));
		edit.putString(Config.PREF_END_DATE, DateFormater.dateToURLString(endYear, endMounth, endDay));
		edit.apply();
		
		Log.v("Config done", "Settings saved");
		this.finish();
	}

	public void showToast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				android.util.Log.d(TAG, message);
				Toast.makeText(ConfigActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
