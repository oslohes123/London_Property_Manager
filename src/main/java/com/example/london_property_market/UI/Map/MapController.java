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
import com.example.london_property_market.UI.PropertyViewer.PropertyController;
import com.example.london_property_market.UI.Welcome.MainModel;
import com.example.london_property_market.UI.Statistics.StatisticsController;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.controlsfx.control.ToggleSwitch;
import org.junit.internal.ExactComparisonCriteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * This class represents the map controller, which represents the second panel.
 * <p>
 * Reference: https://developers.arcgis.com/java/api-reference/reference/
 * Note that a common reference to all ArcGIS functionality is their API reference, which is only written here to reduce
 * redundancy since it almost was used in all of this class. But this reference was not used for the creating of this class,
 * but for the functionality that involves specific actions that are related to the ArcGIS map.
 *
 * @author Yousef Altaher, K20047484
 * @version 23-03-2022
 */
public class MapController implements FXMLIRRepresentable {

    // The API for the map
    private final String ARCGIS_API_KEY = "AAPKc555c6c3e07d4271a12ea786c0965414qrGdevhwwXl16CIE4TsMZFaF4cWqrF3CPKVPZYuqul9SCtFrtWFVEgFeqNF-2Mpg";

    // The geographical information to load london at the beginning.
    private final double LONDON_LONGITUDE = -0.14130290092735798;
    private final double LONDON_LATITUDE = 51.493866432732425;
    private final double MAP_SCALE = 372223.819286;

    // The map
    private MapView mapView;
    // An overlay that has the points that indicates properties
    private GraphicsOverlay propertyPointsOverlay;

    // The polygon, identifier, that has all the polygons, accessed by the file name
    private HashMap<String, Graphic> polygons;
    // A hashset to store the selected boroughs from the user in multi selection.
    private HashSet<String> selectedBoroughs;

    // The header controls - the selectors for the type and statistics with the button for properties
    private ToggleSwitch propertySelectionType;
    private ToggleSwitch statsSelectionType;
    private Button viewBoroughs;
    private Button openStats;

    // The map model
    private MapModel mapModel;

    /**
     * This method initialize the map view with its necessary objects. The use of a different method other than the constructor
     * serve as a generalization, as it will ease the creating of views from mainView.
     *
     * @return the pane for the view
     */
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

    /**
     * This method create the header layout and its UI elements.
     *
     * @return the layout that has the header controls (UIs).
     */
    private HBox getHeaderControls() {
        HBox headerControl = new HBox();
        headerControl.getStylesheets().add("Styles/views/mapViewControls.css");

        //https://github.com/controlsfx/controlsfx
        //https://controlsfx.github.io/javadoc/11.1.1/org.controlsfx.controls/org/controlsfx/control/ToggleSwitch.html
        propertySelectionType = new ToggleSwitch("Enable selection of multiple boroughs");
        propertySelectionType.getStyleClass().add("selectionType");

        statsSelectionType = new ToggleSwitch("View statistics from selected borough");
        statsSelectionType.getStyleClass().add("selectionType");

        //https://stackoverflow.com/questions/29616246/how-to-bind-inverse-boolean-javafx
        viewBoroughs = new Button("View multiple boroughs");
        viewBoroughs.setOnAction(this::openPropertyViewer);
        viewBoroughs.getStyleClass().add("controlButtons");
        viewBoroughs.disableProperty().bind(propertySelectionType.selectedProperty().not());

        openStats = new Button("Statistics");
        openStats.setOnAction(this::openStatsWindow);
        openStats.disableProperty().bind(statsSelectionType.selectedProperty().not());
        openStats.getStyleClass().add("controlButtons");


        headerControl.getChildren().addAll(propertySelectionType, viewBoroughs, statsSelectionType, openStats);
        return headerControl;
    }

