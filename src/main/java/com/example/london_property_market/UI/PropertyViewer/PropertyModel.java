package com.example.london_property_market.UI.PropertyViewer;

import com.example.london_property_market.Loader.CsvLoader;
import com.example.london_property_market.UI.PropertyViewer.PropertyData.PropertyDataController;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;
import java.sql.ResultSet;

/**
 * The model class for the PropertyContoller class
 * It is responsible for getting data from the CsvLoader class
 * and placing it into VBoxes that will be returned to the controller
 * class.
 *
 * @author Shaheer Effandi (K21013734)
 */

public class PropertyModel {

    //The boroughs that are selected by the user
    private Set<String> boroughs;
    //The minimum/maximum price per night selected by the user
    private int minPrice;
    private int maxPrice;

    /*
        A hashMap that contains all the possible sort by filters
        and their corresponding SQL queries
    */
    public static HashMap<String, String> sortCriteria;
    //The SQL loader itself
    public static final CsvLoader loader = new CsvLoader();

    /**
     * Takes the users filter selections and stores them so that they can be
     * used later for filtering the Csv File.
     * Also initialises the sortCriteria HashMap and invokes setSortCriteria()
     * @param boroughs the boroughs that were selected by the user
     * @param minPrice the minimum price per night for a property
     * @param maxPrice the maximum price per night for a property
     */
    public PropertyModel (Set<String> boroughs , int minPrice, int maxPrice)
    {
        this.boroughs = boroughs ;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        sortCriteria = new HashMap<>();
        setSortCriteria();
    }

    /**
     * Creates the SQL query based on the arguments passed
     * when creating the class
     * @return ResultSet every tuple that fits the criteria
     */
    private ResultSet getProperties() {
        return loader.executeQuery(
                "SELECT * FROM Locations" +
                        " WHERE (" + getBoroughFilterQuery() +
                        ") AND price >= " + minPrice +
                        " AND price <= " + maxPrice + ";"
        );
    }

    /**
     * Creates part of the "WHERE" clause in the SQL statement
     * The name of each selected borough is added to the query
     * and returned to the getProperties() query
     * @return
     */
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

    /**
     * Takes a set of tuples and formats the data into VBoxes using Labels.
     * These VBoxes will be displayed in the PropertyController class
     * When one of the VBoxes are clicked, a new window opens with the data for that
     * property
     * @param validProperties
     * @return properties A list of VBoxes with the data from the SQL query
     * @throws SQLException
     */
    public List<VBox> getPropertyBoxes(ResultSet validProperties) throws SQLException {
        List<VBox> properties = new ArrayList<>();

        while (validProperties.next())
        {

            VBox propertyData = new VBox();
            propertyData.setUserData(validProperties.getInt("id"));
            propertyData.setOnMouseClicked(e -> showAllData((int) propertyData.getUserData()));

            //http://tutorials.jenkov.com/jdbc/resultset.html
            Label hostName, neighbourhood, price, numberOfReviews, minNumberOfNights;
            hostName = new Label("Host Name: " + validProperties.getString("host_name"));
            neighbourhood = new Label("Borough: " + validProperties.getString("neighbourhood"));
            price = new Label("Price Per Night: " + validProperties.getInt("price"));
            numberOfReviews = new Label("Number of Reviews: " + validProperties.getInt("number_of_reviews"));
            minNumberOfNights = new Label("Minimum stay (nights): " + validProperties.getInt("minimum_nights"));

            propertyData.getChildren().addAll(hostName, neighbourhood, price, numberOfReviews, minNumberOfNights);
            properties.add(propertyData);
        }
        return properties;
    }

    /**
     * An overloaded version of the method that is used when the
     * page is loaded up initially.
     * The resulting set has no sorting filters applied
     * @return properties A list of VBoxes with the data from the SQL query
     * @throws SQLException
     */
    public List<VBox> getPropertyBoxes() throws SQLException {

        return getPropertyBoxes(getProperties());
    }

    /**
     * Will open a new window which contains all the details about
     * the selected property
     * @param id used in an SQL query to get data about the property
     */
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


    /**
     * creates an orderBy clause that is passed into an overloaded
     * version of getProperties. This is then passed into the
     * getPropertyBoxes() method.
     * Uses the sortCriterias HashMap to create the ORDER BY clause
     *
     * @param sortBy the selected sortBy option
     * @return properties A list of VBoxes with the data from the SQL query
     * @throws SQLException
     */
    public List<VBox> sortBy(String sortBy) throws SQLException {
        String orderByQuery = "ORDER BY ";
        String columnName = sortCriteria.get(sortBy);
        if(columnName == (null)) {
            return null;
        }
        orderByQuery += columnName;
        return getPropertyBoxes(getProperties(orderByQuery));
    }

    /**
     * An overloaded version of the getProperties method that
     * also includes an ORDER BY clause
     * @param sortBy
     * @return ResultSet the set of tuples that match the criteria
     */
    private ResultSet getProperties(String sortBy) {
        return loader.executeQuery(
                "SELECT * FROM Locations" +
                        " WHERE (" + getBoroughFilterQuery() +
                        ") AND price >= " + minPrice +
                        " AND price <= " + maxPrice + "" +
                        " " + sortBy + ";"
        );
    }

    /**
     * Using the set "boroughs", a title for page is created by
     * concatenating all borough names
     * @return title the title of property viewer page
     */
    public String createStageTitle()
    {
        String title = "validProperties: ";
        for (String borough: boroughs) {
            title += borough + ", ";
        }

        return title.substring(0, title.length() - 2);
    }

    /**
     * Adds all the sortBy options to the HashMap and maps them
     * to their corresponding SQL ORDER BY statement.
     */
    private void setSortCriteria()
    {
        sortCriteria.put("Alphabetic: A-Z", "host_name ASC");
        sortCriteria.put("Alphabetic: Z-A", "host_name DESC");
        sortCriteria.put("Price: Low-High", "price ASC");
        sortCriteria.put("Price: High-Low", "price DESC");
        sortCriteria.put("Number of Reviews", "number_of_reviews DESC");
    }
}
