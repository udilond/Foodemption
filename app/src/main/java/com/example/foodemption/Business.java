package com.example.foodemption;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Business {

        private String name;
        private String ID;
        private String type;
        private Double latitude;
        private Double longitude;
        private String address;
        private String hours;
        private String phoneNumber;
        private String logoURL;
        private String discountStartHour;


        public static Map<String, Business> getBusinessesFromFile(String JSONFileName, Context context) {
            Map<String, Business> businessesMap = new HashMap<>();

            try {
                // Load data
                String jsonString = loadJsonFromAsset(JSONFileName, context);
                JSONObject json = new JSONObject(jsonString);
                JSONArray businesses = json.getJSONArray("businesses");
                String length = String.valueOf(businesses.length());
//                businesses.get(0);

                // Get Business objects from data
                for(int i = 0; i < businesses.length(); i++){
                    Business business = new Business();

                    business.ID = businesses.getJSONObject(i).getString("ID");
                    business.name = businesses.getJSONObject(i).getString("name");
                    business.type = businesses.getJSONObject(i).getString("type");
                    business.latitude = businesses.getJSONObject(i).getDouble("latitude");
                    business.longitude = businesses.getJSONObject(i).getDouble("longitude");
                    business.address = businesses.getJSONObject(i).getString("address");
                    business.hours = businesses.getJSONObject(i).getString("hours");
                    business.phoneNumber = businesses.getJSONObject(i).getString("phone number");
                    business.logoURL = businesses.getJSONObject(i).getString("logo URL");
                    business.discountStartHour = businesses.getJSONObject(i).getString("discount start hour");

                    businessesMap.put(business.ID, business);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return businessesMap;

            }

        private static String loadJsonFromAsset(String fileName, Context context){
            String jsonInfo;

            try {
                InputStream inputStream = context.getAssets().open(fileName);
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                jsonInfo = new String(buffer, "UTF-8");
            }
            catch (java.io.IOException ex) {
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
}


    
    
