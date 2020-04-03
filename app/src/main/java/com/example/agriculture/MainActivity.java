package com.example.agriculture;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleObserver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.agriculture.RainNotification.CHANNEL_ID;

public class MainActivity extends AppCompatActivity {
  DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference();
  ImageView imageView;


    private SensorManager sensorManager;
    private ThermometerView thermometer;
    ProgressBar progressBarTemp,progressBarHumidity,progressBarMoisture;
    ProgressBar moistureLevel;
    private float temperature;
    TextView tempShow;
    TextView humidityView;
    Switch motorOnOff;
    NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thermometer = (ThermometerView) findViewById(R.id.thermometer);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tempShow=findViewById(R.id.temp);
        motorOnOff=findViewById(R.id.motorOnOff);
        progressBarTemp=findViewById(R.id.progressBarTemp);
        progressBarHumidity=findViewById(R.id.progressBarHumidity);
        progressBarMoisture=findViewById(R.id.progressBarMoisture);
        moistureLevel=findViewById(R.id.moisturLevel);
        humidityView=findViewById(R.id.humidity);
        moistureLevel.setMax(100);
        notificationManager= NotificationManagerCompat.from(this);
        firebaseDatabase.child("soilmoisture").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBarMoisture.setVisibility(View.INVISIBLE);
                moistureLevel.setVisibility(View.VISIBLE);
                moistureLevel.setProgress(Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue()).toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        motorOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    firebaseDatabase.child("waterpump").setValue("1");
                    Toast.makeText(MainActivity.this,"Motor is on",Toast.LENGTH_LONG).show();
                }
                else if(!false){
                    firebaseDatabase.child("waterpump").setValue("0");
                    Toast.makeText(MainActivity.this,"Motor is off",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
//        startService(new Intent(MainActivity.this,Notification_Service.class));
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
      waterPumpCheck();
        Temperature();
        Humidity();
      startService(new Intent(MainActivity.this,Notification_Service.class));
//      startForegroundService(new Intent(MainActivity.this,Notification_Service.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadAmbientTemperature();
       Temperature();
        waterPumpCheck();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

//  Humidity
   private  void Humidity(){
        firebaseDatabase.child("humidity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBarHumidity.setVisibility(View.INVISIBLE);
                humidityView.setVisibility(View.VISIBLE);
                int humidity=Integer.parseInt(dataSnapshot.getValue().toString());
                humidityView.setText(humidity+" %");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


   }

//temp
    private void Temperature() {


        firebaseDatabase.child("temp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int temp= Integer.parseInt(dataSnapshot.getValue().toString());
                progressBarTemp.setVisibility(View.INVISIBLE);
                tempShow.setVisibility(View.VISIBLE);
                thermometer.setCurrentTemp(temp);
                tempShow.setText(String.valueOf(temp) + "°c");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    public void waterPumpCheck(){
        firebaseDatabase.child("waterpump").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("1")){
                    motorOnOff.setChecked(true);
//                    firebaseDatabase.child("waterpump").removeEventListener(this);
                }
                else{
                    motorOnOff.setChecked(false);
//                    firebaseDatabase.child("waterpump").removeEventListener(this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
