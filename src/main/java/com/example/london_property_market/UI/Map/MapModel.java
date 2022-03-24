package com.example.london_property_market.UI.Map;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.example.london_property_market.Loader.CsvLoader;
import com.example.london_property_market.UI.Welcome.MainModel;
import com.sun.javafx.geom.Line2D;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;


/**
 * This class contains the model of the mapView.
 * @author Yousef Altaher
 * @version 18-03-2022
 */
public class MapModel {

    // The path to the folder that contains the geojson file
    private final String GEO_JSON_FOLDER_PATH = "/map/geoJson/";
    // The hashmap that will store the opacities
    private HashMap<Opacity, Integer> opacityMap;

    /**
     * This constructor creates a mapModel object with initializing its necessary variables
     */
    public MapModel(){
        opacityMap = new HashMap<>();
        fillOpacityMap();
    }

    /**
     * This method returns a hash set that contains locations pairs (Longitude, Latitude) of properties that are within
     * the selected range
     * @return hash set that contains locations pairs (Longitude, Latitude) of properties that are within the selected
     * range
     */
    protected HashSet<Pair<Double, Double>> retrieveApplicableLocations(){
        HashSet<Pair<Double, Double>> locations = new HashSet<>();

        double minSearchAmount = MainModel.getMinAmount();
        double maxSearchAmount = MainModel.getMaxAmount();

        CsvLoader csvLoader = new CsvLoader();
        ResultSet resultedLocations = csvLoader.executeQuery("SELECT longitude, latitude FROM airbnb_locations WHERE price >=" + minSearchAmount + " AND price <=" + maxSearchAmount);

        try {
            resultedLocations.next(); // To skip the header
            while (resultedLocations.next())
                locations.add(new ImmutablePair(Double.parseDouble(resultedLocations.getString(1)), Double.parseDouble(resultedLocations.getString(2))));

        }catch (SQLException exception){
            exception.printStackTrace();
        }

        return locations;
    }


    /**
     * This method populate the opacity map with the element's corresponding values.
     */
    protected void fillOpacityMap(){
        opacityMap.put(Opacity.ZERO_OPACITY, 0xff000000);
        opacityMap.put(Opacity.DEFAULT_FILL_OPACITY, 0x33000000);
    }

    /**
     * This method returns a list that contains the file names in the geojson folder
     * REFERENCE: https://stackoverflow.com/questions/28985379/java-how-to-read-folder-and-list-files-in-that-folder-in-jar-environment-instead
     * @return a list that contains the file names in the geojson folder
     */
    protected String[] getAllGeoJsonResources(){
        InputStream is = getClass().getResourceAsStream(GEO_JSON_FOLDER_PATH);
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        return br.lines().toArray(String[]::new);
    }

    /**
     * This method returns a random hex color with the requested opacity
     * @param opacity the required opacity for the generated color
     * @return a random hex color with the requested opacity
     */
    protected int generateRandomHexWithOpacity(Opacity opacity){
        return opacityMap.get(opacity) + new Random().nextInt(0xffffff + 1);
    }

    /**
     * This method returns the path for the folder that contains the geojson files
     * @return the path for the folder that contains the geojson files
     */
    protected String getGEO_JSON_FOLDER_PATH() {
        return GEO_JSON_FOLDER_PATH;
    }

    /**
     * This method returns the name of the borough that a point is in. This method acts as a helper method for the
     * privately defined method that perform the main algorithm for locating the borough.
     * @param longitude the longitude of a point
     * @param latitude the latitude of a point
     * @param identifier the map that contains, and hence identify, all the boroughs as Name : polygon. Polygon are the
     *                  way that a borough is drawn on the map and which the algorithm needs to interact with.
     * @return the name of the borough that a point is in
     */
    protected Pair<String, String> getBoroughName(double longitude, double latitude, HashMap<String, Graphic> identifier){
        String fileName = getBoroughFileName(longitude, latitude, identifier);
        return new ImmutablePair<>(fileName, GeoJsonCoordinatesParser.getBoroughNameFromFile(fileName));
    }

    /**
     * This method implements the ray casting algorithm to determine if a point is inside the polygon, a borough, or
     * outside it.
     *
     * Ray casting : https://en.wikipedia.org/wiki/Point_in_polygon#Ray_casting_algorithm
     *
     * @param longitude the longitude of a point
     * @param latitude the latitude of a point
     * @param identifier the map that contains, and hence identify, all the boroughs as Name : polygon. Polygon are the
     *                  way that a borough is drawn on the map.
     * @return the name of the borough that a point is in
     */
    private String getBoroughFileName(double longitude, double latitude, HashMap<String, Graphic> identifier){

        for (String fileName: identifier.keySet()) {
            boolean insidePolygon = false;

            // It is important to every part of the polygon separately as multipolygons are represented in ArcGIS as
            // polygons with distinct parts.
            for (int i = 0; i < ((Polygon) identifier.get(fileName).getGeometry()).getParts().size(); i++)
                for (int j = 0; j < ((Polygon) identifier.get(fileName).getGeometry()).getParts().get(i).size(); j++)
                    if (intersects( ((Polygon) identifier.get(fileName).getGeometry()).getParts().get(i).get(j).getStartPoint(), ((Polygon) identifier.get(fileName).getGeometry()).getParts().get(i).get(j).getEndPoint(), new Point(longitude, latitude)))
                        insidePolygon = !insidePolygon;

            if (insidePolygon)
                return fileName;
        }

        return null;
    }

    /**
     *
     * This method checks if the line formed by a and b intersects with the line that is formed by extending the target
     * point in the horizontal direction
     *
     * @param a the first point in the line
     * @param b the second point in the line
     * @param target the targeted point
     * @return true if the line formed by a and b intersects with the line that is formed by extending the target point
     * in the horizontal direction, false otherwise
     */
    private boolean intersects(Point a, Point b, Point target){
        Line2D border = new Line2D((float) a.getX(), (float) a.getY(), (float) b.getX(), (float) b.getY());
        Line2D rayCasted = new Line2D((float) target.getX(), (float) target.getY(), (float) target.getX(), 200);
        return rayCasted.intersectsLine(border);
    }

}
