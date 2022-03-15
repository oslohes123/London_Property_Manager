package com.example.london_property_market.UI;

import com.example.london_property_market.Loader.AirbnbDataLoader;
import com.example.london_property_market.Loader.AirbnbListing;
import com.example.london_property_market.Core.Functionality;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainViewer extends Application {

    private Scene scene;
    private static AirbnbDataLoader dataLoader = new AirbnbDataLoader();
    private List<AirbnbListing> propertyData;
    //private MapViewer mapPage;
    @FXML private ComboBox<String> minComboBox = new ComboBox<>();
    @FXML private ComboBox<String> maxComboBox = new ComboBox<>();
    private Functionality core;

//    @FXML
//    public void goMap(ActionEvent event)
//    {
//        scene = mapPage.getScene();
//        stage.setScene(scene);
//    }
    public MainViewer ()
    {
        propertyData = dataLoader.listDataLoader();

    }

    @Override
    public void start(Stage stage) throws IOException {

        File file = new File("src/main/resources/welcome2.fxml");
        URL url = file.toURI().toURL();
        Pane root = FXMLLoader.load(url);
        scene = new Scene(root);
        core = new Functionality(propertyData);
        core.comboboxValues();
//        minComboBox.getItems().addAll(core.values);
//        maxComboBox.getItems().addAll(core.values);
        stage.setTitle("London Property Viewer");
        stage.setScene(scene);
        stage.show();
    }


}