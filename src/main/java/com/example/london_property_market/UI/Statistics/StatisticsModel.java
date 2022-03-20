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

    /**
     * Builds a WHERE statement to attach to the end of select statements so
     * that information from individual boroughs is created.
     * @return a where statement containing the boroughs 
     */
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
        String query =
                "SELECT * FROM number_of_room_types_view WHERE" + createWhere() + ";";
        return sql.executeQuery(query);
    }

    public ResultSet mostExpensiveProperty(){
        String query =
                "SELECT * FROM most_expensive_property_view WHERE" + createWhere() + ";";
        return sql.executeQuery(query);
    }

    public ResultSet averagePricePerNight(){
        String query =
                "SELECT * FROM avg_price_per_night WHERE" + createWhere() + ";";
        return sql.executeQuery(query);
    }

    public ResultSet averagePricePerMinStay(){
        String query =
                "SELECT * FROM avg_price_per_min_stay WHERE" + createWhere() + ";";
        return sql.executeQuery(query);
    }

}
