package com.example.london_property_market.Core;

import java.util.ArrayList;
import java.util.List;

import com.example.london_property_market.Loader.AirbnbListing;

public class Functionality {

    public List<AirbnbListing> PropertyData;
    public List<String> values;
    private Integer minAmount;
    private Integer maxAmount;

    public Functionality(List<AirbnbListing> propertyData) {
        this.PropertyData = propertyData;
        comboboxValues();
        minAmount = 0;
        maxAmount = comboBoxMaxValue();
    }

    /**
     * A function to generate a price range that can be chosen by the user.
     * @return Returns a list of strings which represent the price range
     */
    public void comboboxValues() {
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
        if (maxAmount == "MAX"){
            this.maxAmount = comboBoxMaxValue();
        }else{
            this.maxAmount = Integer.valueOf(maxAmount);
        }


    }

    public void setMinAmount(String minAmount){
        this.minAmount = Integer.valueOf(minAmount);
    }

    public boolean checkValidValues(){
        return maxAmount > minAmount;
    }

}
