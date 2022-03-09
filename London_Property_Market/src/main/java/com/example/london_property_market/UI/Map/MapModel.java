package com.example.london_property_market.UI.Map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

public class MapModel {

    private final String API_URL = "http://api.postcodes.io/postcodes";
    private final String LONG_PREFIX = "lon";
    private final String LATIT_PREFIX = "lat";
    // Get borough from coordinates
    // API : https://findthatpostcode.uk/

    public String getBoroughID(double latitude, double longitude){
        String url = API_URL + "?" + LONG_PREFIX + "=" + longitude + LATIT_PREFIX + "=" + latitude;
        try {
            URLConnection urlConnection = new URL(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
