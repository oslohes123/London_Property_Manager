package com.example.london_property_market.UI.Map;

import com.esri.arcgisruntime.geometry.PartCollection;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.stream.Collectors;

public class GeoJsonCoordinatesParser {

    /**
     * This method returns a collection of parts, which every part is a collection of points that forms a polygon. This
     * methodology was chosen as ArcGIS does not include multiPolygons. This method returns the parts based on the
     * formatted geojson file.
     * Reference: https://stackoverflow.com/questions/28977308/read-all-lines-with-bufferedreader
     * @param coordinatesFilePath the path of the borough file.
     * @return collection of parts, which every part is a collection of points that forms a polygon.
     */
    public static PartCollection getPointCollectionFromGeoJsonCoordinates(String coordinatesFilePath){
        PartCollection parts = new PartCollection(SpatialReferences.getWgs84());

        InputStream boroughFileReader = GeoJsonCoordinatesParser.class.getResourceAsStream(coordinatesFilePath);

        BufferedReader coordinatesFileReader = new BufferedReader(new InputStreamReader(boroughFileReader));
        // Get the json array that has the parts of the (multi)polygon
        JsonArray coordinatesArray = JsonParser.parseString(coordinatesFileReader.lines().collect(Collectors.joining())).getAsJsonObject().get("features").getAsJsonArray().get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray();

        for (int i = 0; i < coordinatesArray.size(); i++) {

            PointCollection polygonCoordinateSet = new PointCollection(SpatialReferences.getWgs84());
            JsonArray pointSet = coordinatesArray.get(i).getAsJsonArray();

            /* GeoJson files which includes multiPolygons are one level lower than geoJson polygons array that includes
               the parts. This if statement checks if the polygon is an array of parts or not. The use of the type field
                included in the geojson may seem more sensible, however, the check if the polygon has more than one part
                seemed more sensible to allow more independence from the type itself and allow future expansions.*/
            if (coordinatesArray.get(i).getAsJsonArray().get(0).getAsJsonArray().get(0).isJsonArray())
                pointSet = pointSet.get(0).getAsJsonArray();

            for (int j = 0; j < pointSet.size(); j++)
                polygonCoordinateSet.add(pointSet.get(j).getAsJsonArray().get(0).getAsDouble(), pointSet.get(j).getAsJsonArray().get(1).getAsDouble());

            parts.add(polygonCoordinateSet);
        }

        return parts;
    }

    /**
     * This method returns the name of the borough in the specified file. The name of the borough is in the geojson file,
     * which this method extracts.
     * Reference: https://stackoverflow.com/questions/28977308/read-all-lines-with-bufferedreader
     * @param fileName the name of the file
     * @return the name of the borough
     */
    public static String getBoroughNameFromFile(String fileName){

        //The case where a location outside london is selected
        if (fileName == null)
            return null;

        InputStream boroughFileReader = GeoJsonCoordinatesParser.class.getResourceAsStream(fileName);

        BufferedReader coordinatesFileReader = new BufferedReader(new InputStreamReader(boroughFileReader));
        return JsonParser.parseString(coordinatesFileReader.lines().collect(Collectors.joining())).getAsJsonObject().get("features").getAsJsonArray().get(0).getAsJsonObject().get("properties").getAsJsonObject().get("name").toString().replaceAll("\"","");

    }

}
