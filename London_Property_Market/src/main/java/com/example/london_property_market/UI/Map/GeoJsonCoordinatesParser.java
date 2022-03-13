package com.example.london_property_market.UI.Map;

import com.esri.arcgisruntime.geometry.PartCollection;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.stream.Collectors;

public class GeoJsonCoordinatesParser {

    public static PartCollection getPointCollectionFromGeoJsonCoordinates(String coordinatesFilePath){
        PartCollection parts = new PartCollection(SpatialReferences.getWgs84());

        try {

            BufferedReader coordinatesFileReader = new BufferedReader(new FileReader(coordinatesFilePath));
            JsonArray coordinatesArray = JsonParser.parseString(coordinatesFileReader.lines().collect(Collectors.joining())).getAsJsonObject().get("features").getAsJsonArray().get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray();

            for (int i = 0; i < coordinatesArray.size(); i++) {

                PointCollection polygonCoordinateSet = new PointCollection(SpatialReferences.getWgs84());
                JsonArray pointSet = coordinatesArray.get(i).getAsJsonArray();

                if (coordinatesArray.get(i).getAsJsonArray().get(0).getAsJsonArray().get(0).isJsonArray())
                    pointSet = pointSet.get(0).getAsJsonArray();

                for (int j = 0; j < pointSet.size(); j++)
                    polygonCoordinateSet.add(pointSet.get(j).getAsJsonArray().get(0).getAsDouble(), pointSet.get(j).getAsJsonArray().get(1).getAsDouble());

                parts.add(polygonCoordinateSet);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return parts;
    }

    public static String getBoroughNameFromFile(String fileName){

        //The case where a location outside london is selected
        if (fileName == null)
            return null;

        try {

            BufferedReader coordinatesFileReader = new BufferedReader(new FileReader(fileName));
            return JsonParser.parseString(coordinatesFileReader.lines().collect(Collectors.joining())).getAsJsonObject().get("features").getAsJsonArray().get(0).getAsJsonObject().get("properties").getAsJsonObject().get("name").toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
