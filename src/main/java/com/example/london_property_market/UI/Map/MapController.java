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
import com.example.london_property_market.UI.FXMLIRRepresentable;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.controlsfx.control.ToggleSwitch;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class MapController implements FXMLIRRepresentable {

    private final String ARCGIS_API_KEY = "AAPKc555c6c3e07d4271a12ea786c0965414qrGdevhwwXl16CIE4TsMZFaF4cWqrF3CPKVPZYuqul9SCtFrtWFVEgFeqNF-2Mpg";


    private final double LONDON_LONGITUDE = -0.14130290092735798;
    private final double LONDON_LATITUDE = 51.493866432732425;
    private final double MAP_SCALE = 372223.819286;


    private MapView mapView;
    private GraphicsOverlay propertyPointsOverlay;

    private HashMap<String, Graphic> polygons;
    private HashSet<String> selectedBoroughs;
    private ToggleSwitch propertySelectionType;
    private ToggleSwitch statsSelectionType;
    private Button viewBoroughs;



    MapModel mapModel;


    @Override
    public BorderPane initialize() {
        BorderPane mainPane = new BorderPane();
        ArcGISRuntimeEnvironment.setApiKey(ARCGIS_API_KEY);

        mapModel = new MapModel();

        selectedBoroughs = new HashSet<>();
        mainPane.setTop(getHeaderControls());

        polygons = new HashMap<>();
        mapView = new MapView();

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
        headerControl.getStylesheets().add("Styles/views/mapViewControls.css");

        //https://github.com/controlsfx/controlsfx
        //https://controlsfx.github.io/javadoc/11.1.1/org.controlsfx.controls/org/controlsfx/control/ToggleSwitch.html
        propertySelectionType = new ToggleSwitch("Enable selection of multiple boroughs");
        propertySelectionType.getStyleClass().add("selectionType");

        statsSelectionType = new ToggleSwitch("View statistics from selected borough");
        statsSelectionType.getStyleClass().add("selectionType");

        //https://stackoverflow.com/questions/29616246/how-to-bind-inverse-boolean-javafx
        //
        viewBoroughs = new Button("View multiple boroughs");
        viewBoroughs.setOnAction(this::openPropertyViewer);
        viewBoroughs.getStyleClass().add("controlButtons");
        viewBoroughs.disableProperty().bind(propertySelectionType.selectedProperty().not());

        Button openStats = new Button("Statistics");
        openStats.setOnAction(this::openStatsWindow);
        openStats.getStyleClass().add("controlButtons");


        headerControl.getChildren().addAll(propertySelectionType, viewBoroughs, statsSelectionType, openStats);
        return headerControl;
    }

    private void openPropertyViewer(ActionEvent actionEvent) {

    }

    private void openPropertyViewer(String boroughName){

    }


    private void openStatsWindow(ActionEvent actionEvent) {
        try{
            if (statsSelectionType.isSelected()){
                FXMLLoader statsLoader = new FXMLLoader(getClass().getResource("/views/StatsView.fxml"));
                AnchorPane statsAnchorPane = statsLoader.load();
                Scene statsScene = new Scene(statsAnchorPane, 600, 400);
                Stage stage = new Stage();
                stage.setTitle("Statistics");
                stage.setScene(statsScene);
                stage.show();
                // pass the hashset itself
            }else{
                // pass null, which will indicate *
            }
        }catch(Exception e ){
            e.printStackTrace();
        }


    }

    private void drawBoroughsBoundariesFromFolder(){
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        for (String fileName : mapModel.getAllGeoJsonResources()) {

            PolygonBuilder polygon = new PolygonBuilder(GeoJsonCoordinatesParser.getPointCollectionFromGeoJsonCoordinates(mapModel.getGEO_JSON_FOLDER_PATH() + fileName));
            Graphic polygonGraphic = new Graphic(polygon.toGeometry(), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, mapModel.generateRandomHexWithOpacity(Opacity.ZERO_OPACITY), 3));
            polygonGraphic.getAttributes().put("Name", GeoJsonCoordinatesParser.getBoroughNameFromFile(mapModel.getGEO_JSON_FOLDER_PATH()+fileName));
            graphicsOverlay.getGraphics().add(polygonGraphic);
            polygons.put(mapModel.getGEO_JSON_FOLDER_PATH()+fileName, polygonGraphic);

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

            Pair<String, String> boroughInfo = mapModel.getBoroughName(projectedPoint.getX(), projectedPoint.getY(), polygons);

            if (!propertySelectionType.isSelected()){
                openPropertyViewer(boroughInfo.getRight());
            }else {

                if (boroughInfo.getLeft() != null && boroughInfo.getRight() != null) {
                    // change fill when clicked

                    if (JsonParser.parseString(polygons.get(boroughInfo.getLeft()).getSymbol().toJson()).getAsJsonObject().get("type").getAsString().equals("esriSLS")) {
                        polygons.get(boroughInfo.getLeft()).setSymbol(new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, mapModel.generateRandomHexWithOpacity(Opacity.DEFAULT_FILL_OPACITY), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, mapModel.generateRandomHexWithOpacity(Opacity.ZERO_OPACITY), 2)));
                        selectedBoroughs.add(boroughInfo.getRight());
                    }else {
                        polygons.get(boroughInfo.getLeft()).setSymbol(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, mapModel.generateRandomHexWithOpacity(Opacity.ZERO_OPACITY), 3));
                        selectedBoroughs.remove(boroughInfo.getRight());
                    }
                }

            }

        }
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

            for (Pair<Double,Double> locationPair : mapModel.retrieveApplicableLocations()){
                Point point = new Point(locationPair.getLeft(), locationPair.getRight(), SpatialReferences.getWgs84());
                Graphic pointGraphic = new Graphic(point);

                // add the point graphic to the graphics overlay
                propertyPointsOverlay.getGraphics().add(pointGraphic);
            }

        }
    }


}
