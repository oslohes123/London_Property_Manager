package com.example.london_property_market.Loader;

import javax.swing.plaf.nimbus.State;
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

    public void dbTeardown(){
        try{
            Connection connection = DriverManager.getConnection(DATABASE_URL,
                    USER, "");
            Statement statement = connection.createStatement();
            statement.execute("RUNSCRIPT FROM './src/main/resources/database" +
                    "/queries/databaseTeardown.sql'");
        }catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        System.out.println("Database removed");
    }

    public void dbSetup(){
        try{
            Connection connection = DriverManager.getConnection(DATABASE_URL,
                    USER, "");
            Statement statement = connection.createStatement();
            statement.execute("RUNSCRIPT FROM './src/main/resources/database" +
                    "/queries/databaseSetup.sql'");
        }catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        System.out.println("Database setup");
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
            return con.createStatement().executeQuery(SQL);
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
            String query = "RUNSCRIPT FROM '" + scriptPath + "'";
            statement.executeQuery(query);
            return statement.getResultSet();
        }catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}
