package com.example.london_property_market.UI;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.arcgisservices.LabelDefinition;
import com.esri.arcgisruntime.arcgisservices.LabelingPlacement;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.*;
import com.esri.arcgisruntime.mapping.labeling.ArcadeLabelExpression;
import com.esri.arcgisruntime.mapping.labeling.LabelExpression;
import com.esri.arcgisruntime.mapping.labeling.LabelOverlapStrategy;
import com.esri.arcgisruntime.mapping.labeling.SimpleLabelExpression;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.ColorUtil;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.example.london_property_market.UI.Map.GeoJsonCoordinatesParser;
import com.example.london_property_market.UI.Map.MapModel;
import com.example.london_property_market.UI.Map.Opacity;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class MainViewer extends Application {

    private final double LONDON_LONGITUDE = -0.14130290092735798;
    private final double LONDON_LATITUDE = 51.493866432732425;
    private final double MAP_SCALE = 372223.819286;

    private final String GEO_JSON_FOLDER_PATH = "src/main/resources/map/geoJson/";

    private MapView mapView;
    private HashMap<String, Graphic> polygons;
    private HashMap<Opacity, Integer> opacityMap;


    @Override
    public void start(Stage stage) throws IOException {

        ArcGISRuntimeEnvironment.setApiKey("AAPKc555c6c3e07d4271a12ea786c0965414qrGdevhwwXl16CIE4TsMZFaF4cWqrF3CPKVPZYuqul9SCtFrtWFVEgFeqNF-2Mpg");

        polygons = new HashMap<>();
        opacityMap = new HashMap<>();
        mapView = new MapView();
        fillOpacityMap();

        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_NAVIGATION);

        mapView.setMap(map);

        drawBoroughsBoundariesFromFolder();

        mapView.setViewpoint(new Viewpoint(LONDON_LATITUDE, LONDON_LONGITUDE, MAP_SCALE));
        mapView.setOnMouseClicked(this::mouse);

        Scene scene = new Scene(new BorderPane(mapView), 1020, 740);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private String[] getAllGeoJsonResources(){
        File geoJsonResFolder = new File(GEO_JSON_FOLDER_PATH);
        return geoJsonResFolder.list();
    }

    private void drawBoroughsBoundariesFromFolder(){
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        for (String fileName : getAllGeoJsonResources()) {

            PolygonBuilder polygon = new PolygonBuilder(GeoJsonCoordinatesParser.getPointCollectionFromGeoJsonCoordinates(GEO_JSON_FOLDER_PATH + fileName));
            Graphic polygonGraphic = new Graphic(polygon.toGeometry(), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, generateRandomHexWithOpacity(Opacity.ZERO_OPACITY), 3));
            polygonGraphic.getAttributes().put("Name", GeoJsonCoordinatesParser.getBoroughNameFromFile(GEO_JSON_FOLDER_PATH+fileName));
            graphicsOverlay.getGraphics().add(polygonGraphic);
            polygons.put(GEO_JSON_FOLDER_PATH+fileName, polygonGraphic);

        }

        addBoroughsLabels(graphicsOverlay);

    }

    private void addBoroughsLabels(GraphicsOverlay graphicsOverlay){
        LabelExpression lblExpression = new SimpleLabelExpression("[Name]");
        TextSymbol labelSymbol = new TextSymbol();
        labelSymbol.setColor(0xff5528f9);
        labelSymbol.setSize(12);
        LabelDefinition lbl = new LabelDefinition(lblExpression, labelSymbol);
        lbl.setPlacement(LabelingPlacement.POINT_ABOVE_CENTER);
        lbl.setMinScale(1372223.819286);
        lbl.setMaxScale(0);
        graphicsOverlay.setLabelsEnabled(true);
        graphicsOverlay.getLabelDefinitions().add(lbl);
    }

    private void fillOpacityMap(){
        opacityMap.put(Opacity.ZERO_OPACITY, 0xff000000);
        opacityMap.put(Opacity.DEFAULT_FILL_OPACITY, 0x33000000);
    }

    //https://stackoverflow.com/questions/28977308/read-all-lines-with-bufferedreader
    private void mouse(MouseEvent mouseEvent) {
        Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        Point mapPoint = mapView.screenToLocation(point);

        // Important as the user may click on the map before it completely loads
        if (mapPoint != null) {
            Point projectedPoint = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());
            MapModel mapModel = new MapModel();

            Pair<String, String> boroughInfo = mapModel.getBoroughName(projectedPoint.getX(), projectedPoint.getY(), polygons);

            if (boroughInfo.getLeft() != null && boroughInfo.getRight() != null) {
                // change fill when clicked

                if (JsonParser.parseString(polygons.get(boroughInfo.getLeft()).getSymbol().toJson()).getAsJsonObject().get("type").getAsString().equals("esriSLS"))
                    polygons.get(boroughInfo.getLeft()).setSymbol(new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, generateRandomHexWithOpacity(Opacity.DEFAULT_FILL_OPACITY), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, generateRandomHexWithOpacity(Opacity.ZERO_OPACITY), 2)));
                else
                    polygons.get(boroughInfo.getLeft()).setSymbol(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, generateRandomHexWithOpacity(Opacity.ZERO_OPACITY), 3));

            }
        }
    }

    private int generateRandomHexWithOpacity(Opacity opacity){
        Random rand = new Random();
        return opacityMap.get(opacity) + rand.nextInt(0xffffff + 1);
    }


    public static void main(String[] args) {
        launch();
    }

    /**
     * Stops and releases all resources used in application.
     */
    @Override
    public void stop() {
        if (mapView != null) {
            mapView.dispose();
        }
    }
}