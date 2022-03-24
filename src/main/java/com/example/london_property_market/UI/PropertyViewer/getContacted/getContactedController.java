package com.example.london_property_market.UI.PropertyViewer.getContacted;

import com.example.london_property_market.UI.PropertyViewer.PropertyData.PropertyDataModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class getContactedController implements Initializable {
    @FXML
    public Button sendButton;
    @FXML
    public TextField emailTextField;
    @FXML
    public TextField nameTextField;

    private Stage stage;
    private ResultSet propertyData;

    public getContactedController(int propertyID) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/sendInfo.fxml"));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();
            propertyData = PropertyDataModel.getPropertyData(propertyID);
            
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setTitle("Get Connected");
            stage.setScene(scene);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Stage getStage()
    {
        return stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void sendClicked(MouseEvent mouseEvent) {
        
    }
}
