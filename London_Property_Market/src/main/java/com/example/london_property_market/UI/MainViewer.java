package com.example.london_property_market.UI;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.*;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.example.london_property_market.UI.Map.GeoJsonCoordinatesParser;
import com.example.london_property_market.UI.Map.MapModel;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MainViewer extends Application {

    private final double LONDON_LONGITUDE = -0.14130290092735798;
    private final double LONDON_LATITUDE = 51.493866432732425;
    private final double MAP_SCALE = 372223.819286;
    private final String GEO_JSON_FOLDER_PATH = "src/main/resources/map/geoJson/";

    private MapView mapView;


    @Override
    public void start(Stage stage) throws IOException {

        ArcGISRuntimeEnvironment.setApiKey("AAPKc555c6c3e07d4271a12ea786c0965414qrGdevhwwXl16CIE4TsMZFaF4cWqrF3CPKVPZYuqul9SCtFrtWFVEgFeqNF-2Mpg");

        mapView = new MapView();

        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_NAVIGATION);

        mapView.setMap(map);
//        mapView.setEnableMousePan(false);
//        mapView.setEnableMouseZoom(false);
//        mapView.setEnableKeyboardNavigation(false);
//        mapView.setEnableTouchPan(false);
//        mapView.setEnableTouchRotate(false);
//        mapView.setEnableTouchZoom(false);

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
            Random rand = new Random();
            int rand_num = 0xff000000 + rand.nextInt(0xffffff + 1);

            for (PointCollection subPolygon : GeoJsonCoordinatesParser.getPointCollectionFromGeoJsonCoordinates("src/main/resources/map/geoJson/" + fileName)) {
                Polygon polyline = new Polygon(subPolygon);
                Graphic polygonGraphic = new Graphic(polyline, new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, rand_num, 3));
                graphicsOverlay.getGraphics().add(polygonGraphic);
            }
        }

    }

    //https://stackoverflow.com/questions/28977308/read-all-lines-with-bufferedreader
    private void mouse(MouseEvent mouseEvent) {
        Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        Point mapPoint = mapView.screenToLocation(point);
        // Important as the user may click on the map before it completely loads
        if (mapPoint != null) {
            Point projectedPoint = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());
            MapModel mapModel = new MapModel();
            mapModel.getBoroughID(projectedPoint.getX(), projectedPoint.getY());
        }
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