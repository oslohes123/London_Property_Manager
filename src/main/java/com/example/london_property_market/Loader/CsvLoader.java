package com.example.london_property_market.Loader;
import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.Arrays;

/**
 * http://www.h2database.com/html/tutorial.html#csv
 *
 * @author Tom Hurford - k21002432
 */
public final class CsvLoader {
    private final String DATABASE_URL = "jdbc:h2:file:" +
            "./src/main/resources/database/Locations";

    private final String USER = "sa";

    private static boolean created = false;
    private Connection con;

    public CsvLoader(){
        if(!created){
            created = true;
        }
    }

    /**
     * Runs the teardown script on the database
     */
    public void dbTeardown(){
        try{
             con = DriverManager.getConnection(DATABASE_URL,
                    USER, "");
            Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.execute("RUNSCRIPT FROM './src/main/resources/database" +
                    "/queries/databaseTeardown.sql'");
        }catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        System.out.println("Database removed");
    }



    /**
     * Executes an SQL query on the database
     * @param SQL the SQL statement to execute
     * @return A Result set object if statement has run, null otherwise
     */
    public ResultSet executeQuery(String SQL) {
        try {
            //Connect to the database
            con = DriverManager.getConnection("jdbc:postgresql://18.133.250.240:5432/london_property_viewer","assignmentViewer","admin");
            return con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE).executeQuery(SQL);
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
            con = DriverManager.getConnection("jdbc:postgresql://18.133.250.240:5432/london_property_viewer","assignmentViewer","admin");
            Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String query = "RUNSCRIPT FROM '" + scriptPath + "'";
            statement.executeQuery(query);
            return statement.getResultSet();
        }catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}