    /**
     * This method opens the property viewer for the selected boroughs
     *
     * @param actionEvent actionEvent
     */
    private void openPropertyViewer(ActionEvent actionEvent) {
        try {
            if (selectedBoroughs.size() == 0) {
                noBoroughsSelected();
            } else {
                PropertyController viewProperties = new PropertyController(selectedBoroughs, MainModel.getMinAmount(), MainModel.getMaxAmount());
                Stage stage = viewProperties.getStage();
                stage.show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method opens the property viewer for one borough.
     * The reason behind the existence of two methods is to allow the user to save their selection after changing the
     * mode to one borough
     *
     * @param boroughName borough name
     */
    private void openPropertyViewer(String boroughName) {

    }

    private void noBoroughsSelected() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No boroughs selected");
        alert.setHeaderText("You need to select a borough first");
        alert.setContentText("Select 1 or more boroughs");
        alert.show();

    }


    /**
     * This method opens the statistics windows for the selected panel
     *
     * @param actionEvent actionEvent
     */
    private void openStatsWindow(ActionEvent actionEvent) {
        try {


            if (statsSelectionType.isSelected()) {
                FXMLLoader statsLoader = new FXMLLoader(getClass().getResource("/views/StatsView.fxml"));
                Parent root = statsLoader.load();

                StatisticsController statisticsController = statsLoader.getController();
                statisticsController.setBoroughsList(new ArrayList<>(selectedBoroughs));

                Scene statsScene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Statistics");
                stage.setScene(statsScene);
                stage.show();
                // pass the hashset itself
            } else {
                // pass null, which will indicate *

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * This method draws the boundaries of london on the map based on the files that are specified on the mapModel.
     * Reference: https://developers.arcgis.com/java/maps-2d/tutorials/add-a-point-line-and-polygon/
     */
    private void drawBoroughsBoundariesFromFolder() {
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        for (String fileName : mapModel.getAllGeoJsonResources()) {

            PolygonBuilder polygon = new PolygonBuilder(GeoJsonCoordinatesParser.getPointCollectionFromGeoJsonCoordinates(mapModel.getGEO_JSON_FOLDER_PATH() + fileName));
            Graphic polygonGraphic = new Graphic(polygon.toGeometry(), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, mapModel.generateRandomHexWithOpacity(Opacity.ZERO_OPACITY), 3));
            polygonGraphic.getAttributes().put("Name", GeoJsonCoordinatesParser.getBoroughNameFromFile(mapModel.getGEO_JSON_FOLDER_PATH() + fileName));
            graphicsOverlay.getGraphics().add(polygonGraphic);
            polygons.put(mapModel.getGEO_JSON_FOLDER_PATH() + fileName, polygonGraphic);

        }

        addBoroughsLabels(graphicsOverlay);

    }

    /**
     * This method add labels (borough names) to the map.
     * <p>
     * Note: the references have used json encoding. The developer have chosen to follow the coding approach for more
     * consistency and control, but the reference is kept as the developer have used it, along with the documentation,
     * to locate the attributes and to understand their mechanism.
     * <p>
     * References: https://www.youtube.com/watch?v=bLUwuK5ZpHM&t=1585s
     *
     * @param graphicsOverlay the overlay that the labels will be added to
     */
    private void addBoroughsLabels(GraphicsOverlay graphicsOverlay) {
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

    /**
     * This method takes the mouse input of the user and color the borough that the user has selected.
     * <p>
     * References: https://developers.arcgis.com/java/sample-code/show-callout/, https://www.youtube.com/watch?v=bLUwuK5ZpHM&t=1585s
     *
     * @param mouseEvent mouseEvent
     */
    private void onMouseClick(MouseEvent mouseEvent) {
        Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        Point mapPoint = mapView.screenToLocation(point);

        // Important as the user may click on the map before it completely loads
        if (mapPoint != null) {

            Point projectedPoint = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());
            Pair<String, String> boroughInfo = mapModel.getBoroughName(projectedPoint.getX(), projectedPoint.getY(), polygons);

            // check if the user is on single or multiple borough mode
            if (!propertySelectionType.isSelected()) {
                openPropertyViewer(boroughInfo.getRight());
            } else {

                // Important as users may click a location outside london
                if (boroughInfo.getLeft() != null && boroughInfo.getRight() != null) {

                    // change fill when clicked
                    if (JsonParser.parseString(polygons.get(boroughInfo.getLeft()).getSymbol().toJson()).getAsJsonObject().get("type").getAsString().equals("esriSLS")) {
                        polygons.get(boroughInfo.getLeft()).setSymbol(new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, mapModel.generateRandomHexWithOpacity(Opacity.DEFAULT_FILL_OPACITY), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, mapModel.generateRandomHexWithOpacity(Opacity.ZERO_OPACITY), 2)));
                        selectedBoroughs.add(boroughInfo.getRight());
                    } else {
                        polygons.get(boroughInfo.getLeft()).setSymbol(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, mapModel.generateRandomHexWithOpacity(Opacity.ZERO_OPACITY), 3));
                        selectedBoroughs.remove(boroughInfo.getRight());
                    }
                }

            }

        }
    }

    /**
     * This method perform the task of updating the landmarks that indicates properties on the map.
     * Reference: https://www.youtube.com/watch?v=bLUwuK5ZpHM&t=1585s
     */
    @Override
    public void onChangeInformation() {
        if (mapView != null) {
            propertyPointsOverlay.getGraphics().clear();

            Image propertyIcon = new Image(getClass().getResourceAsStream("/icon/property_clipart.png"), 0, 40, true, true);
            PictureMarkerSymbol propertyIconSymbol = new PictureMarkerSymbol(propertyIcon);
            SimpleRenderer propertyRenderer = new SimpleRenderer(propertyIconSymbol);

            propertyPointsOverlay.setRenderer(propertyRenderer);

            for (Pair<Double, Double> locationPair : mapModel.retrieveApplicableLocations()) {
                Point point = new Point(locationPair.getLeft(), locationPair.getRight(), SpatialReferences.getWgs84());
                Graphic pointGraphic = new Graphic(point);

                // add the point graphic to the graphics overlay
                propertyPointsOverlay.getGraphics().add(pointGraphic);
            }

        }
    }


}
