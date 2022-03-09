package com.example.london_property_market.UI;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.KmlLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.*;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.ogc.kml.KmlDataset;
import com.example.london_property_market.UI.Map.MapModel;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewer extends Application {

    private final double LONDON_LONGITUDE = -0.14130290092735798;
    private final double LONDON_LATITUDE = 51.493866432732425;
    private final double MAP_SCALE = 372223.819286;

    private MapView mapView;


    @Override
    public void start(Stage stage) throws IOException {

        ArcGISRuntimeEnvironment.setApiKey("AAPKc555c6c3e07d4271a12ea786c0965414qrGdevhwwXl16CIE4TsMZFaF4cWqrF3CPKVPZYuqul9SCtFrtWFVEgFeqNF-2Mpg");

        mapView = new MapView();

        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_NAVIGATION);

        mapView.setMap(map);
        mapView.setEnableMousePan(false);
        mapView.setEnableMouseZoom(false);
        mapView.setEnableKeyboardNavigation(false);
        mapView.setEnableTouchPan(false);
        mapView.setEnableTouchRotate(false);
        mapView.setEnableTouchZoom(false);

        //https://www.google.com/maps/d/viewer?ptab=2&ie=UTF8&oe=UTF8&msa=0&mid=1t4G7Q0brBWa2_kKwYyEtoxmCd60&ll=51.54916761414271%2C-0.3551975488280934&z=9
        KmlLayer kmlLayer = new KmlLayer(new KmlDataset("src/main/resources/map/LondonBoroughs.kmz.kmz"));
        map.getOperationalLayers().add(kmlLayer);

        mapView.setViewpoint(new Viewpoint(LONDON_LATITUDE, LONDON_LONGITUDE, MAP_SCALE));
        mapView.setOnMouseClicked(this::mouse);

        Scene scene = new Scene(new BorderPane(mapView), 1020, 740);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    //https://stackoverflow.com/questions/28977308/read-all-lines-with-bufferedreader
    private void mouse(MouseEvent mouseEvent) {
        Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        Point mapPoint = mapView.screenToLocation(point);
        Point projectedPoint = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84()) ;

        MapModel mapModel = new MapModel();
        mapModel.getBoroughID(projectedPoint.getX(), projectedPoint.getY());
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