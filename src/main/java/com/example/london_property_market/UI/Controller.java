package com.example.london_property_market.UI;

import com.example.london_property_market.Core.MainModel;
import com.example.london_property_market.Loader.AirbnbDataLoader;
import com.example.london_property_market.Loader.AirbnbListing;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private static AirbnbDataLoader dataLoader = new AirbnbDataLoader();
    private List<AirbnbListing> propertyData;
    private static final String COMBO_BOX = "combo";
    @FXML
    private ComboBox<String> minComboBox;
    @FXML
    private ComboBox<String> maxComboBox;
    @FXML
    private Button goMapButton1;
    @FXML
    private Button goMapButton;

    private MainModel core;

    public Controller() {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        propertyData = dataLoader.listDataLoader();
        core = new MainModel(propertyData);
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

    private void setColors() {
        if (!core.checkValidValues()) {
            minComboBox.setStyle("-fx-background-color:#ff1100");
            maxComboBox.setStyle("-fx-background-color: #ff1100");

        } else {
            minComboBox.setStyle("-fx-background-color:#dbdbdb");
            maxComboBox.setStyle("-fx-background-color: #dbdbdb");
        }
    }

    @FXML
    public void backwardsClick(ActionEvent actionEvent) {
        // Currently, not implemented
    }

    @FXML
    public void forwardsClick(ActionEvent actionEvent) {
        if (!core.checkValidValues()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("The minimum price is currently greater than the maximum price");
            alert.setHeaderText("Min: " + core.getMinAmount() + " Max: " + core.getMaxAmount());
            alert.setContentText("Invalid please select a minimum price that is less than the maximum price");
            alert.show();
        } else {



        }
    }
}
