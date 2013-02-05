package fr.istic.mmm.adeagenda.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.util.Log;
import fr.istic.mmm.adeagenda.ConfigActivity;
import fr.istic.mmm.adeagenda.utils.Config;

public class DownloadCalendarTask extends AsyncTask<URL, Integer, Boolean> {
	
	private static final String TAG = DownloadCalendarTask.class.getSimpleName();
	
	private int projectId;
	private String resources;
	private String login;
	private String password;
	private String firstDate;
	private String lastDate;

	public DownloadCalendarTask(int projectId, String resources, String login, String password, String firstDate, String lastDate) {
		this.projectId = projectId;
		this.resources = resources;
		this.login = login;
		this.password = password;
		this.firstDate = firstDate;
		this.lastDate = lastDate;
	}
	
	@Override
	protected Boolean doInBackground(final URL... urls) {
		Log.v(TAG, "Updating");
		String calType = "ical";

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

		Log.v(TAG, "Trying to download : " + cal_url);
		
		try {
			boolean success = false;

			// Création du dossier de destination
			File downloadDir = new File(Config.DOWNLOAD_DIRECTORY);
			if (!(success = downloadDir.exists())) {
				if (success = downloadDir.mkdir()) {
					android.util.Log.d(TAG, "Creating folder " + downloadDir.getAbsolutePath());
				} else {
					android.util.Log.e(TAG, "Can't create folder " + downloadDir.getAbsolutePath());
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

				Log.v(TAG, "Download complete !");
				
				// Parse Calendar Task
				ParseCalendarTask parseTask = new ParseCalendarTask();
				parseTask.execute();
				
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			android.util.Log.e(TAG, "error", e);
			return false;
		}
	}

}
