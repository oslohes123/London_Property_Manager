package com.example.london_property_market.Loader;

import java.sql.*;
import java.util.Arrays;

/**
 * Loads the CSV into the database so that we can execute queries against it.
 * http://www.h2database.com/html/tutorial.html#csv
 *
 * @author Tom Hurford - k21002432
 */
public final class CsvLoader {
    private final String DATABASE_URL = "jdbc:h2:file:" +
            "./src/main/resources/database/Locations";
    private final String USER = "sa";

    private static boolean created = false;

    public CsvLoader(){
        if(!created){
            dbSetup();
            created = true;
        }
    }

    /**
     * Sets up the database, should only be run once
     */
    private void dbSetup(){
        try{
            // If the database is created then it simply connects to the database
            // if the database is not created then it creates the database at the
            // filepath
            Connection con = DriverManager.getConnection(DATABASE_URL, USER, "");

            // Creates a statement object so that we can execute code on the
            // database
            Statement statement = con.createStatement();

            // First drop the table if it has already been created
            statement.executeUpdate("DROP TABLE IF EXISTS Locations CASCADE;");

            // Then execute a statement to create a Locations table using the
            // airbnb csv file
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Locations AS SELECT * " +
                    "FROM CSVREAD('./src/main/resources" +
                    "/database/airbnb.csv');");

            statement.execute("RUNSCRIPT FROM './src/main/resources/database" +
                    "/queries/avgReviewsPerBorough.sql'");

            // Always close database connections otherwise concurrency errors
            con.close();
        }catch(Exception e){
            System.out.println(e.getCause() + "; \n" + e.getMessage());
        }

    }

    /**
     * Executes an SQL query on the database
     * @param SQL the SQL statement to execute
     * @return A Result set object if statement has run, null otherwise
     */
    public ResultSet executeQuery(String SQL) {
        try {
            //Connect to the database
            Connection con = DriverManager.getConnection(DATABASE_URL, USER, "");
            ResultSet rs = con.createStatement().executeQuery(SQL);
            return rs;
        } catch (Exception e) {
            System.out.println(e.getCause() + "; \n" + e.getMessage());
        }
        return null;
    }

    /**
     * Executes an SQL script on the database
     * @param scriptPath the file path of the script
     * @return a result set from the script
     */
    public ResultSet executeScript(String scriptPath){
        try{
            Connection con = DriverManager.getConnection(DATABASE_URL, USER,
                    "");
            Statement statement = con.createStatement();
            String query = "RUNSCRIPT FROM \'"+ scriptPath + "\'";
            statement.executeQuery(query);
            return statement.getResultSet();
        }catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}
