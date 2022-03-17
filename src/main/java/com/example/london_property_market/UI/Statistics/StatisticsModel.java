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


    /**
     * Returns the top result from the query, -1 otherwise
     * @return double of the average number of reviews
     */
    public double averageReviewsPerProperty(){
        String query =
                "SELECT * FROM avgReviewsPerBorough WHERE" + createWhere();
        ResultSet rs = sql.executeQuery(query);
        try{
            return rs.getDouble("Average_Reviews");
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
        return -1;
    }

}
