package com.example.london_property_market.UI.PropertyViewer;

import com.example.london_property_market.Loader.CsvLoader;
import com.example.london_property_market.UI.PropertyViewer.PropertyData.PropertyDataController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.ResultSet;
import java.util.Set;

public class PropertyModel {

    private Set<String> boroughs;
    private int minPrice;
    private int maxPrice;
    public static final CsvLoader loader = new CsvLoader();


    public PropertyModel (Set<String> boroughs , int minPrice, int maxPrice)
    {
        this.boroughs = boroughs ;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    private ResultSet getProperties() {
        return loader.executeQuery(
                "SELECT * FROM Locations" +
                        " WHERE (" + getBoroughFilterQuery() +
                        ") AND price >= " + minPrice +
                        " AND price <= " + maxPrice + ";"
        );
    }

    private String getBoroughFilterQuery()
    {


        String whereClause = "";
        for (Iterator<String> it = boroughs.iterator(); it.hasNext(); ) {
            String borough = it.next();
            whereClause += "neighbourhood = '" + borough + "'";

            if (it.hasNext())
            {
                whereClause += " OR ";
            }
        }
        return whereClause;
    }
    public List<VBox> getPropertyBoxes(ResultSet validProperties) throws SQLException {
        List<VBox> properties = new ArrayList<>();

        while (validProperties.next())
        {

            VBox propertyData = new VBox();
            Label hostName, neighbourhood, price, numberOfReviews, minNumberOfNights;
            Button seeMoreButton = new Button();
            hostName = new Label("Host Name: " + validProperties.getString("host_name"));
            neighbourhood = new Label("Borough: " + validProperties.getString("neighbourhood"));
            price = new Label("Price Per Night: " + validProperties.getInt("price"));
            numberOfReviews = new Label("Number of Reviews: " + validProperties.getInt("number_of_reviews"));
            minNumberOfNights = new Label("Minimum stay (nights): " + validProperties.getInt("minimum_nights"));
            //http://tutorials.jenkov.com/jdbc/resultset.html

            seeMoreButton.setUserData(validProperties.getInt("id"));
            seeMoreButton.setOnAction(e -> showAllData((int) seeMoreButton.getUserData()));
            seeMoreButton.setText("More Details");

            propertyData.getChildren().addAll(hostName, neighbourhood, price, numberOfReviews, minNumberOfNights, seeMoreButton);
            properties.add(propertyData);
        }
        return properties;
    }

    public List<VBox> getPropertyBoxes() throws SQLException {

        return getPropertyBoxes(getProperties());
    }


    private void showAllData(int id) {
        try {

            PropertyDataController propertyData = new PropertyDataController(id);
            Stage stage = propertyData.getStage();
            stage.show();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public List<VBox> sortBy(String sortBy) throws SQLException {
        String orderByQuery = "ORDER BY ";
        if (sortBy.equals("Alphabetic: A-Z")) {
            orderByQuery += "host_name ASC";
        }
        else if (sortBy.equals("Alphabetic: Z-A")) {
            orderByQuery += "host_name DESC";
        }
        else if(sortBy.equals("Price: Low-High")) {
            orderByQuery += "price ASC";
        }
        else if(sortBy.equals("Price: High-Low")) {
            orderByQuery += "price DESC";
        }
        else if(sortBy.equals("Number of Reviews")) {
            orderByQuery += "number_of_reviews DESC";
        }
        else {
            return null;
        }

        return getPropertyBoxes(getProperties(orderByQuery));
    }

    private ResultSet getProperties(String sortBy) {
        return loader.executeQuery(
                "SELECT * FROM Locations" +
                        " WHERE (" + getBoroughFilterQuery() +
                        ") AND price >= " + minPrice +
                        " AND price <= " + maxPrice + "" +
                        " " + sortBy + ";"
        );
    }

    public String createStageTitle()
    {
        String title = "validProperties: ";
        for (String borough: boroughs) {
            title += borough + ", ";
        }

        return title.substring(0, title.length() - 2);
    }
}
