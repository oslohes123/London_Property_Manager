package com.example.london_property_market.UI.Statistics;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
/**
 * @auther Ashley Tyagi K21008496
 *
 * v1
 */



/**
 * This controls the statistics window
 */
public class StatisticsController implements Initializable {

    @FXML
    public Button leftStatsButton = new Button();
    @FXML
    public Button rightStatsButton = new Button();
    @FXML
    public Label statsTitle;
    @FXML
    public TableView statTable;

    private StatisticsModel statModel;


    /**
     * Used to initialise stuff to default values at the moment is empty
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * An initialisation function to get data from the mapController
     * @param boroughsList
     */
    public void setBoroughsList(List<String> boroughsList){
        statModel = new StatisticsModel(boroughsList);
        ResultSet rs = statModel.getNextStat(true);
        setupLabels(rs);
    }

    /**
     * Moves to the previous stat available in the enum loops around at 0
     * @param mouseEvent Event that nouse has been clicked
     */
    @FXML
    public void leftButtonClick(MouseEvent mouseEvent) {
        ResultSet rs = statModel.getNextStat(false);
        setupLabels(rs);
    }

    /**
     * Moves to the next stat available in the enum loops around at last.
     * @param mouseEvent Event that mouse has been clicked
     */
    @FXML
    public void rightButtonClick(MouseEvent mouseEvent) {
        ResultSet rs = statModel.getNextStat(true);
        setupLabels(rs);
    }



    /**
     *
     * Answer takes some inspiration from
     * https://stackoverflow.com/questions/18941093/how-to-fill-up-a-tableview-with-database-data
     *
     * @param rs The result set used to setup the tableview
     */
    private void setupLabels(ResultSet rs){
        String label = statModel.getStatName();
        statsTitle.setText(label);
        ObservableList data = FXCollections.observableArrayList();
        ObservableList<String> row;
        try{
            statTable.getColumns().clear();
            ResultSetMetaData rsmd = rs.getMetaData();

            // This part is for adding the column names
            for(int i=0 ; i< rsmd.getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;

                TableColumn col = new TableColumn(rsmd.getColumnName(i+1));
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                statTable.getColumns().addAll(col);
//                System.out.println("Column ["+i+"] ");

            }

            // This part is for adding the data to each of the columns
            while(rs.next()) {
                //Iterate Row
                row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
//                System.out.println("Row [1] added " + row);
                data.add(row);
            }

            statTable.setItems(data);
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }





    }

}
