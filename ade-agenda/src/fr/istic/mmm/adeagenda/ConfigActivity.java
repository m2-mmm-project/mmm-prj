package fr.istic.mmm.adeagenda;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import fr.istic.mmm.adeagenda.task.DownloadCalendarTask;
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
	private DatePickerDialog.OnDateSetListener mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			startYear = year;
			startMounth = monthOfYear;
			startDay = dayOfMonth;
			etDateStart.setText(DateFormater.getDisplayString(startYear,
					startMounth, startDay));

		}
	};
	private DatePickerDialog.OnDateSetListener mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			endYear = year;
			endMounth = monthOfYear;
			endDay = dayOfMonth;
			etDateEnd.setText(DateFormater.getDisplayString(endYear, endMounth,
					endDay));

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);

		SharedPreferences settings = getSharedPreferences(Config.ADE_PREF, 0);
		String firstDate = settings.getString(Config.PREF_START_DATE, "");
		String lastDate = settings.getString(Config.PREF_END_DATE, "");

		if (firstDate != "" && lastDate != "") {
			startYear = Integer.parseInt(firstDate.substring(0, 4));
			startMounth = Integer.parseInt(firstDate.substring(5, 7))-1;
			startDay = Integer.parseInt(firstDate.substring(8, 10));

			endYear = Integer.parseInt(lastDate.substring(0, 4));
			endMounth = Integer.parseInt(lastDate.substring(5, 7))-1;
			endDay = Integer.parseInt(lastDate.substring(8, 10));
		} else {
			startYear = Calendar.getInstance().get(Calendar.YEAR);
			startMounth = Calendar.SEPTEMBER;
			startDay = 1;

			endYear = Calendar.getInstance().get(Calendar.YEAR) + 1;
			endMounth = Calendar.SEPTEMBER;
			endDay = 30;

			if (Calendar.getInstance().get(Calendar.MONTH) < Calendar.AUGUST) {
				startYear--;
				endYear--;
			}
		}

		// Edit text date
		etDateStart = (EditText) findViewById(R.id.editText_date_start);
		etDateStart.setText(DateFormater.getDisplayString(startYear,
				startMounth, startDay));

		etDateEnd = (EditText) findViewById(R.id.editText_date_end);
		etDateEnd.setText(DateFormater.getDisplayString(endYear, endMounth,
				endDay));

		dialogDateStart = new DatePickerDialog(this, mStartDateSetListener,
				startYear, startMounth, startDay);
		dialogDateEnd = new DatePickerDialog(this, mEndDateSetListener,
				endYear, endMounth, endDay);

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

		String firstDate = DateFormater.getURLString(startYear, startMounth, startDay);
		String lastDate = DateFormater.getURLString(endYear, endMounth, endDay);
		
		
		edit.putBoolean(Config.PREF_CONFIG_DONE, true);
		edit.putInt(Config.PREF_PROJECT_ID, projectId);
		edit.putString(Config.PREF_RESOURCES_ID, resources);
		edit.putString(Config.PREF_LOGIN, "cal");
		edit.putString(Config.PREF_PASSWORD, "visu");
		edit.putString(Config.PREF_START_DATE, firstDate);
		edit.putString(Config.PREF_END_DATE, lastDate);
		edit.apply();
		Log.v("Config done", "Settings saved");
		

		Log.v("Config done", "Downloading new calendar");
		try {
			DownloadCalendarTask progressTask = new DownloadCalendarTask(projectId, resources, "cal", "visu", firstDate, lastDate);
			progressTask.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
