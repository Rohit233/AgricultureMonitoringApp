package com.example.agriculture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
  DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference();
  ImageView imageView;


    private SensorManager sensorManager;
    private ThermometerView thermometer;
    ProgressBar moistureLevel;
    private float temperature;
    TextView tempShow;
    Switch motorOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thermometer = (ThermometerView) findViewById(R.id.thermometer);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tempShow=findViewById(R.id.temp);
        motorOnOff=findViewById(R.id.motorOnOff);
        moistureLevel=findViewById(R.id.moisturLevel);
        moistureLevel.setMax(100);
        firebaseDatabase.child("soilmoisture").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                    firebaseDatabase.child("waterpump").setValue(true);
                    Toast.makeText(MainActivity.this,"Motor is on",Toast.LENGTH_LONG).show();
                }
                else if(!false){
                    firebaseDatabase.child("waterpump").setValue(false);
                    Toast.makeText(MainActivity.this,"Motor is off",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
      waterPumpCheck();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadAmbientTemperature();
        simulateAmbientTemperature();
        waterPumpCheck();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterAll();
    }

    private void simulateAmbientTemperature() {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                temperature = (float)27.0;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        thermometer.setCurrentTemp(temperature);
                        tempShow.setText(String.valueOf(temperature));

                    }
                });
            }
        }, 0, 3500);
    }

    private void loadAmbientTemperature() {
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "No Ambient Temperature Sensor !", Toast.LENGTH_LONG).show();
        }
    }

    private void unregisterAll() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values.length > 0) {
            temperature = sensorEvent.values[0];
            thermometer.setCurrentTemp(temperature);
            getSupportActionBar().setTitle(getString(R.string.app_name) + " : " + temperature);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

//  @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        imageView=findViewById(R.id.imageView);
//        Picasso.get().load("127.0.0.1").placeholder(R.drawable.thermometer).into(imageView);
//
//    }

    public void waterPumpCheck(){
        firebaseDatabase.child("waterpump").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().equals(true)){
                    motorOnOff.setChecked(true);
                    firebaseDatabase.child("waterpump").removeEventListener(this);
                }
                else{
                    motorOnOff.setChecked(false);
                    firebaseDatabase.child("waterpump").removeEventListener(this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
