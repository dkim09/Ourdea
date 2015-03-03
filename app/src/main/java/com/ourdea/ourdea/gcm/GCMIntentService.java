package com.ourdea.ourdea.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.activities.DashboardActivity;

public class GCMIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
	public static final String PROPERTY_NAME = "OURDEA_PROPERTY";
    public static final String PROPERTY_REG_ID = "gcmId";
    public static final String TYPE = "type";
    public static final String TYPE_NEWREGID = "new_reg_id";
    public static final String TYPE_NOTIFICATION = "notification";
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
	
	public GCMIntentService() {
		super("GCMIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        Log.d("TESTING", "JERER");
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            Log.d ("TESTING", "got GCM: " + extras.toString());
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            	processMessage (extras);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);
	}
	
	private void processMessage (Bundle extras){
		String type = extras.getString(TYPE);
		if (type == null){
			return;
		} else if (type.equals(TYPE_NEWREGID)){
	        SharedPreferences prefs = getSharedPreferences(PROPERTY_NAME, Context.MODE_PRIVATE);
	        SharedPreferences.Editor editor = prefs.edit();
	        editor.putString(PROPERTY_REG_ID, extras.getString(MESSAGE));
	        editor.commit();
		} else if (type.equals(TYPE_NOTIFICATION)){
            sendNotification(extras);
		}
	}
	
    // Put the message into a notification and post it.
    private void sendNotification(Bundle extras) {
    	String title = extras.getString(TITLE);
    	String message = extras.getString(MESSAGE);

    	if (title == null){
    		title = getString(R.string.app_name);
    	}

        int notificationId = 1;
    	if (message == null){
    		message = getString(R.string.app_name);
    	}

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, DashboardActivity.class);
        notificationIntent.putExtra(getString(R.string.PROPERTY_GCM_NOTIFICATION_ID), notificationId);
        PendingIntent contentIntent = PendingIntent.getActivity(this, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_action_assign_to_me)
        .setContentTitle(title)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
        .setContentText(message)
        .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
        .setAutoCancel(true);
        
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
