package com.example.london_property_market;

import com.example.london_property_market.Loader.CsvLoader;
import com.example.london_property_market.UI.Statistics.StatisticsModel;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> boroughTest = new ArrayList<>();
        boroughTest.add("Croydon");
        StatisticsModel stats = new StatisticsModel(boroughTest);

        System.out.println(stats.averageReviewsPerProperty());
    }

}
