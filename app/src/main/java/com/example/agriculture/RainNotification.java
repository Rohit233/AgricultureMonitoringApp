package com.example.agriculture;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class RainNotification extends Application {
    public static final String CHANNEL_ID="Rain";
    public static final String CHANNEL_ID1="moisture";

    @Override
    public void onCreate() {
        super.onCreate();
    creteNotifiChannel();

    }
    private void creteNotifiChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel serviceChannel=new NotificationChannel(
                    CHANNEL_ID,
                    "Rain",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.setDescription("Rain Message");

            NotificationChannel channel=new NotificationChannel(
                    CHANNEL_ID1,
                    "moisture",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Moisture Message");
            NotificationManager manager=getSystemService(NotificationManager.class);
            NotificationManager manager1=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
            manager1.createNotificationChannel(channel);
        }
    }
}
