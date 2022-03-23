package com.example.london_property_market.UI.Statistics;

import com.example.london_property_market.Loader.CsvLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

/**
 * Stats model, I am going to attempt to create all the SQL statements in
 * separate SQL files so that this file does not get "clogged up"
 */
public class StatisticsModel {

    private final CsvLoader sql;
    private final List<String> boroughs;
    private typesOfStat counter;


    public StatisticsModel(List<String> boroughs){
        sql = new CsvLoader();
        this.boroughs = boroughs;
        counter = typesOfStat.avgReviewsPerProperty;
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

    public ResultSet getNextStat(boolean isForward){
        if (isForward){
           counter =  counter.loopForward();
        }else{
           counter = counter.loopBackward();
        }
        String methodToRun = counter.toString();

        ResultSet output = null;
        try{
           Method method = this.getClass().getDeclaredMethod(methodToRun);
            output  = (ResultSet) method.invoke(this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return output;
    }



    public String getStatName(){
        return counter.getName();
    }

    private ResultSet avgReviewsPerProperty(){
        String query = "SELECT * FROM avg_reviews_per_property_view WHERE" +
                createWhere() + ";";
        return sql.executeQuery(query);
    }

    private ResultSet number_of_properties_available(){
        String query = "SELECT * FROM number_of_properties_available_view " +
                "WHERE" + createWhere() + ";";
        return sql.executeQuery(query);
    }

    private ResultSet numberOfRoomTypes(){
        String query =
                "SELECT * FROM number_of_room_types_view WHERE" + createWhere() + ";";
        return sql.executeQuery(query);
    }

    private ResultSet mostExpensiveProperty(){
        String query =
                "SELECT * FROM most_expensive_property_view WHERE" + createWhere() + ";";
        return sql.executeQuery(query);
    }

    private ResultSet averagePricePerNight(){
        String query =
                "SELECT * FROM avg_price_per_night WHERE" + createWhere() + ";";
        return sql.executeQuery(query);
    }

    private ResultSet averagePricePerMinStay(){
        String query =
                "SELECT * FROM avg_price_per_min_stay WHERE" + createWhere() + ";";
        return sql.executeQuery(query);
    }

}
