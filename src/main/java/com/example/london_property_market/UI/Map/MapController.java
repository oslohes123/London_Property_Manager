package com.example.london_property_market.UI.Map;


import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.arcgisservices.LabelDefinition;
import com.esri.arcgisruntime.arcgisservices.LabelingPlacement;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PolygonBuilder;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.labeling.LabelExpression;
import com.esri.arcgisruntime.mapping.labeling.SimpleLabelExpression;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.*;
import com.example.london_property_market.Loader.CsvLoader;
import com.example.london_property_market.UI.FXMLIRRepresentable;
import com.example.london_property_market.UI.Welcome.MainModel;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class MapController implements FXMLIRRepresentable {

    private final String ARCGIS_API_KEY = "AAPKc555c6c3e07d4271a12ea786c0965414qrGdevhwwXl16CIE4TsMZFaF4cWqrF3CPKVPZYuqul9SCtFrtWFVEgFeqNF-2Mpg";
    private final String GEO_JSON_FOLDER_PATH = "src/main/resources/map/geoJson/";


    private final double LONDON_LONGITUDE = -0.14130290092735798;
    private final double LONDON_LATITUDE = 51.493866432732425;
    private final double MAP_SCALE = 372223.819286;


    private MapView mapView;
    private GraphicsOverlay propertyPointsOverlay;

    private HashMap<String, Graphic> polygons;
    private HashMap<Opacity, Integer> opacityMap;
    HashSet<String> selectedBoroughs;
    private ToggleGroup selectionType;
    private Button viewBoroughs;

    @Override
    public BorderPane initialize() {
        BorderPane mainPane = new BorderPane();
        ArcGISRuntimeEnvironment.setApiKey(ARCGIS_API_KEY);

        selectedBoroughs = new HashSet<>();
        mainPane.setTop(getHeaderControls());

        polygons = new HashMap<>();
        opacityMap = new HashMap<>();
        mapView = new MapView();
        fillOpacityMap();

        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_NAVIGATION);
        mapView.setMap(map);

        drawBoroughsBoundariesFromFolder();

        mapView.setViewpoint(new Viewpoint(LONDON_LATITUDE, LONDON_LONGITUDE, MAP_SCALE));
        mapView.setOnMouseClicked(this::onMouseClick);
        mainPane.setCenter(mapView);

        propertyPointsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(propertyPointsOverlay);

        return mainPane;
    }

    private HBox getHeaderControls(){
        HBox headerControl = new HBox();

        selectionType = new ToggleGroup();

        ToggleButton singleBoroughToggle = new ToggleButton();
        ToggleButton multipleBoroughToggle = new ToggleButton();

        singleBoroughToggle.setText("Single Borough");
        multipleBoroughToggle.setText("Multiple Borough");

        singleBoroughToggle.getProperties().put("id", SelectionOptions.SINGLE_BOROUGH);
        multipleBoroughToggle.getProperties().put("id", SelectionOptions.MULTIPLE_BOROUGH);

        singleBoroughToggle.setSelected(true);

        singleBoroughToggle.setOnAction(this::disableViewButton);
        multipleBoroughToggle.setOnAction(this::enableViewButton);

        selectionType.getToggles().addAll(singleBoroughToggle, multipleBoroughToggle);


        viewBoroughs = new Button("View multiple boroughs");
        viewBoroughs.setDisable(true);
        viewBoroughs.setOnAction(this::openPropertyViewer);

        headerControl.getChildren().addAll(singleBoroughToggle, multipleBoroughToggle, viewBoroughs);
        return headerControl;
    }

    private void enableViewButton(ActionEvent actionEvent) {
        viewBoroughs.setDisable(false);
    }

    private void disableViewButton(ActionEvent actionEvent) {
        viewBoroughs.setDisable(true);
    }


    private void openPropertyViewer(ActionEvent actionEvent) {
        System.out.println("opened");
    }

    private void fillOpacityMap(){
        opacityMap.put(Opacity.ZERO_OPACITY, 0xff000000);
        opacityMap.put(Opacity.DEFAULT_FILL_OPACITY, 0x33000000);
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
        labelSymbol.setFontFamily("Constantia");
        labelSymbol.setHaloWidth(1);
        labelSymbol.setHaloColor(0xffbdffbd);

        LabelDefinition lbl = new LabelDefinition(lblExpression, labelSymbol);
        lbl.setPlacement(LabelingPlacement.POINT_ABOVE_CENTER);
        lbl.setMinScale(1372223.819286);
        lbl.setMaxScale(0);
        graphicsOverlay.setLabelsEnabled(true);
        graphicsOverlay.getLabelDefinitions().add(lbl);
    }

    //https://stackoverflow.com/questions/28977308/read-all-lines-with-bufferedreader
    private void onMouseClick(MouseEvent mouseEvent) {
        Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        Point mapPoint = mapView.screenToLocation(point);

        // Important as the user may click on the map before it completely loads
        if (mapPoint != null) {
            Point projectedPoint = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());
            MapModel mapModel = new MapModel();

            Pair<String, String> boroughInfo = mapModel.getBoroughName(projectedPoint.getX(), projectedPoint.getY(), polygons);

            if (selectionType.getSelectedToggle().getProperties().get("id").equals(SelectionOptions.SINGLE_BOROUGH)){
                openPropertyViewer(null);
            }else {

                if (boroughInfo.getLeft() != null && boroughInfo.getRight() != null) {
                    // change fill when clicked

                    if (JsonParser.parseString(polygons.get(boroughInfo.getLeft()).getSymbol().toJson()).getAsJsonObject().get("type").getAsString().equals("esriSLS")) {
                        polygons.get(boroughInfo.getLeft()).setSymbol(new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, generateRandomHexWithOpacity(Opacity.DEFAULT_FILL_OPACITY), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, generateRandomHexWithOpacity(Opacity.ZERO_OPACITY), 2)));
                        selectedBoroughs.add(boroughInfo.getRight());
                    }else {
                        polygons.get(boroughInfo.getLeft()).setSymbol(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, generateRandomHexWithOpacity(Opacity.ZERO_OPACITY), 3));
                        selectedBoroughs.remove(boroughInfo.getRight());
                    }
                }

            }

        }
    }

    private int generateRandomHexWithOpacity(Opacity opacity){
        Random rand = new Random();
        return opacityMap.get(opacity) + rand.nextInt(0xffffff + 1);
    }

    //https://www.youtube.com/watch?v=bLUwuK5ZpHM&t=1585s
    @Override
    public void onChangeInformation(){
        if (mapView != null) {
            propertyPointsOverlay.getGraphics().clear();

            Image propertyIcon = new Image(getClass().getResourceAsStream("/icon/property_clipart.png"),0,40,true, true);
            PictureMarkerSymbol propertyIconSymbol = new PictureMarkerSymbol(propertyIcon);
            SimpleRenderer propertyRenderer = new SimpleRenderer(propertyIconSymbol);

            propertyPointsOverlay.setRenderer(propertyRenderer);

            for (Pair<Double,Double> locationPair : retrieveApplicableLocations()){
                Point point = new Point(locationPair.getLeft(), locationPair.getRight(), SpatialReferences.getWgs84());
                Graphic pointGraphic = new Graphic(point);

                // add the point graphic to the graphics overlay
                propertyPointsOverlay.getGraphics().add(pointGraphic);
            }

        }
    }

    private HashSet<Pair<Double, Double>> retrieveApplicableLocations(){
        HashSet<Pair<Double, Double>> locations = new HashSet<>();

        double minSearchAmount = MainModel.getMinAmount();
        double maxSearchAmount = MainModel.getMaxAmount();
        CsvLoader csvLoader = new CsvLoader();
        ResultSet resultedLocations = csvLoader.executeQuery("SELECT longitude, latitude FROM Locations WHERE price >=" + minSearchAmount + " AND price <=" + maxSearchAmount);

        try {
            resultedLocations.next(); // To skip the header
            while (resultedLocations.next())
                locations.add(new ImmutablePair(Double.parseDouble(resultedLocations.getString(1)), Double.parseDouble(resultedLocations.getString(2))));

        }catch (SQLException exception){
                exception.printStackTrace();
        }

        return locations;
    }

}
