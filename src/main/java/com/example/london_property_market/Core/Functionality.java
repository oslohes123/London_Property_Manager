package com.example.london_property_market.Core;

import java.util.ArrayList;
import java.util.List;

import com.example.london_property_market.Loader.AirbnbListing;

public class Functionality {

    public List<AirbnbListing> PropertyData;
    public List<String> values;

    public Functionality(List<AirbnbListing> propertyData) {
        this.PropertyData = propertyData;
    }

    /**
     * A function to generate a price range that can be chosen by the user.
     * @return Returns a list of strings which represent the price range
     */
    public void comboboxValues() {
        List<String> values = new ArrayList<>();
        int maxPrice = comboBoxMaxValue();
        for (int i = maxPrice / 10; i <= maxPrice; i += maxPrice / 10) {
            values.add("" + i);
        }
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


}
