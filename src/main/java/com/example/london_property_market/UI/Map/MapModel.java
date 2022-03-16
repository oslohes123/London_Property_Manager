package com.example.london_property_market.UI.Map;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.sun.javafx.geom.Line2D;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;


public class MapModel {

    public Pair<String, String> getBoroughName(double longitude, double latitude, HashMap<String, Graphic> identifier){
        String fileName = getBoroughFileName(longitude, latitude, identifier);
        return new ImmutablePair<>(fileName, GeoJsonCoordinatesParser.getBoroughNameFromFile(fileName));
    }

    private String getBoroughFileName(double longitude, double latitude, HashMap<String, Graphic> identifier){

        for (String fileName: identifier.keySet()) {
            boolean insidePolygon = false;

            for (int i = 0; i < ((Polygon) identifier.get(fileName).getGeometry()).getParts().size(); i++)
                for (int j = 0; j < ((Polygon) identifier.get(fileName).getGeometry()).getParts().get(i).size(); j++)
                    if (intersects( ((Polygon) identifier.get(fileName).getGeometry()).getParts().get(i).get(j).getStartPoint(), ((Polygon) identifier.get(fileName).getGeometry()).getParts().get(i).get(j).getEndPoint(), new Point(longitude, latitude)))
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
