package existv2.com.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

import existv2.com.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static int count = 0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "Notification Message TITLE: " + Objects.requireNonNull(remoteMessage.getNotification()).getTitle());
        Log.d(TAG, "Notification Message BODY: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message DATA: " + remoteMessage.getData().toString());
        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());
    }
    //This method is only generating push notification
    private void sendNotification(String messageTitle, String messageBody, Map<String, String> row) {
        PendingIntent contentIntent = null;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(MyFirebaseMessagingService.this.getResources(), R.drawable.ic_logo))
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[] { 1000, 1000})
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(count, notificationBuilder.build());
        count++;
    }
}