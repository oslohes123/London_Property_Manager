package com.example.london_property_market.UI.Map;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.sun.javafx.geom.Line2D;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;


/**
 * This class contains the model of the mapView.
 * @author Yousef Altaher
 * @version 18-03-2022
 */
public class MapModel {

    /**
     * This method returns the name of the borough that a point is in. This method acts as a helper method for the
     * privately defined method that perform the main algorithm for locating the borough.
     * @param longitude the longitude of a point
     * @param latitude the latitude of a point
     * @param identifier the map that contains, and hence identify, all the boroughs as Name : polygon. Polygon are the
     *                  way that a borough is drawn on the map.
     * @return the name of the borough that a point is in
     */
    public Pair<String, String> getBoroughName(double longitude, double latitude, HashMap<String, Graphic> identifier){
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
