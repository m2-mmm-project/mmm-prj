package fr.istic.mmm.adeagenda.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import fr.istic.mmm.adeagenda.utils.Config;
import fr.istic.mmm.adeagenda.utils.DateFormater;

public class UpdateService extends Service {

	private SharedPreferences settings;

	private boolean isConfigDone;
	private int projectId;
	private String resources;
	private String login;
	private String password;
	private String firstDate;
	private String lastDate;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.v("UpdateService", "Creating Service");

		settings = getSharedPreferences(Config.ADE_PREF, 0);
		isConfigDone = settings.getBoolean(Config.PREF_CONFIG_DONE, false);
		
		projectId = settings.getInt(Config.PREF_PROJECT_ID, 31);
		resources = settings.getString(Config.PREF_RESOURCES_ID, "239");
		login = settings.getString(Config.PREF_LOGIN, "cal");
		password = settings.getString(Config.PREF_PASSWORD, "visu");
		firstDate = settings.getString(Config.PREF_START_DATE,DateFormater.dateToURLString(1999, 12, 31));
		lastDate = settings.getString(Config.PREF_END_DATE,DateFormater.dateToURLString(2000, 12, 31));

		
		final Handler handler = new Handler();
		Timer timer = new Timer();
		
		TimerTask doAsynchronousTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							ProgressTask progressTask = new ProgressTask();
							progressTask.execute();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		};
		
		timer.schedule(doAsynchronousTask, 0, 60000*60); // execute in every hour
		
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.v("UpdateService", "Service shutdown");
		super.onDestroy();
	}

	private class ProgressTask extends AsyncTask<URL, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(final URL... urls) {
			Log.v("UpdateService", "Updating");
			String calType = "ical";
			
			isConfigDone = settings.getBoolean(Config.PREF_CONFIG_DONE, false);
			if(!isConfigDone){
				return false;
			}
			
			projectId = settings.getInt(Config.PREF_PROJECT_ID, 31);
			resources = settings.getString(Config.PREF_RESOURCES_ID, "129");
			login = settings.getString(Config.PREF_LOGIN, "cal");
			password = settings.getString(Config.PREF_PASSWORD, "visu");
			firstDate = settings.getString(Config.PREF_START_DATE,DateFormater.dateToURLString(1999, 12, 31));
			lastDate = settings.getString(Config.PREF_END_DATE,DateFormater.dateToURLString(2000, 12, 31));

			final String cal_url = "http://plannings.univ-rennes1.fr/ade/custom/modules/plannings/direct_cal.jsp?calType="
					+ calType
					+ "&login="
					+ login
					+ "&password="
					+ password
					+ "&resources="
					+ resources
					+ "&firstDate="
					+ firstDate
					+ "&lastDate=" + lastDate + "&projectId=" + projectId;
			
			try {
				boolean success = false;

				// Création du dossier de destination
				File downloadDir = new File(Config.DOWNLOAD_DIRECTORY);
				if (!(success = downloadDir.exists())) {
					if (success = downloadDir.mkdir()) {
						android.util.Log.d("UpdatorService","Creating folder "+ downloadDir.getAbsolutePath());
					} else {
						android.util.Log.e("UpdatorService","Can't create folder "+ downloadDir.getAbsolutePath());
						return false;
					}
				}

				if (success) {
					URL url = new URL(cal_url);
					URLConnection uConn = url.openConnection();
					InputStream is = url.openStream();
					uConn.connect();
					// Téléchargement du fichier
					FileOutputStream destFile = new FileOutputStream(downloadDir + "/" + Config.FILE_NAME);

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
				android.util.Log.e("UpdatorService", "error", e);
				return false;
			}
		}
	}

}
