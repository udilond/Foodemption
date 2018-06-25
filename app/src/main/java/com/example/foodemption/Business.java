package com.example.foodemption;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Business {

    private String name;
    private String ID;
    private String type;
    private Double latitude;
    private Double longitude;
    private String address;
    private String city;
    private String hours;
    private String phoneNumber;
    private String logoURL;
    private String discountStartHour;
    private Float distanceFromUserLocation;
    //private Float distanceFromUserLocationToPresent;


    public static ArrayList<Business> getBusinessesFromFile(String JSONFileName, Context context) {
        final ArrayList<Business> businessesList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset(JSONFileName, context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray businesses = json.getJSONArray("businesses");

            // Get Business objects from data
            for (int i = 0; i < businesses.length(); i++) {
                Business business = new Business();

                business.ID = businesses.getJSONObject(i).getString("ID");
                business.name = businesses.getJSONObject(i).getString("name");
                business.type = businesses.getJSONObject(i).getString("type");
                business.latitude = businesses.getJSONObject(i).getDouble("latitude");
                business.longitude = businesses.getJSONObject(i).getDouble("longitude");
                business.address = businesses.getJSONObject(i).getString("address");
                business.city = businesses.getJSONObject(i).getString("city");
                business.hours = businesses.getJSONObject(i).getString("hours");
                business.phoneNumber = businesses.getJSONObject(i).getString("phoneNumber");
                business.logoURL = businesses.getJSONObject(i).getString("logoURL");
                business.discountStartHour = businesses.getJSONObject(i).getString("discountStartHour");
                business.distanceFromUserLocation = 0f;


                businessesList.add(business);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return businessesList;

    }

    private static String loadJsonFromAsset(String fileName, Context context) {
        String jsonInfo;

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonInfo = new String(buffer, "UTF-8");
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonInfo;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public String getType() {
        return type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getHours() {
        return hours;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public String getDiscountStartHour() {
        return discountStartHour;
    }

    public Float getDistanceFromUserLocation() {
        return distanceFromUserLocation;
    }


    public void setDistanceFromUserLocation(float distance) {
        distanceFromUserLocation = distance;
    }

    /*public void setDistanceFromUserLocationToPresent() {
        distanceFromUserLocationToPresent = new DecimalFormat("#.#").format(distanceFromUserLocation);

    }

    public Float getDistanceFromUserLocationToPresent() {
        return distanceFromUserLocationToPresent;

    }*/


}


    
    
