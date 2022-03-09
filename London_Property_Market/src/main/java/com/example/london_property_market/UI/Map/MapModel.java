package com.example.london_property_market.UI.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

public class MapModel {

    private final String API_URL = "http://api.postcodes.io/postcodes";
    private final String LONG_PREFIX = "lon";
    private final String LATIT_PREFIX = "lat";

    public String getBoroughID(double longitude, double latitude){
        String url = API_URL + "?" + LONG_PREFIX + "=" + longitude + "&" + LATIT_PREFIX + "=" + latitude;
        String borough = "";

        try {
            URLConnection urlConnection = new URL(url).openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            //https://stackoverflow.com/questions/28977308/read-all-lines-with-bufferedreader
            JsonObject jsonObject = JsonParser.parseString(bufferedReader.lines().collect(Collectors.joining())).getAsJsonObject();

            if (!isValidAddress(jsonObject))
                return null;


            JsonArray jsonArray = jsonObject.getAsJsonArray("result");
            borough = jsonArray.get(0).getAsJsonObject().get("admin_district").getAsString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return borough;
    }

    private boolean isValidAddress(JsonObject jsonObject){
        return jsonObject.get("result") != JsonNull.INSTANCE && jsonObject.getAsJsonArray("result").get(0).getAsJsonObject().get("region").getAsString().equals("London");
    }

}
