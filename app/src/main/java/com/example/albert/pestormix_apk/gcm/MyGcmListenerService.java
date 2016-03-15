package com.example.albert.pestormix_apk.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.activities.SplashActivity;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Albert on 15/03/2016.
 */
public class MyGcmListenerService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        sendNotification("You are recived this notification from " + from);
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.pestormix_logo)
                .setContentTitle("Pestormix message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
