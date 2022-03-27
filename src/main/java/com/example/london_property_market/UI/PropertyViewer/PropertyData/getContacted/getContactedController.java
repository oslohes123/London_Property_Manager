package com.example.london_property_market.UI.PropertyViewer.PropertyData.getContacted;

import com.example.london_property_market.UI.PropertyViewer.PropertyData.PropertyDataModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.example.london_property_market.ContactAPI.SendPropertyInquire;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            while(propertyData.next()){
                stage.setTitle(propertyData.getString("name"));
            }

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
    public void sendClicked(MouseEvent mouseEvent) throws SQLException {
        String property = "";

        try {
            InternetAddress emailAddr = new InternetAddress(emailTextField.getText());
            emailAddr.validate();

            while(propertyData.next()){
                property = propertyData.getString("name");
            }

            SendPropertyInquire.sendContact(emailTextField.getText(),nameTextField.getText(),property);
        } catch (AddressException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong email type");
            alert.setHeaderText("Input an email properly please");
            alert.setContentText("Re-enter your email");
            alert.show();
        }


    }
}
