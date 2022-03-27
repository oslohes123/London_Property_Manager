package com.example.london_property_market.UI.PropertyViewer.PropertyData;

import com.example.london_property_market.UI.PropertyViewer.PropertyData.getContacted.getContactedController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The controller/view class for the PropertyData page
 * Creates the Property Data page that shows all the data
 * for a single selected property.
 *
 * @author Shaheer Effandi (K21013734)
 */
public class PropertyDataController {

    //A variable that stores the stage so that it can be returned
    private Stage stage;
    //A VBox that contain a label with all the property data
    @FXML private VBox dataPage;
    //The tuple of with the data of the selected property
    private ResultSet propertyData;
    private int propertyId;

    /**
     * Loads the FXML file and creates the stage for the window.
     * Invokes the createDataPage(). Also assigns propertyData to
     * the corresponding tuple in the Csv File
     *
     * @param propertyID the ID of the property in the Csv File
     * @throws IOException
     * @throws SQLException
     */
    public PropertyDataController (int propertyID) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/PropertyDetails.fxml"));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        this.propertyId = propertyID;
        propertyData = PropertyDataModel.getPropertyData(propertyId);

        createDataPage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("Styles/views/propertyDataPageView.css");
        stage = new Stage();
        stage.setTitle(propertyData.getString("name"));
        stage.setScene(scene);

    }

    /**
     * Creates a label for the name of the property and the data about the property
     * the text of the label is set to the corresponding data from propertyData
     * @throws SQLException
     */
    private void createDataPage() throws SQLException {
        propertyData.next();

        Label name;
        Label data;
        name = new Label(propertyData.getString("name"));
        name.getStyleClass().add("propertyName");

        String lastReview = propertyData.getString("last_review");

        if (lastReview == null)
            lastReview = "No previous reviews";

        data = new Label();
        data.setText(
                "Host Name: " + propertyData.getString("host_name")
                + "\nHost ID: " + propertyData.getInt("host_id")
                + "\nRoom Type: " + propertyData.getString("room_type")
                + "\nPrice: " + propertyData.getInt("price")
                + "\nMinimum Stay: " + propertyData.getInt("minimum_nights")
                + "\nNumber of Review: " + propertyData.getInt("number_of_reviews")
                + "\nLast Review: " + lastReview
                + "\nAverage reviews per month: " + propertyData.getDouble("reviews_per_month")
                + "\nNumber of Property listings by this Host: " + propertyData.getInt("calculated_host_listings_count")
                + "\nNumber of available days: " + propertyData.getInt("availability_365")
                + "\nLatitude: " + propertyData.getDouble("latitude")
                + "\nLongitude: " + propertyData.getDouble("longitude")
        );

        Button getContactedButton = new Button();
        getContactedButton.getStyleClass().add("innerPaneButton");
        getContactedButton.setOnAction(e -> getContactedOpener(propertyId));
        getContactedButton.setText("Get Contacted");

        dataPage.getChildren().addAll(name, data, getContactedButton);
    }

    /**
     * Creates a new window where the user can contact the property Owner
     * @param id The id of the property
     */

    private void getContactedOpener(int id){
        try{
            getContactedController contactedController = new getContactedController(id);
            Stage stage = contactedController.getStage();
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    /**
     * An accessor method used so that the Property Viewer page can
     * open a new window that shows data for a particular property
     * @return stage
     */
    public Stage getStage()  {
        return stage;
    }
}
