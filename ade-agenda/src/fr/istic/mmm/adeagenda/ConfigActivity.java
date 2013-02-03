package fr.istic.mmm.adeagenda;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import fr.istic.mmm.adeagenda.calendar.CalendarReader;
import fr.istic.mmm.adeagenda.model.Event;
import fr.istic.mmm.adeagenda.utils.DateFormater;

public class ConfigActivity extends Activity {

	private static final String TAG = ConfigActivity.class.getSimpleName();
	
	public static final String FILE_NAME = "ADECal.ics";
	public static final String DOWNLOAD_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/ADECalendar";

	private EditText etDateStart;
	private EditText etDateEnd;

	private DatePickerDialog dialogDateStart;
	private DatePickerDialog dialogDateEnd;
	private Spinner spinnerAlarmTime, spinnerAlarmRecurrence;

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
				etDateStart.setText(DateFormater.dateToDisplayString(startYear,
						startMounth, startDay));
			} else if (view == findViewById(R.id.editText_date_end)) {
				endYear = year;
				endMounth = monthOfYear;
				endDay = dayOfMonth;
				etDateEnd.setText(DateFormater.dateToDisplayString(endYear,
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
		etDateStart.setText(DateFormater.dateToDisplayString(startYear,
				startMounth, startDay));

		etDateEnd = (EditText) findViewById(R.id.editText_date_end);
		etDateEnd.setText(DateFormater.dateToDisplayString(endYear, endMounth,
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

	public void onClickLoad(View view) {
		// Création de l'URL avec les bons paramètres
		String resources = "129";
		String calType = "ical";
		String login = "cal";
		String password = "visu";
		String projectId = "31";

		String cal_url = "http://plannings.univ-rennes1.fr/ade/custom/modules/plannings/direct_cal.jsp?calType="
				+ calType
				+ "&login="
				+ login
				+ "&password="
				+ password
				+ "&resources="
				+ resources
				+ "&firstDate="
				+ DateFormater.dateToURLString(startYear, endMounth, startDay)
				+ "&lastDate="
				+ DateFormater.dateToURLString(endYear, endMounth, endDay)
				+ "&projectId=" + projectId;

		// AsynkTask pour le téléchargement
		try {
			ProgressTask progressTask = new ProgressTask();
			progressTask.execute(new URL(cal_url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void onClickValidate(View view) {
		// Enregistrer les modifications
		// TODO

		this.finish();
	}

	public void openICSFile() {
		// Chargement fichier ics
		InputStream in;
		try {
			in = new FileInputStream(DOWNLOAD_DIRECTORY + "/" + FILE_NAME);
			CalendarReader reader = new CalendarReader(in);
			List<Event> events = reader.eventsOfTheDay();

			for (Event event : events) {
				android.util.Log.d(TAG, "[" + event.getName() + "]");
				android.util.Log.d(TAG, DateFormater.dateToString(event.getStart())
						+ " - " + (event.getDuration() / (1000 * 60 * 60))
						+ " Heures");
				android.util.Log.d(TAG, event.getDescription());
			}
			showToast("Ouverture terminée !");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void showToast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				android.util.Log.d(TAG, message);
				Toast.makeText(ConfigActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	// AsyncTask pour le téléchargement du fichier ICS
	private class ProgressTask extends AsyncTask<URL, Integer, Boolean> {

		private ProgressDialog dialog;

		public ProgressTask() {
			dialog = new ProgressDialog(ConfigActivity.this);
		}

		@Override
		protected void onPreExecute() {
			dialog.setTitle(getResources().getString(R.string.dialog_download_title));
			dialog.setMessage(getResources().getString(R.string.dialog_download_msg));
			dialog.show();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			if (success) {
				showToast(getResources().getString(R.string.dialog_download_toast));
			}
		}

		@Override
		protected Boolean doInBackground(final URL... urls) {
			try {
				// Création du dossier de destination
				File downloadDirectory = new File(DOWNLOAD_DIRECTORY);
				if (!downloadDirectory.exists()) {
					if (downloadDirectory.mkdir()) {
						android.util.Log.d(TAG, "Creating folder "+downloadDirectory.getAbsolutePath());	
					} else {
						android.util.Log.e(TAG, "Creating folder "+downloadDirectory.getAbsolutePath()+" FAILED");
						showToast("Opération annulée : problème lors de la sauvegarde");
						return false;
					}
				}

				URL url = urls[0];
				URLConnection conexion = url.openConnection();
				InputStream is = url.openStream();
				conexion.connect();

				// Téléchargement du fichier
				FileOutputStream destFile = new FileOutputStream(
						downloadDirectory + "/" + FILE_NAME);
				
				byte data[] = new byte[1024];
				int count = 0;
				while ((count = is.read(data)) != -1) {
					destFile.write(data, 0, count);
				}

				is.close();
				destFile.close();

				return true;
			} catch (Exception e) {
				android.util.Log.e(TAG, "error", e);
				return false;
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
	}

}
