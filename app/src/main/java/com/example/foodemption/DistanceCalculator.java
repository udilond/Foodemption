package com.example.foodemption;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;

public class DistanceCalculator {

    private Location userLocation;
    private ArrayList<Business> businessList;

    public DistanceCalculator(Location userLocation, ArrayList<Business> businessList) {
        this.userLocation = userLocation;
        this.businessList = businessList;
    }

    public void calculateDistance() {

        //final Double lon = 32.079657;
        //final Double lat = 34.684563;

        Location businessLocation = new Location("");
        //businessLocation.setLatitude(0.0);
        //businessLocation.setLongitude(0.0);

        for (Business business : businessList) {
            //businessLocation.setLatitude(lon);
            //businessLocation.setLongitude(lat);
            businessLocation.setLatitude(business.getLatitude());
            businessLocation.setLongitude(business.getLongitude());
            float distance = userLocation.distanceTo(businessLocation);
            business.setDistanceFromUserLocation(distance);
        }

        Collections.sort(businessList, new BusinessComperator());
    }
}
