package com.example.london_property_market.UI.PropertyViewer.getContacted;

import com.example.london_property_market.UI.PropertyViewer.PropertyData.PropertyDataModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class getContactedController {
    private Stage stage;

//    public getContactedController{
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/PropertyDetails.fxml"));
//        fxmlLoader.setController(this);
//        Parent root = fxmlLoader.load();
//        propertyData = PropertyDataModel.getPropertyData(propertyID);
//
//        createDataPage();
//        Scene scene = new Scene(root);
//        scene.getStylesheets().add("Styles/views/propertyDataPageView.css");
//        stage = new Stage();
//        stage.setTitle(propertyData.getString("name"));
//        stage.setScene(scene);
//    }

    public Stage getStage()
    {
        return stage;
    }
}
