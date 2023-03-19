package com.mrprogrammer.mrtower.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class PushNotification extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        String title=remoteMessage.getNotification().getTitle();
        String msg=remoteMessage.getNotification().getBody();
        String img=remoteMessage.getNotification().getIcon();

        String ID="HEADS_UP_NOTIFICATION";
        NotificationChannel channel= new NotificationChannel(ID,"Heads Up Notification",NotificationManager.IMPORTANCE_HIGH);

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this,ID)
                .setContentTitle(title)
                .setContentText(msg)
                //.setSmallIcon(R.drawable.logo)
                .setAutoCancel(true);

        assert title != null;
        NotificationManagerCompat.from(this).notify(1,notification.build());
        super.onMessageReceived(remoteMessage);

    }
}
