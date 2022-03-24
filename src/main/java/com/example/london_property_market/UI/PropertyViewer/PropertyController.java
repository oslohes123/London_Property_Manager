package com.example.london_property_market.UI.PropertyViewer;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.event.ActionEvent;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
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
public class PropertyController {
    @FXML
    public HBox topPropertyDisplayBox;
    @FXML private HBox botPropertyDisplayBox;
    @FXML private Button nextProperties;
    @FXML private Button previousProperties;
    @FXML ComboBox sortByCombo;

    public static final int PROPERTIES_PER_PAGE = 8;
    private PropertyModel model;

    //Pointers that help display properties
    private int beginningPointer;
    private int endPointer;

    private Scene scene;
    private Stage stage;
    private List<VBox> properties;

    /**
     * Constructor for DisplayView Class
     *
     * @param boroughs the selected boroughs
     * @param minPrice the minimum price per night for a property
     * @param maxPrice the maximum price per night for a property
     */
    public PropertyController(Set<String> boroughs, int minPrice, int maxPrice) throws IOException, SQLException {

        URL url = getClass().getClassLoader().getResource("views/PropertyDisplay.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        //https://ifelse.info/questions/46749/problem-loading-fxml-file
        fxmlLoader.setController(this);

        Parent root = fxmlLoader.load();

        scene = new Scene(root);
        model = new PropertyModel(boroughs, minPrice, maxPrice);
        stage = new Stage();

        setSortByCombo();
        addPropertiesToViewer(model.getPropertyBoxes());
        previousProperties.setOnAction(this::viewPrevious);
        nextProperties.setOnAction(this::viewNext);
        sortByCombo.setOnAction(this::sortProperties);


        stage.setScene(scene);
        stage.setTitle(model.createStageTitle());

    }

    private void addPropertiesToViewer(List<VBox> propertyBoxes) throws SQLException
    {
        properties = propertyBoxes;
        beginningPointer = 0;

        properties.forEach(a -> a.setPadding(new Insets(20)));

        for(VBox property: properties) {

            if(topPropertyDisplayBox.getChildren().size() != PROPERTIES_PER_PAGE/2) {
                topPropertyDisplayBox.getChildren().add(property);
            }

            else if (botPropertyDisplayBox.getChildren().size() != PROPERTIES_PER_PAGE/2) {
                botPropertyDisplayBox.getChildren().add(property);
            }

            else {
                endPointer = PROPERTIES_PER_PAGE;
                return;
            }
        }

        //if there are no more properties, the number of properties
        nextProperties.setDisable(true);
    }

    public void viewPrevious(ActionEvent event)
    {
        clearDisplayBoxes();
        //if we click the view previous button, that means there are methods ahead
        nextProperties.setDisable(false);
        endPointer = beginningPointer;

        //Since we may have a scenario where the number of properties is less than properties per page;
        if (beginningPointer - PROPERTIES_PER_PAGE <= 0)
        {
            beginningPointer = 0;
            //since there are no more properties, this button should not be clickable
            previousProperties.setDisable(true);
        }
        else {
            beginningPointer -= PROPERTIES_PER_PAGE;
        }

        addPropertiesToDisplay();
    }

    public void viewNext(ActionEvent event)
    {
        clearDisplayBoxes();

        previousProperties.setDisable(false);

        beginningPointer = endPointer;
        //setting pointer so that if it is at the end of the list of properties, it is set to the last value
        if (endPointer + PROPERTIES_PER_PAGE >= properties.size())
        {
            endPointer = properties.size();
            //since there are no more properties, this button should not be clickable
            nextProperties.setDisable(true);
        }
        else
        {
            endPointer += PROPERTIES_PER_PAGE;
        }

        addPropertiesToDisplay();
    }

    private void addPropertiesToDisplay() {
        for (int i = beginningPointer; i < endPointer; i++) {
            if(topPropertyDisplayBox.getChildren().size() != PROPERTIES_PER_PAGE/2) {
                topPropertyDisplayBox.getChildren().add(properties.get(i));
            }
            else if(botPropertyDisplayBox.getChildren().size() != PROPERTIES_PER_PAGE) {
                botPropertyDisplayBox.getChildren().add(properties.get(i));
            }
        }
    }

    private void setSortByCombo()
    {
        sortByCombo.getItems().addAll(
                "Alphabetic: A-Z",
                "Alphabetic: Z-A",
                "Price: High-Low",
                "Price: Low-High",
                "Number of Reviews");
    }



    private void sortProperties(Event event)  {

        try {
            System.out.println("This Has Been Called");
            List<VBox> sortedProperties = model.sortBy((String) sortByCombo.getValue());
            if (sortedProperties != null) {
                clearDisplayBoxes();
                addPropertiesToViewer(sortedProperties);
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    private void clearDisplayBoxes()
    {
        topPropertyDisplayBox.getChildren().clear();
        botPropertyDisplayBox.getChildren().clear();
    }

    public Stage getStage()
    {
        return stage;
    }


}