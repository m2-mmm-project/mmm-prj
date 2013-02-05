package fr.istic.mmm.adeagenda.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import fr.istic.mmm.adeagenda.task.DownloadCalendarTask;
import fr.istic.mmm.adeagenda.utils.Config;
import fr.istic.mmm.adeagenda.utils.DateFormater;

public class UpdateService extends Service {

	private static final String TAG = UpdateService.class.getSimpleName();
	
	private SharedPreferences settings;

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
		super.onCreate();
		Log.v(TAG, "Creating Service");
		
		final Handler handler = new Handler();
		Timer timer = new Timer();

		TimerTask doAsynchronousTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Log.v(TAG, "Running task");
						settings = getSharedPreferences(Config.ADE_PREF, 0);

						boolean configIsDone = settings.getBoolean(
								Config.PREF_CONFIG_DONE, false);

						if (configIsDone) {
							Log.v(TAG, "Config Ok, launch UpdateTask");
							projectId = settings.getInt(Config.PREF_PROJECT_ID,
									31);
							resources = settings.getString(
									Config.PREF_RESOURCES_ID, "129");
							login = settings
									.getString(Config.PREF_LOGIN, "cal");
							password = settings.getString(Config.PREF_PASSWORD,
									"visu");
							firstDate = settings.getString(
									Config.PREF_START_DATE,
									DateFormater.getDateURLString(1999, 11, 31));
							lastDate = settings
									.getString(Config.PREF_END_DATE,
											DateFormater.getDateURLString(2000,
													11, 31));

							try {
								DownloadCalendarTask downloadTask = new DownloadCalendarTask(
										projectId, resources, login, password,
										firstDate, lastDate);
								downloadTask.execute();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							Log.v(TAG, "Bad config, no update");
						}
					}
				});
			}
		};

		timer.schedule(doAsynchronousTask, 0, Config.TASK_PERIOD);
	}

	@Override
	public void onDestroy() {
		Log.v(TAG, "Service shutdown");
		super.onDestroy();
	}

}
