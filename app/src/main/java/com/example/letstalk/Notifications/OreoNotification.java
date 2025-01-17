package com.example.letstalk.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class OreoNotification extends ContextWrapper {
    //replace value with your package name value
    private static final String CHANNEL_ID = "com.example.letstalk";
    private static final String CHANNEL_NAME = "letstalk";
    private NotificationManager notificationManager;

    public OreoNotification(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getNotificationManager().createNotificationChannel(channel);
    }

    public NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getOreoNotification(String tile, String body, PendingIntent pendingIntent, Uri sendUri, String icon) {
        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(tile)
                .setContentText(body)
                .setSmallIcon(Integer.parseInt(icon))
                .setSound(sendUri)
                .setAutoCancel(true);
    }
}
