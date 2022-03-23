package com.example.london_property_market.UI.Statistics;

import com.example.london_property_market.UI.Welcome.MainModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.util.Callback;

import java.awt.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }

    public void setBoroughsList(List<String> boroughsList){
        statModel = new StatisticsModel(boroughsList);
        ResultSet rs = statModel.getNextStat(true);
        setupLabels(rs);
    }

    @FXML
    public void leftButtonClick(MouseEvent mouseEvent) {
        ResultSet rs = statModel.getNextStat(false);
        setupLabels(rs);
    }

    @FXML
    public void rightButtonClick(MouseEvent mouseEvent) {
        statTable.getColumns().clear();
        ResultSet rs = statModel.getNextStat(true);

        setupLabels(rs);
    }

    private void setupLabels(ResultSet rs){
        String label = statModel.getStatName();
        statsTitle.setText(label);
        try{
            statTable.getColumns().clear();
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;

                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                statTable.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
