package com.example.foodemption;

import java.util.Comparator;

public class BusinessComperator implements Comparator<Business> {

    public int compare(Business business1, Business business2) {
        return Math.round(business1.getDistanceFromUserLocation() - business2.getDistanceFromUserLocation()); //converting float distance to int
    }

}
