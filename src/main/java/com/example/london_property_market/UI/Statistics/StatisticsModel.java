package com.example.london_property_market.UI.Statistics;

import com.example.london_property_market.Loader.CsvLoader;


import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

/**
 * Stats model, I am going to attempt to create all the SQL statements in
 * separate SQL files so that this file does not get "clogged up"
 *
 * @auther Ashley Tyagi K21008496, Tom Hurford - k21002432
 * @version 27/03/2022
 *
 */


public class StatisticsModel {

    private final CsvLoader sql;
    private final List<String> boroughs;
    private typesOfStat counter;


    /**
     * Constructor that takes in a list of boroughs to be setup.
     * Also uses this list to set the first stat, in the enum
     * @param boroughs The list of boroughs
     */
    public StatisticsModel(List<String> boroughs){
        sql = new CsvLoader();
        this.boroughs = boroughs;
        counter = typesOfStat.avg_reviews_per_property_view;
    }

    /**
     * Builds a WHERE statement to attach to the end of select statements so
     * that information from individual boroughs is created.
     * @return a where statement containing the boroughs 
     */
    private String createWhere(){
        StringBuilder where = new StringBuilder();
        if (boroughs.size() > 0){
            where.append(" WHERE");
            Iterator<String> i = boroughs.listIterator();
            while(i.hasNext()){
                where.append(" neighbourhood = '").append(i.next()).append("'");
                if(i.hasNext()){
                    where.append(" OR");
                }
            }
        }

        return where.toString();
    }

    /**
     * Gets the next stat using the enum to loop over all the different stats available.
     * @param isForward should we go to the next stat or the stat before
     * @return A resultset containing data from the SQL lines
     */
    public ResultSet getNextStat(boolean isForward){
        if (isForward){
           counter =  counter.loopForward();
        }else{
           counter = counter.loopBackward();
        }
        String methodToRun = counter.toString();
        ResultSet output = null;
        output = runQuery(methodToRun);
        return output;
    }

    /**
     *
     * @return Name of the stat to be displayed on the label.
     */
    public String getStatName(){
        return counter.getName();
    }

    /**
     * Runs a general query
     * @param view Name of the view we will be getting.
     * @return ResultSet containing that view.
     */
    private ResultSet runQuery(String view){
        String query = "SELECT * FROM "+ view +
                createWhere() + ";";

        return sql.executeQuery(query);
    }


}
