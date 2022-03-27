package com.example.london_property_market.UI.Welcome;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.london_property_market.Loader.CsvLoader;

public class MainModel {

    private List<String> values;
    private static int minAmount;
    private static int maxAmount;

    public MainModel() {

        comboBoxValues();
        minAmount = 0;
        maxAmount = comboBoxMaxValue();
    }

    public List<String> getCombValues() {
        return values;
    }

    /**
     * A function to generate a price range that can be chosen by the user.
     * @return Returns a list of strings which represent the price range
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
            CsvLoader loader = new CsvLoader();
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

    public void setMaxAmount(String maxAmount) {
     this.maxAmount = getAmounts(maxAmount);
    }

    public void setMinAmount(String minAmount){
        this.minAmount = getAmounts(minAmount);
    }

    private int getAmounts(String input){
        if (input.equals("MAX"))
            return comboBoxMaxValue();
        else
            return Integer.parseInt(input);

    }

    public boolean isValidValues(){
        return maxAmount > minAmount;
    }

    public static int getMinAmount(){
        return minAmount;
    }
    public static int getMaxAmount(){
        return maxAmount;
    }
}
