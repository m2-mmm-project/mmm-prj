package fr.istic.mmm.adeagenda;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
	private EditText selectedEdit;
	
	private DatePickerDialog dialogDateStart;
	private DatePickerDialog dialogDateEnd;

	// DialogPicker Callback
	private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                	selectedEdit.setText(DateFormater.dateToString(dayOfMonth, monthOfYear, year));
                }
            };
            
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		
		// Edit text date
		etDateStart = (EditText) findViewById(R.id.editText_date_start);
		etDateEnd = (EditText) findViewById(R.id.editText_date_end);
		
		dialogDateStart = new DatePickerDialog(this, mDateSetListener, 2012, 8, 1);
		dialogDateEnd = new DatePickerDialog(this, mDateSetListener, 2013, 6, 31);
	}
	
	public void onClickDateStart(View view) {
		selectedEdit = etDateStart;
		dialogDateStart.show();
	}
	
	public void onClickDateEnd(View view) {
		selectedEdit = etDateEnd;
		dialogDateEnd.show();
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
				+ etDateStart.getText()
				+ "&lastDate="
				+ etDateEnd.getText()
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
				Log.v("Event date", DateFormater.dateToString(event.getStart()) + " - "
						+ (event.getDuration() / (1000 * 60 * 60)) + " Heures");
				Log.v("Event description", event.getDescription());
			}
			showToast("Done !");
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
			this.dialog.setTitle("Please wait");
			this.dialog.setMessage("Downloading...");
			this.dialog.show();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			if (success) {
				activity.openICSFile();
			}
		}

		@Override
		protected Boolean doInBackground(final URL... urls) {
			try {
				// Création du dossier de destination
				File downloadDirectory = new File(DOWNLOAD_DIRECTORY);
				if (!downloadDirectory.exists()) {
					Log.v("Calendar", "Création dossier ...");
					if (downloadDirectory.mkdir()) {
						Log.v("Calendar", "Création dossier ok");
					}
					Log.v("Calendar", downloadDirectory.getAbsolutePath());
				}

				URL url = urls[0];
				URLConnection conexion = url.openConnection();
				InputStream is = url.openStream();
				conexion.connect();

				// Téléchargement du fichier
				FileOutputStream destFile = new FileOutputStream(
						downloadDirectory + "/" + ICS_FILE_NAME);

				byte data[] = new byte[1024];
				int count = 0;
				while ((count = is.read(data)) != -1) {
					destFile.write(data, 0, count);
				}

				is.close();
				destFile.close();

				return true;
			} catch (Exception e) {
				Log.e("tag", "error", e);
				return false;
			}
		}

	}

}
