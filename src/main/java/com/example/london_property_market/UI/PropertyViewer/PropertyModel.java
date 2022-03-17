package com.example.london_property_market.UI.PropertyViewer;

import com.example.london_property_market.Loader.CsvLoader;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class PropertyModel {

    private List<String> boroughs;
    private int minPrice;
    private int maxPrice;


    public PropertyModel (List<String> boroughs, int minPrice, int maxPrice)
    {
        this.boroughs = boroughs;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public List<ResultSet> getProperties()
    {
        List<ResultSet> queryResults = new ArrayList<>();
        CsvLoader loader = new CsvLoader();

        for(String borough: boroughs) {
            ResultSet properties = loader.executeQuery(
                    "SELECT * FROM Locations" +
                            "WHERE neighbourhood = " + borough +
                            "AND price >= " + minPrice +
                            "AND price <= " + maxPrice + ";"
            );
            queryResults.add(properties);
        }
        System.out.println("Arrlen: " + queryResults.size());
        return queryResults;
    }

    public static void main(String[] args) {
        ArrayList<String> borough = new ArrayList<>();
        borough.add("Kingston upon Thames");
        borough.add("Croydon");
        borough.add("Ealing");

        PropertyModel p = new PropertyModel(borough, 0, Integer.MAX_VALUE);
        p.getProperties();

    }
}
