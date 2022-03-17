package com.example.london_property_market;

import com.example.london_property_market.Loader.CsvLoader;

import java.sql.ResultSet;

public class Main {

    public static void main(String[] args) {
        CsvLoader sql = new CsvLoader();
        ResultSet rs = sql.executeQuery("SELECT * FROM Locations");



    }

}
