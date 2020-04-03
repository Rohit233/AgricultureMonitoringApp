package com.example.agriculture;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.agriculture.RainNotification.CHANNEL_ID;
import static com.example.agriculture.RainNotification.CHANNEL_ID1;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Notification_Service extends Service{
    DatabaseReference firebaseDatabase= FirebaseDatabase.getInstance().getReference();
    Notification notification;
    Notification notification2;
    RemoteViews notificatonLayoput;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

      rainNotifi();
      waterpumpNotify();

       Intent nofiIntent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,nofiIntent,PendingIntent.FLAG_UPDATE_CURRENT);
       android.app.Notification notification1=new NotificationCompat.Builder(this,CHANNEL_ID)
                .build();
        startForeground(1,notification1);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

   public void waterpumpNotify(){
        firebaseDatabase.child("soilmoisturemsg").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("1")){
                   Intent broadcastIntent=new Intent(getApplicationContext(),NotificationReceiver.class);
                   PendingIntent PIntent=PendingIntent.getBroadcast(getApplicationContext(),0,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),
                            0,intent,0
                    );
                    notification2=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID1)
                            .setContentTitle("Water Pump")
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setSmallIcon(R.drawable.ic_adb_black_24dp)
                            .setContentTitle("Your Field get Suficient water turn off water pump")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .addAction(R.mipmap.ic_launcher,"Water Pump Off",PIntent)

                            .build();


                    NotificationManagerCompat notificationManager= NotificationManagerCompat.from(getApplicationContext());
                    notificationManager.notify(1,notification2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

   }


    public void rainNotifi(){
        firebaseDatabase.child("rainmsg").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("1")){

                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),
                            0,intent,0
                    );
                    notification=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                            .setContentTitle("Rain is coming")
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setSmallIcon(R.drawable.ic_adb_black_24dp)
                            .setSubText("Rain is comming")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_CALL)
                            .setContentIntent(pendingIntent)
                            .build();

                    notification2=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID1)
                            .setContentTitle("Water Pump")
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setSmallIcon(R.drawable.ic_adb_black_24dp)
                            .setContentTitle("Yor Field get Suficient water turn off water pump")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setContentIntent(pendingIntent)
                            .build();


                    NotificationManagerCompat notificationManager= NotificationManagerCompat.from(getApplicationContext());
                    notificationManager.notify(1,notification);
//                    startForeground(1,notification);
                    firebaseDatabase.child("rainmsg").setValue("0");
//                     firebaseDatabase.child("rainmsg").removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


