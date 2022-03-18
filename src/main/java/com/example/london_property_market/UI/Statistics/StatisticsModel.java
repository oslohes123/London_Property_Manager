package com.example.london_property_market.UI.Statistics;

import com.example.london_property_market.Loader.CsvLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Stats model, I am going to attempt to create all the SQL statements in
 * separate SQL files so that this file does not get "clogged up"
 */
public class StatisticsModel {

    private final CsvLoader sql;
    private final List<String> boroughs;

    public StatisticsModel(List<String> boroughs){
        sql = new CsvLoader();
        this.boroughs = boroughs;
    }

    private String createWhere(){
        StringBuilder where = new StringBuilder();
        Iterator<String> i = boroughs.listIterator();
        while(i.hasNext()){
            where.append(" neighbourhood = '").append(i.next()).append("'");
            if(i.hasNext()){
                where.append(" AND");
            }
        }
        return where.toString();
    }

    public ResultSet avgReviewsPerProperty(){
        String query = "SELECT * FROM avg_reviews_per_property_view WHERE" +
                createWhere() + ";";
        return sql.executeQuery(query);
    }

    public ResultSet number_of_properties_available(){
        String query = "SELECT * FROM number_of_properties_available_view " +
                "WHERE" + createWhere() + ";";
        return sql.executeQuery(query);
    }

    public ResultSet numberOfRoomTypes(){

    }

}
