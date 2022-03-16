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
    private static final String COMBO_BOX = "combo" ;
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
        minComboBox.getStyleClass().add(COMBO_BOX);
        maxComboBox.getStyleClass().add(COMBO_BOX);
        minComboBox.setValue("0");
        maxComboBox.setValue("MAX");
    }



    @FXML
    public void getMinValue(ActionEvent actionEvent) {
        core.setMinAmount(minComboBox.getValue());
        setColors();
    }

    @FXML
    public void getMaxValue(ActionEvent actionEvent) {
        core.setMaxAmount(maxComboBox.getValue());
        setColors();
    }

    private void setColors(){
        if (!core.checkValidValues()){
            minComboBox.setStyle("-mycolor:\"red\"");
            maxComboBox.setStyle("-mycolor:\"red\"");

        }else{
            minComboBox.setStyle("-mycolor:\"grey\"");
            maxComboBox.setStyle("-mycolor:\"grey\"");
        }
    }
}
