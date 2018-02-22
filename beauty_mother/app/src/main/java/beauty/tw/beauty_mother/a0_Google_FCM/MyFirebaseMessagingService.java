package beauty.tw.beauty_mother.a0_Google_FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import beauty.tw.beauty_mother.R;
import beauty.tw.beauty_mother.a2_Roading_Join.atv_Roading;


/**
 * Created by kimtaewoo on 2017-01-22.
 */

//받는 곳
public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static String message;
    public static String title;
    public static String isMaster;

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        //추가한것
        title = remoteMessage.getData().get("title");
        message = remoteMessage.getData().get("messageBody");
        isMaster = remoteMessage.getData().get("isMaster");
        sendNotification();
    }

    private void sendNotification() {

        Intent intent = new Intent(this, atv_Roading.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_626)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());



        Intent popupIntent = new Intent(this, Dialog_Notification.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(popupIntent);

    }
}


