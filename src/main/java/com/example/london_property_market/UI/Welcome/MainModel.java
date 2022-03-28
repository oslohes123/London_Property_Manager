package com.example.london_property_market.UI.Welcome;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.london_property_market.Loader.DataBaseLoader;

/**
 * The model class for Main Viewer. Mainly deals with calculating
 * the combo box values as well as storing them so that they can
 * be used later
 *
 * @author Ashley Tyagi K21008496, Shaheer Effandi K21013734
 * @version 27/03/2022
 */
public class MainModel {

    //List of values that will be set in the combo Box
    private List<String> values;
    //Min and max amounts that are chosen in the combo drop down
    private static int minAmount;
    private static int maxAmount;

    /**
     * Initialises the min and max Amount fields
     * Adds the combo box values to the values list
     */
    public MainModel() {

        comboBoxValues();
        minAmount = 0;
        maxAmount = comboBoxMaxValue();
    }

    /**
     * @return the values that will be set in the ComboBox
     */
    public List<String> getCombValues() {
        return values;
    }

    /**
     * A function to generate a price range that can be chosen by the user.
     */
    public void comboBoxValues() {
        List<String> values = new ArrayList<>();
        int maxPrice = comboBoxMaxValue();
        for (int i = maxPrice / 10; i <= maxPrice-1; i += maxPrice / 10) {
            values.add("" + i);
        }
        values.add(0,"0");
        values.add("MAX");
        this.values = values;
    }

    /**
     * Gets the highest price apartment from the CSV file
     * @return int with highest price
     */
    private int comboBoxMaxValue() {
        int maxPrice = -1;

        try {
            DataBaseLoader loader = new DataBaseLoader();
            ResultSet rs = loader.executeQuery(
                    "SELECT MAX(price) AS maxPrice FROM airbnb_locations; "
            );
            rs.next();
            maxPrice = rs.getInt("maxPrice");

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return maxPrice;
    }

    /**
     * Sets the maxAmount field to the chosen value
     * @param maxAmount the chosen combo box value
     */
    public void setMaxAmount(String maxAmount) {
     this.maxAmount = getAmounts(maxAmount);
    }

    /**
     * Sets the minAmount field to the chosen value
     * @param minAmount the chosen combo box value
     */
    public void setMinAmount(String minAmount){
        this.minAmount = getAmounts(minAmount);
    }

    /**
     * Checks if the user has chosen "MAX" as the combo value
     * If so it wil return the max price, else it will return
     * the value chosen
     *
     * @param input the chosen combo box value
     * @return the corresponding price as an int
     */
    private int getAmounts(String input){
        if (input.equals("MAX"))
            return comboBoxMaxValue();
        else
            return Integer.parseInt(input);

    }

    /**
     * This method checks if the valeus for the combo boxes are valid
     * @return true the values are valid, false otherwise
     */
    public boolean isValidValues(){
        return maxAmount >= minAmount;
    }

    /**
     * This method returns the minimum amount
     * @return the minimum amount
     */
    public static int getMinAmount(){
        return minAmount;
    }

    /**
     * This method returns the maximum amount
     * @return the maximum amount
     */
    public static int getMaxAmount(){
        return maxAmount;
    }
}
