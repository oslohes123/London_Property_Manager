package com.example.london_property_market.UI.Welcome;

import com.example.london_property_market.UI.MainViewer;
import com.example.london_property_market.UI.Welcome.MainModel;
import com.example.london_property_market.Loader.AirbnbDataLoader;
import com.example.london_property_market.Loader.AirbnbListing;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class WelcomeController implements Initializable{

    private AirbnbDataLoader dataLoader = new AirbnbDataLoader();
    private List<AirbnbListing> propertyData;
    private final String COMBO_BOX_CSS_CLASS = "combo" ;

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

    public void nextBtnOnAction(ActionEvent actionEvent){
        if (!MainViewer.isNextPointerChangeValid(1)){
            //
        }else{
            MainViewer.setCenterLayout(1);
        }
    }

    public void previousBtnOnAction(ActionEvent actionEvent){
        if (!MainViewer.isNextPointerChangeValid(-1)){
            //
        }else{
            MainViewer.setCenterLayout(-1);
        }
    }

    private void setColors(){
        if (core.isValidValues())
            MainViewer.setMainStyleSheet("combo/validCombo.css");
        else
            MainViewer.setMainStyleSheet("combo/invalidCombo.css");

    }
}