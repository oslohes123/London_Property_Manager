package com.example.london_property_market.UI.PropertyViewer;

import com.example.london_property_market.Loader.CsvLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.sql.ResultSet;
import java.util.Set;

public class PropertyModel {

    private Set<String> boroughs;
    private int minPrice;
    private int maxPrice;
    public static final CsvLoader loader = new CsvLoader();


    public PropertyModel (Set<String> boroughs, int minPrice, int maxPrice)
    {
        this.boroughs = boroughs;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    private List<ResultSet> getProperties() {
        List<ResultSet> queryResults = new ArrayList<>();
        for(String borough: boroughs) {
            ResultSet properties = loader.executeQuery(
                    "SELECT * FROM Locations" +
                            " WHERE neighbourhood = '" + borough + "'" +
                            " AND price >= " + minPrice +
                            " AND price <= " + maxPrice + ";"
            );
            queryResults.add(properties);
        }

        return queryResults;
    }

    public List<VBox> getPropertyBoxes() throws SQLException {
        List<VBox> properties = new ArrayList<>();
        List<ResultSet> validProperties = getProperties();
        for (ResultSet borough: validProperties)
        {
            VBox propertyData = new VBox();
            Label hostName, neighbourhood, price, numberOfReviews, minNumberOfNights;
            Button seeMoreButton = new Button();
            hostName = new Label();
            neighbourhood = new Label();
            price = new Label();
            numberOfReviews = new Label();
            minNumberOfNights = new Label();

            while (borough.next())
            {
                //http://tutorials.jenkov.com/jdbc/resultset.html
                hostName.setText(borough.getString("host_name"));
                neighbourhood.setText(borough.getString("neighbourhood"));
                price.setText("" + borough.getInt("price"));
                numberOfReviews.setText("" + borough.getInt("number_of_reviews"));
                minNumberOfNights.setText("" + borough.getInt("minimum_nights"));

                seeMoreButton.setUserData(
                        loader.executeQuery(
                           "SELECT * FROM Locations" +
                            " WHERE id = " + borough.getInt("id")
                        )
                );
                seeMoreButton.setOnAction(e -> showAllData((ResultSet) seeMoreButton.getUserData()));
                seeMoreButton.setText("More Details");

                propertyData.getChildren().addAll(hostName, neighbourhood, price, numberOfReviews);
            }

            properties.add(propertyData);
        }
        return properties;
    }

    private void showAllData(ResultSet results) {
        try {

            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/PropertyDetails.fxml"));
            Scene scene = new Scene(root);


            Stage stage = new Stage();
            stage.setTitle(results.getString("name"));
            stage.setScene(scene);
            stage.show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private VBox createDataPage(ResultSet results) throws SQLException {
        VBox dataPage = new VBox();
        Label name, hostName, latitude, longitude, room_type, minNights, numberOfReviews, lastReview, reviewsPerMonth, hostListingCount, availability365;
        Label data;
        name = new Label(results.getString("name"));
        data = new Label();

        data.setText(
                "Host Name: " + results.getString("host_name")
                + "\nHost ID: " + results.getInt("host_id")
                + "\nRoom Type: " + results.getString("room_type")
                + "\nPrice: " + results.getInt("price")
                + ""
        );
    dataPage.getChildren().addAll(name, data);
    return null;
    }

    public String createStageTitle()
    {
        String title = "Boroughs: ";
        for (String borough: boroughs) {
            title += borough + ", ";
        }

        return title.substring(0, title.length() - 2);
    }
}
