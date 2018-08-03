package com.example.foodemption;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 11111;
    private FusedLocationProviderClient mFusedLocationClient;
    private Task<Location> location;
    TextView tvLastUpdate;
    private ListView lvBusinesses;
    //private DistanceCalculator distanceCalculator;
    private BusinessAdapter adapter;
    private ArrayList<Business> businessesList;
    private SimpleDateFormat lastUpdate;
    private DatabaseReference firebaseRootRef;
    private DatabaseReference BusinessesRef;
    private ArrayList<Business> fbBusiness;
    private ArrayList<Business> fbBusinessList;
    private String TAG;
    private Business business;
    private Location phoneLocation;
    private TextView tvFbData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLastUpdate = findViewById(R.id.tvLastUpdate);
        lvBusinesses = findViewById(R.id.lvBusinesses);
        lvBusinesses.setVisibility(View.INVISIBLE);
        //businessesList = Business.getBusinessesFromFile("businessesHeb.json", this);
        //adapter = new BusinessAdapter(this, businessesList);
        //adapter = new BusinessAdapter(this, fbBusinessList);
        //lvBusinesses.setAdapter(adapter);
        lastUpdate = new SimpleDateFormat("E, d/M HH:mm");


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseRootRef = firebaseDatabase.getReference();
        BusinessesRef = firebaseRootRef.child("businesses");
        fbBusiness = new ArrayList<>();
        tvFbData = findViewById(R.id.tvFbData);

        getData(new FirebaseBusinessesCallback() {
            @Override
            public void onCallback(ArrayList<Business> businessList) {
                fbBusinessList = businessList;
                DistanceCalculator distanceCalculator = new DistanceCalculator(phoneLocation, fbBusinessList);
                distanceCalculator.calculateDistance();
                //adapter.notifyDataSetChanged();
                //lvBusinesses.setVisibility(View.VISIBLE);

                adapter = new BusinessAdapter(MainActivity.this, fbBusiness);
                adapter.notifyDataSetChanged();
                lvBusinesses.setAdapter(adapter);
                lvBusinesses.setVisibility(View.VISIBLE);


            }
        });


        getLocation();
    }

    private void getData(final FirebaseBusinessesCallback firebaseBusinessesCallback) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    business = new Business();
                    business.setName(ds.child("name").getValue(String.class));
                    business.setID(ds.child("ID").getValue(String.class));
                    business.setType(ds.child("type").getValue(String.class));
                    business.setLatitude(ds.child("latitude").getValue(Double.class));
                    business.setLongitude(ds.child("longitude").getValue(Double.class));
                    business.setAddress(ds.child("address").getValue(String.class));
                    business.setCity(ds.child("city").getValue(String.class));
                    business.setHours(ds.child("hours").getValue(String.class));
                    business.setPhoneNumber(ds.child("phoneNumber").getValue(String.class));
                    business.setLogoURL(ds.child("logoURL").getValue(String.class));
                    business.setDiscountStartHour(ds.child("discountStartHour").getValue(String.class));

                    fbBusiness.add(business);
                }

                tvFbData.setText(fbBusiness.get(0).getType());
                firebaseBusinessesCallback.onCallback(fbBusiness);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG, databaseError.getMessage());
            }
        };

        BusinessesRef.addListenerForSingleValueEvent(valueEventListener);
    }


    @Override
    public void onRestart() {
        super.onRestart();
        showCurrentDateTime();
        getLocation();

    }

    public void getLocation() {
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

                {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            //tvLocation.setText("Latitude: " + String.valueOf(location.getLatitude()) + "\n" +
                            //        "Longitude: " + String.valueOf(location.getLongitude()));
                            showCurrentDateTime();
                            //DistanceCalculator distanceCalculator = new DistanceCalculator(location, businessesList);
                           // distanceCalculator.calculateDistance();
                            //adapter.notifyDataSetChanged();
                            //lvBusinesses.setVisibility(View.VISIBLE);
                            phoneLocation = location;

                            // Logic to handle location object
                            //gotLocation = 100;

                        } else {
                            //gotLocation = 99;
                            tvLastUpdate.setText("Couldn't get location please restart the app");
                        }
                        //tvLastUpdate.setText(String.valueOf(gotLocation));
                        //tvLastUpdate.setText(String.valueOf(location.getLatitude()));
                    }
                });

    }

    public void showCurrentDateTime() {
        Date currentTime = Calendar.getInstance().getTime();
        String currentTimeFormat = lastUpdate.format(currentTime);
        tvLastUpdate.setText("מעודכן ל: " + currentTimeFormat);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean isAccessFineLocationRequestCode = requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE;
        boolean isPermissionDenied = grantResults[0] == PackageManager.PERMISSION_DENIED;
        boolean shouldShowRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (isAccessFineLocationRequestCode && isPermissionDenied && shouldShowRequestPermissionRationale) {
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

    private interface FirebaseBusinessesCallback {
        void onCallback(ArrayList<Business> businessList);
    }
}

//Algolia code
        /*Client client = new Client("090J1XPRFE", "ccf46d6f9da2b10235034f9845bf015f");
        Index index = client.getIndex("Businesses");
        Query query = new Query("03-561-8111")
                .setAttributesToRetrieve("phoneNumber")
                .setHitsPerPage(50);


        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                // [...]
                //String Businumber = content.optString("phoneNumber");
                //Gson gson = new Gson();
               // String queryBusiness = gson.fromJson(content.getString("hits"),Business.class);

                /*try {
                    String businessName = content.getJSONObject("businesses").getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/



        /*try {
            index.addObjectAsync(new JSONObject()
                    .put("name", "לחמנינה")
                    .put("ID", "11111114")
                    .put("latitude", 32.071863)
                    .put("longitude", 34.779550)
                    .put("address", "מרמורק 4 תל אביב")
                    .put("hours", "7:00-23:00")
                    .put("phone number", "03-536-2124")
                    .put("logo", "")
                    .put("discount start hour", "20:00"), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        /*
        JSONObject settings = new JSONObject();
        //.append("searchableAttributes", "name")
        //        .append("searchableAttributes", "type");
        index.setSettingsAsync(settings, null);

        CompletionHandler completionHandler = new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                // [...]
            }
        };
// search by name
        index.searchAsync(new Query("לחמים"), completionHandler);
//

*/

        /*String[] listItems = new String[businessesList.size()];

        for (int i = 0; i < businessesList.size(); i++) {
            Business business = businessesList.get(i);
            listItems[i] = business.getName();
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        lvBusinesses.setAdapter(adapter);
*/

         /*@Override
                    public void onComplete(@NonNull Task<Location> location) {

                        if (location != null) {
                            //tvLastUpdate.setText(String.valueOf(location.getLatitude()));
                            tvLastUpdate.setText(String.valueOf(location.getResult().getLatitude()));

                            // Logic to handle location object
                            gotLocation = 100;

                        }
                        else {
                            gotLocation = 99;
                        }
                        tvLastUpdate.setText(String.valueOf(gotLocation));
                        tvLastUpdate.setText(String.valueOf(location.getResult().getLatitude()));
                    }
                    */
        /*if (location != null) {
            //userLocation = location.getResult().toString();
            lat = location.getResult().getLatitude();
            //tvLastUpdate.setText(userLocation);
            gotLocation = 50;
        }
        else {
            gotLocation = 49;
        }
        */


//tvLastUpdate.setText(Double.toString(lat));
//tvLastUpdate.setText(Integer.toString(gotLocation));
//tvLastUpdate.setText(String.valueOf(gotLocation));

