package com.example.agriculture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationReceiver extends BroadcastReceiver {
   DatabaseReference firebaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    public void onReceive(Context context, Intent intent) {
   firebaseReference.child("waterpump").setValue("0");
    firebaseReference.child("soilmoisturemsg").setValue("0");
    }
}
