package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//import java.util.concurrent.ThreadLocalRandom;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient client;

    TextView tv_data_long, tv_data_lat, tv_data_tarLon, tv_data_tarLat, tv_data_diff;
    Double tarLon, tarLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        tv_data_long = findViewById(R.id.tv_data_long);
        tv_data_lat = findViewById(R.id.tv_data_lat);
        tv_data_tarLon = findViewById(R.id.tv_data_tarLon);
        tv_data_tarLat = findViewById(R.id.tv_data_tarLat);
        tv_data_diff = findViewById(R.id.tv_data_diff);


        client = LocationServices.getFusedLocationProviderClient(this);
        Button buttonGuess = findViewById(R.id.getLoc);
        buttonGuess.setOnClickListener(v -> {

            if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                 requestPermission();
                 return;
            }

            client.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
                if(location != null){
                    TextView textView = findViewById(R.id.location);
                    textView.setText(location.toString());

                    tv_data_long.setText(String.valueOf(location.getLongitude()));
                    tv_data_lat.setText(String.valueOf(location.getLatitude()));

                }

            });

        });

        Button buttonTarget = findViewById(R.id.getTarget);
        buttonTarget.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
                return;
            }

            client.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
                if(location != null){
//                            TextView textView = findViewById(R.id.location);
//                            textView.setText(location.toString());

                    double latMod = BigDecimal.valueOf((Math.random())/1000).setScale(6, RoundingMode.CEILING).doubleValue();
                    double lonMod = BigDecimal.valueOf((Math.random())/1000).setScale(6, RoundingMode.CEILING).doubleValue();

                    if (Math.random() > 0.5) {
                        latMod = latMod * -1;
                    }

                    if (Math.random() > 0.5) {
                        lonMod = lonMod * -1;
                    }

                    tarLon = location.getLongitude() + lonMod;
                    tarLat = location.getLatitude() + latMod;

                    double lonDisp = BigDecimal.valueOf(tarLon).setScale(4, RoundingMode.CEILING).doubleValue();
                    double latDisp = BigDecimal.valueOf(tarLat).setScale(4, RoundingMode.CEILING).doubleValue();

                    tv_data_tarLon.setText(String.valueOf(lonDisp));
                    tv_data_tarLat.setText(String.valueOf(latDisp));



                    // try distance

                    float[] results = new float[1];
                    Location.distanceBetween(tarLat, tarLon,
                            location.getLatitude(), location.getLongitude(), results);

                    tv_data_diff.setText(String.valueOf(results[0]) + "meters");
                }

            });

        });

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
//    https://developer.android.com/reference/android/location/Location

}