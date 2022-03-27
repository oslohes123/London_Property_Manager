package com.example.london_property_market.UI.Welcome;

import com.example.london_property_market.UI.MainViewer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class WelcomeController implements Initializable {

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

    /**
     * Creates the ComboBox values and sets the default values to 0 and MAX
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        core = new MainModel();
        minComboBox.getItems().addAll(core.getCombValues());
        maxComboBox.getItems().addAll(core.getCombValues());
        minComboBox.getStyleClass().add(COMBO_BOX_CSS_CLASS);
        maxComboBox.getStyleClass().add(COMBO_BOX_CSS_CLASS);
        minComboBox.setValue("0");
        maxComboBox.setValue("MAX");
    }

    /**
     * sets the Minimum Price in the Main Model to the comboBox value
     * @param actionEvent
     */
    @FXML
    public void getMinValue(ActionEvent actionEvent) {
        core.setMinAmount(minComboBox.getValue());
        setColors();
    }

    /**
     * Sets the Maximum Price in the Main Model to the combo box value
     * @param actionEvent
     */
    @FXML
    public void getMaxValue(ActionEvent actionEvent) {
        core.setMaxAmount(maxComboBox.getValue());
        setColors();
    }

    /**
     * Used to advance through the program to different pages
     * Checks if the combo box values are correct and if there is another
     * page to display before displaying the page
     *
     * @param actionEvent
     */
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

    /**
     * Used to go back to previous pages
     * Checks if the combo box values are correct and if there is another
     * page to display before displaying the page
     *
     * @param actionEvent
     */
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

    /**
     * This method updates the colors of the combo boxes
     */
    private void setColors(){
        if (core.isValidValues()) {
            MainViewer.setMainStyleSheet("Styles/combo/validCombo.css", minComboBox, maxComboBox);
            MainViewer.updatePanels();

        }else {
            MainViewer.setMainStyleSheet("Styles/combo/invalidCombo.css", minComboBox, maxComboBox);
        }
    }

    /**
     * This method alerts the user of an error on price selection
     */
    public static void priceError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pricing mismatch");
        alert.setHeaderText("The current Min: " + MainModel.getMinAmount()  + " The current max: " + MainModel.getMaxAmount());
        alert.setContentText("Select a min price that is less than the max price");
        alert.show();
    }

    /**
     * This method alert the user incorrect traversing of the program
     */
    private void paneError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Pane error");
        alert.setHeaderText("There is no pane on this side ");
        alert.setContentText("Select the pane from a different side");
        alert.show();
    }

}
