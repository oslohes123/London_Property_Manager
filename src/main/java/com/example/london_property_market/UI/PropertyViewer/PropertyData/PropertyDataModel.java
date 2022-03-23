package com.example.london_property_market.UI.PropertyViewer.PropertyData;

import com.example.london_property_market.Loader.CsvLoader;

import java.sql.ResultSet;

/**
 * The model class for the PropertyData page
 * Its only use is getting the correct data
 * from the Csv File
 *
 * @author Shaheer Effandi (K21013734)
 */
public class PropertyDataModel {

    private static final CsvLoader loader = new CsvLoader();

    /**
     * Uses a query to get the data of a particular property
     * from the Csv File
     *
     * @param id the id of the property
     * @return ResultSet contains a single tuple with the selected property's data
     */
    public static ResultSet getPropertyData(int id) {

        return loader.executeQuery(
                "SELECT * FROM Locations " +
                        "WHERE id = '" + id + "';"
        );
    }
}
