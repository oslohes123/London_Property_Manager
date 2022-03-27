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


/**
 * Get contacted controller
 *
 * @auther Ashley Tyagi K21008496
 * @version 27/03/2022
 *
 */
public class getContactedController  {

    @FXML
    public Button sendButton;
    @FXML
    public TextField emailTextField;
    @FXML
    public TextField nameTextField;

    private Stage stage;
    private ResultSet propertyData;
    private String propertyName;

    /**
     * Sets up the new iwindow and sets up the initial stat to be shown
     * @param propertyID The property to get the stats of
     */
    public getContactedController(int propertyID) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/sendInfo.fxml"));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();
            propertyData = PropertyDataModel.getPropertyData(propertyID);
            
            Scene scene = new Scene(root);
            stage = new Stage();

            while(propertyData.next()){
                propertyName = propertyData.getString("name");
                stage.setTitle(propertyName);
            }

            stage.setScene(scene);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     *
     * @return Returns the stage
     */
    public Stage getStage()  {
        return stage;
    }

    /**
     * Runs when the sendButton is clicked in the getContacted panel
     * @param mouseEvent
     * @throws SQLException
     */
    @FXML
    public void sendClicked(MouseEvent mouseEvent) throws SQLException {

        try {
            InternetAddress emailAddr = new InternetAddress(emailTextField.getText());
            emailAddr.validate();
            if ("".equals(nameTextField.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Please enter a name");
                alert.show();
            }else{
                SendPropertyInquire.sendContact(emailTextField.getText(),nameTextField.getText(), propertyName);
                closingAction();
                stage.close();
            }


        } catch (AddressException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong email type");
            alert.setHeaderText("Input an email properly please");
            alert.setContentText("Re-enter your email");
            alert.show();
        }


    }


    /**
     * This method handles the successful sending of data by the user.
     */
    private void closingAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(propertyName);
        alert.setHeaderText("Thank you for contacting S.T.A.Y property viewer about the selected property. We will contact" +
                " you within our working days");
        alert.setContentText("We will contact you as soon as possible");
        alert.show();
    }
}
