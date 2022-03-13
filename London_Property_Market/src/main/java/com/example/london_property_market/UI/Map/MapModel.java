package com.example.london_property_market.UI.Map;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.sun.javafx.geom.Line2D;
import java.util.HashMap;


public class MapModel {

    public String getBoroughName(double longitude, double latitude, HashMap<String, Polygon> identifier){
        return GeoJsonCoordinatesParser.getBoroughNameFromFile(getBoroughFileName(longitude, latitude, identifier));
    }

    private String getBoroughFileName(double longitude, double latitude, HashMap<String, Polygon> identifier){

        for (String fileName: identifier.keySet()) {
            boolean insidePolygon = false;

            for (int i = 0; i < identifier.get(fileName).getParts().size(); i++)
                for (int j = 0; j < identifier.get(fileName).getParts().get(i).size(); j++)
                    if (intersects(identifier.get(fileName).getParts().get(i).get(j).getStartPoint(), identifier.get(fileName).getParts().get(i).get(j).getEndPoint(), new Point(longitude, latitude)))
                        insidePolygon = !insidePolygon;

            if (insidePolygon)
                return fileName;
        }

        return null;
    }

    private boolean intersects(Point a, Point b, Point target){
        Line2D border = new Line2D((float) a.getX(), (float) a.getY(), (float) b.getX(), (float) b.getY());
        Line2D rayCasted = new Line2D((float) target.getX(), (float) target.getY(), (float) target.getX(), 200);
        return rayCasted.intersectsLine(border);
    }

}
