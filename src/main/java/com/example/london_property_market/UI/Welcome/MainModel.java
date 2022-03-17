package com.example.london_property_market.UI.Welcome;

import java.util.ArrayList;
import java.util.List;

import com.example.london_property_market.Loader.AirbnbListing;

public class MainModel {

    private List<AirbnbListing> PropertyData;
    private List<String> values;
    private int minAmount;
    private int maxAmount;

    public MainModel(List<AirbnbListing> propertyData) {
        this.PropertyData = propertyData;
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

        return PropertyData.stream()
                .map(AirbnbListing::getPrice)
                .max(Integer::compare).get();

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
        return maxAmount >= minAmount;
    }

    public int getMinAmount(){
        return minAmount;
    }
    public int getMaxAmount(){
        return maxAmount;
    }
}
