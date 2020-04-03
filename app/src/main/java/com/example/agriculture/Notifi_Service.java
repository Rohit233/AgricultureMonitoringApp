package com.example.agriculture;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.agriculture.RainNotification.CHANNEL_ID;

public class Notifi_Service extends IntentService {
    Notification notification;
    DatabaseReference  firebaseDatabase= FirebaseDatabase.getInstance().getReference();
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public Notifi_Service(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
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
