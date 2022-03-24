package com.example.london_property_market.UI.PropertyViewer.PropertyData;

import com.example.london_property_market.Loader.CsvLoader;

import java.sql.ResultSet;

public class PropertyDataModel {
    private static final CsvLoader loader = new CsvLoader();

    public static ResultSet getPropertyData(int id) {

        return loader.executeQuery(
                "SELECT * FROM airbnb_locations " +
                        "WHERE id = '" + id + "';"
        );
    }
}
