package com.example.london_property_market.UI.PropertyViewer;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.event.ActionEvent;


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
public class PropertyController {
    @FXML private GridPane propertyDisplayGrid;
    @FXML private Button nextProperties;
    @FXML private Button previousProperties;
    @FXML private ComboBox<String> sortByCombo;

    //The number of properties that will be shown on a page at a time
    public static final int PROPERTIES_PER_PAGE = 8;
    private PropertyModel model;

    //Pointers that help display properties
    private int beginningPointer;
    private int endPointer;

    private Stage stage;
    private List<VBox> properties;

    /**
     * Constructor for DisplayView Class, Loads the FXML file and initialises
     * the model and the stage.
     * Sets the Actions for each of the @FXML variables and adds options to the
     * comboBox
     *
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

        Scene scene = new Scene(root);
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

    /**
     * Iterates through a list of VBoxes a constant number of them to the page
     * Set pointers so that the next and previous buttons are able to show the properties
     * @param propertyBoxes a list of properties
     * @throws SQLException
     */
    private void addPropertiesToViewer(List<VBox> propertyBoxes) throws SQLException
    {
        properties = propertyBoxes;
        beginningPointer = 0;
        int counter = 0;

        properties.forEach(a -> a.setPadding(new Insets(20)));

        for(VBox property: properties) {

            if(counter < PROPERTIES_PER_PAGE/2) {
                propertyDisplayGrid.add(property, counter %(PROPERTIES_PER_PAGE / 2), 0);
            }

            else if (counter < PROPERTIES_PER_PAGE) {
                propertyDisplayGrid.add(property, counter %(PROPERTIES_PER_PAGE / 2), 1);
            }

            else {
                endPointer = PROPERTIES_PER_PAGE;
                return;
            }
            counter ++;
        }

        //if there are no more properties, the number of properties
        nextProperties.setDisable(true);
    }

    /**
     * If this button is enabled, it will show the previous properties in the list.
     * If we press the previousProperties button, it means that after clicking, the
     * nextProperties button must be enabled
     * @param event
     */
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

    /**
     * If this button is enabled, it will show the next properties in the list.
     * If we press the nextProperties button, it means that after clicking, the
     * previousProperties button must be enabled
    */
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

    /**
     * A method that adds properties to a gridPane to display.
     * Used to reduce duplicate code
     */
    private void addPropertiesToDisplay() {
        int counter = 0;
        for (int i = beginningPointer; i < endPointer; i++) {
            if(counter < PROPERTIES_PER_PAGE/2) {
                propertyDisplayGrid.add(properties.get(i), counter %(PROPERTIES_PER_PAGE / 2), 0);
            }

            else if (counter < PROPERTIES_PER_PAGE) {
                propertyDisplayGrid.add(properties.get(i), counter %(PROPERTIES_PER_PAGE / 2), 1);
            }
            counter ++;
        }

    }

    /**
     * Adds all sortBy options to the ComboBox
     */
    private void setSortByCombo()
    {
        sortByCombo.getItems().addAll(
                "Alphabetic: A-Z",
                "Alphabetic: Z-A",
                "Price: High-Low",
                "Price: Low-High",
                "Number of Reviews");
    }


    /**
     * When an option in the ComboBox is selected, the Pane is emptied
     * and the model gets a list of VBoxes with properties sorted by the selected criteria
     * this list is then passed into the addPropertiesToViewer method
     * @param event
     */
    private void sortProperties(Event event)  {

        try {
            List<VBox> sortedProperties = model.sortBy(sortByCombo.getValue());
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

    /**
     * Clears the gridPane so that different properties can be added
     */
    private void clearDisplayBoxes()
    {
        propertyDisplayGrid.getChildren().clear();
    }

    /**
     * An accessor method used so that the Map page can
     * open a new window that shows data for all the properties
     * that fit the criteria
     * @return stage
    */
    public Stage getStage()
    {
        return stage;
    }


}