package com.example.foodemption;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 11111;
    private FusedLocationProviderClient mFusedLocationClient;
    private Task<Location> location;
    TextView tvLocation;
    private ListView lvBusinesses;
    private String LI;

    //int gotLocation = 55;
    //double lat = 0.0;
    //private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLocation = findViewById(R.id.tvLocation);
        lvBusinesses = findViewById(R.id.lvBusinesses);

        final Map<String, Business> businessesMap = Business.getBusinessesFromFile("businesses.json", this);

        String[] listItems = new String[businessesMap.size()];
        int i = 0;

        for (Map.Entry<String, Business> entry : businessesMap.entrySet()) {

                Business business = businessesMap.get(entry);
                listItems[i] = business.getName();
                i++;

        }


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);

        lvBusinesses.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_REQUEST_CODE);

        } else {
            Toast.makeText(MainActivity.this, "Permission granted! Thank you!", Toast.LENGTH_SHORT).show();
        }


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        location = mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>()
                        //.addOnCompleteListener (this, new OnCompleteListener <Location>()

                {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            tvLocation.setText("Latitude: " + String.valueOf(location.getLatitude()) + "\n" +
                                    "Longitude: " + String.valueOf(location.getLongitude()));

                            // Logic to handle location object
                            //gotLocation = 100;

                        } else {
                            //gotLocation = 99;
                            tvLocation.setText("Couldn't get location please restart the app");
                        }
                        //tvLocation.setText(String.valueOf(gotLocation));
                        //tvLocation.setText(String.valueOf(location.getLatitude()));
                    }
                });

                    /*@Override
                    public void onComplete(@NonNull Task<Location> location) {

                        if (location != null) {
                            //tvLocation.setText(String.valueOf(location.getLatitude()));
                            tvLocation.setText(String.valueOf(location.getResult().getLatitude()));

                            // Logic to handle location object
                            gotLocation = 100;

                        }
                        else {
                            gotLocation = 99;
                        }
                        tvLocation.setText(String.valueOf(gotLocation));
                        tvLocation.setText(String.valueOf(location.getResult().getLatitude()));
                    }
                    */
        /*if (location != null) {
            //userLocation = location.getResult().toString();
            lat = location.getResult().getLatitude();
            //tvLocation.setText(userLocation);
            gotLocation = 50;
        }
        else {
            gotLocation = 49;
        }
        */


        //tvLocation.setText(Double.toString(lat));
        //tvLocation.setText(Integer.toString(gotLocation));
        //tvLocation.setText(String.valueOf(gotLocation));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thank you! Permission granted", Toast.LENGTH_SHORT).show();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("This permission is important to offer relevant deals, please approve it")
                            .setTitle("Important permission required!");

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    ACCESS_FINE_LOCATION_REQUEST_CODE);
                        }
                    });

                    dialog.setNegativeButton("NO THANKS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Cannot be done!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialog.show();
                } else {
                    Toast.makeText(this, "We will never show this to you again!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}

