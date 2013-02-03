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
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import fr.istic.mmm.adeagenda.calendar.CalendarReader;
import fr.istic.mmm.adeagenda.model.Event;
import fr.istic.mmm.adeagenda.utils.DateFormater;

public class ConfigActivity extends Activity {

	public static final String ICS_FILE_NAME = "ADECal.ics";
	public static final String DOWNLOAD_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/ADECalendar";

	private EditText etDateStart;
	private EditText etDateEnd;

	private DatePickerDialog dialogDateStart;
	private DatePickerDialog dialogDateEnd;

	private int startYear;
	private int startMounth;
	private int startDay;
	private int endYear;
	private int endMounth;
	private int endDay;

	// DialogPicker Callback
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
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

	private RelativeLayout layoutAlramConfig;

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

		layoutAlramConfig = (RelativeLayout) findViewById(R.id.layout_alarm_config);
	}

	public void onClickDateStart(View view) {
		dialogDateStart.show();
	}

	public void onClickDateEnd(View view) {
		dialogDateEnd.show();
	}

	public void onClickCheckBoxAlarm(View view) {
		if (((CheckBox) view).isChecked())
			layoutAlramConfig.setVisibility(View.VISIBLE);
		else
			layoutAlramConfig.setVisibility(View.GONE);
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
			new ProgressTask(this).execute(new URL(cal_url));
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
			in = new FileInputStream(DOWNLOAD_DIRECTORY + "/" + ICS_FILE_NAME);
			CalendarReader reader = new CalendarReader(in);
			List<Event> events = reader.eventsOfTheDay();

			for (Event event : events) {
				Log.v("Event name", "[" + event.getName() + "]");
				Log.v("Event date", DateFormater.dateToString(event.getStart())
						+ " - " + (event.getDuration() / (1000 * 60 * 60))
						+ " Heures");
				Log.v("Event description", event.getDescription());
			}
			showToast("Ouverture terminée !");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			public void run() {
				Log.v("Toast", toast);
				Toast.makeText(ConfigActivity.this, toast, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	// AsyncTask pour le téléchargement du fichier ICS
	private class ProgressTask extends AsyncTask<URL, Void, Boolean> {

		private ProgressDialog dialog;
		private ConfigActivity activity;

		public ProgressTask(ConfigActivity activity) {
			this.activity = activity;
			dialog = new ProgressDialog(activity);
		}

		@Override
		protected void onPreExecute() {
			this.dialog.setTitle(getResources().getString(R.string.dialog_download_title));
			this.dialog.setMessage(getResources().getString(R.string.dialog_download_msg));
			this.dialog.show();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			if (success) {
				activity.showToast(getResources().getString(R.string.dialog_download_toast));
			}
		}

		@Override
		protected Boolean doInBackground(final URL... urls) {
			try {
				// Création du dossier de destination

				File downloadDir = new File(DOWNLOAD_DIRECTORY);
				boolean success = true;
				if (!downloadDir.exists()) {
					success = downloadDir.mkdir();
				}
				if (success) {
					URL url = urls[0];
					URLConnection conexion = url.openConnection();
					InputStream is = url.openStream();
					conexion.connect();

					// Téléchargement du fichier
					FileOutputStream destFile = new FileOutputStream(downloadDir + "/" + ICS_FILE_NAME);

					byte data[] = new byte[1024];
					int count = 0;
					while ((count = is.read(data)) != -1) {
						destFile.write(data, 0, count);
					}

					is.close();
					destFile.close();

					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				Log.e("WTF?", "error", e);
				return false;
			}
		}

	}

}
