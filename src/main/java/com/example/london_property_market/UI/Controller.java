package com.example.london_property_market.UI;

import com.example.london_property_market.Core.Functionality;
import com.example.london_property_market.Loader.AirbnbDataLoader;
import com.example.london_property_market.Loader.AirbnbListing;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable{

    private static AirbnbDataLoader dataLoader = new AirbnbDataLoader();
    private List<AirbnbListing> propertyData;
    @FXML
    private ComboBox<String> minComboBox;
    @FXML
    private ComboBox<String> maxComboBox;
    @FXML
    private Button goMapButton1;
    @FXML
    private Button goMapButton;

    private Functionality core;

    public Controller(){
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        propertyData = dataLoader.listDataLoader();
        core = new Functionality(propertyData);
        minComboBox.getItems().addAll(core.values);
        maxComboBox.getItems().addAll(core.values);
    }



    @FXML
    public void getMinValue(ActionEvent actionEvent) {
        core.setMinAmount(minComboBox.getValue());

    }


    public void getMaxValue(ActionEvent actionEvent) {
        core.setMaxAmount(maxComboBox.getValue());
    }
}
