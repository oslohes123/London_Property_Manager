package com.example.london_property_market.UI.PropertyViewer;

import com.example.london_property_market.Loader.CsvLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * The class that will show all the property listings
 * that are in one of the selected boroughs, and is within
 * the selected price range.
 * These values will be passed in by the map page.
 * @author Shaheer Effandi (K21013734)
 */
public class PropertyView {
    @FXML
    private HBox propertyDisplayBox;
    private CsvLoader loader;
    private PropertyModel model;
    private Scene scene;
    private Stage stage;

    /**
     * Constructor for DisplayView Class
     *
     * @param boroughs
     * @param minPrice
     * @param maxPrice
     */
    public PropertyView(Set<String> boroughs, int minPrice, int maxPrice) throws IOException, SQLException {

        URL url = getClass().getResource("views/PropertyDisplay.fxml");
        Parent root = FXMLLoader.load(url);
        scene = new Scene(root);


        model = new PropertyModel(boroughs, minPrice, maxPrice);
        addPropertiesToViewer();

        stage.setScene(scene);
        stage.setTitle(model.createStageTitle());

    }

    private void addPropertiesToViewer() throws SQLException
    {
        List<VBox> properties = model.getPropertyBoxes();
        propertyDisplayBox.getChildren().addAll(properties);
    }

    public Stage getStage()
    {
        return stage;
    }

}