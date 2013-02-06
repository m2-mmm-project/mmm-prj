package fr.istic.mmm.adeagenda.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import fr.istic.mmm.adeagenda.EventActivity;
import fr.istic.mmm.adeagenda.db.AgendaDb;
import fr.istic.mmm.adeagenda.model.Event;
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
						Log.v(TAG, "Running task Download");
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
									DateFormater.getDateURLString(2012, 9, 01));
							lastDate = settings
									.getString(Config.PREF_END_DATE,
											DateFormater.getDateURLString(2013,
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

		TimerTask setNotificationTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Log.v(TAG, "Setting up alarm for notification");
						settings = getSharedPreferences(Config.ADE_PREF, 0);

						boolean configIsDone = settings.getBoolean(
								Config.PREF_CONFIG_DONE, false);

						int alarmTime = settings.getInt(Config.PREF_ALARM_TIME,
								-1);
						int alarmRec = settings.getInt(Config.PREF_ALARM_REC,
								-1);

						if (configIsDone && (alarmTime >= 0 || alarmRec >= 0)) {
							Log.v(TAG, "Notification demandé");
							// get a Calendar object with current time
							Calendar cal = Calendar.getInstance();
							cal.add(Calendar.SECOND, 10);

							AgendaDb db = new AgendaDb(getApplicationContext());
							Event event = db.getNextEvent(new Date());

							// Creates an explicit intent for an Activity in
							// your
							// app
							if (event != null) {
								Bundle eventInfo = new Bundle();
								eventInfo.putString(EventActivity.EVENT_NAME,
										event.getName());
								eventInfo.putLong(EventActivity.EVENT_START,
										event.getStart().getTime());
								eventInfo.putLong(EventActivity.EVENT_END,
										event.getEnd().getTime());
								eventInfo.putString(EventActivity.EVENT_PLACE,
										event.getPlace());
								eventInfo.putString(
										EventActivity.EVENT_DESCRIPTION,
										event.getDescription());

								Intent intent = new Intent(
										getApplicationContext(),
										AlarmReceiver.class);
								intent.putExtras(eventInfo);

								PendingIntent pendingIntent = PendingIntent
										.getBroadcast(getApplicationContext(),
												13454, intent, 0);
								AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
								alarmManager.set(AlarmManager.RTC_WAKEUP,
										cal.getTimeInMillis() + 1000,
										pendingIntent);

								Log.v(TAG, "Alarm setted");
							}
						}

					}
				});
			}
		};

		timer.schedule(setNotificationTask, 0, Config.NOTIF_TASK_PERIOD);

	}

	@Override
	public void onDestroy() {
		Log.v(TAG, "Service shutdown");
		super.onDestroy();
	}

}
