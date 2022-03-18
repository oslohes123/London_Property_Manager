package com.example.london_property_market.UI.Welcome;

import com.example.london_property_market.UI.MainViewer;
import com.example.london_property_market.UI.Welcome.MainModel;
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


public class WelcomeController implements Initializable{

    private AirbnbDataLoader dataLoader = new AirbnbDataLoader();
    private List<AirbnbListing> propertyData;
    private final String COMBO_BOX_CSS_CLASS = "combo";

    @FXML
    private ComboBox<String> minComboBox;
    @FXML
    private ComboBox<String> maxComboBox;
    @FXML
    private Button nextBtn;
    @FXML
    private Button previousBtn;

    private MainModel core;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        propertyData = dataLoader.listDataLoader();
        core = new MainModel(propertyData);
        minComboBox.getItems().addAll(core.getCombValues());
        maxComboBox.getItems().addAll(core.getCombValues());
        minComboBox.getStyleClass().add(COMBO_BOX_CSS_CLASS);
        maxComboBox.getStyleClass().add(COMBO_BOX_CSS_CLASS);
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
    @FXML
    public void nextBtnOnAction(ActionEvent actionEvent){
        if (!core.isValidValues()){
            priceError();
        }
        else if (!MainViewer.isNextPointerChangeValid(1)){
            paneError();
        }else{
            MainViewer.setCenterLayout(1);
        }
    }
    @FXML
    public void previousBtnOnAction(ActionEvent actionEvent){
        if (!core.isValidValues()){
            priceError();
        }
        else if (!MainViewer.isNextPointerChangeValid(-1)){
            paneError();
        }else{
            MainViewer.setCenterLayout(-1);
        }
    }

    private void setColors(){
        if (core.isValidValues()) {
            MainViewer.setMainStyleSheet("combo/validCombo.css");
            MainViewer.updatePanels();
        }else {
            MainViewer.setMainStyleSheet("combo/invalidCombo.css");
        }

    }

    private void priceError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pricing mismatch");
        alert.setHeaderText("The current Min: " + core.getMinAmount()  + " The current max: " + core.getMaxAmount());
        alert.setContentText("Select a min price that is less than the max price");
        alert.show();
    }

    private void paneError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pane error");
        alert.setHeaderText("There is no pane on this side ");
        alert.setContentText("Select the pane from a different side");
        alert.show();
    }

}
