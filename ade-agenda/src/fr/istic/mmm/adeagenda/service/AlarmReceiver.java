package fr.istic.mmm.adeagenda.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	private static final String TAG = AlarmReceiver.class.getSimpleName();
//	
//	private static final int NOTIFICATION_ID = 42;
//	private NotificationManager notificationManager;
//	private Notification myNotification;

	@Override
	public void onReceive(Context context, Intent intent) {
//		Bundle extras = intent.getExtras();
		Log.v(TAG, "AlarmReceive");
//		
//		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		
//		int icon = R.drawable.logo;
//		String statusText = "ADE Notification";
//		long when = System.currentTimeMillis();
//
//		myNotification = new Notification(icon, statusText, when);
//
//		String notificationTitle = "ADE Agenda";
//		String notificationText = "Le prochain cours est prévue blablabla";
//
//		Intent seeAgendaIntent = new Intent(context, AgendaPagerActivity.class);
//		PendingIntent launchIntent = PendingIntent.getActivity(context, 0,	seeAgendaIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
//
//		myNotification.defaults |= Notification.DEFAULT_SOUND;
//		myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
//		myNotification.setLatestEventInfo(context, notificationTitle, notificationText, launchIntent);
//		notificationManager.notify(NOTIFICATION_ID, myNotification);
	}
}
