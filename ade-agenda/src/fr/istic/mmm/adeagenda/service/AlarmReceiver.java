package fr.istic.mmm.adeagenda.service;

import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.DateFormat;
import android.util.Log;
import fr.istic.mmm.adeagenda.AgendaPagerActivity;
import fr.istic.mmm.adeagenda.EventActivity;
import fr.istic.mmm.adeagenda.HomeActivity;
import fr.istic.mmm.adeagenda.R;

public class AlarmReceiver extends BroadcastReceiver {

	private static final String TAG = AlarmReceiver.class.getSimpleName();
	
	private static final int NOTIFICATION_ID = 42;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, "AlarmReceive. Send Notification");
		Bundle extras = intent.getExtras();
		
		CharSequence eventName = extras.getCharSequence(EventActivity.EVENT_NAME);
		Date eventStart = new Date(extras.getLong(EventActivity.EVENT_START));
		CharSequence eventPlace = extras.getCharSequence(EventActivity.EVENT_PLACE);
		CharSequence strTime = DateFormat.format("hh:mm", eventStart); 
		
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("ADEAgenda - Notification")
                .setContentText(eventName + " à "+ strTime +" " + eventPlace);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.from(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(HomeActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(new Intent(context, AgendaPagerActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.getNotification());
	}
}
