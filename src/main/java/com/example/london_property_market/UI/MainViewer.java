package com.example.london_property_market.UI;

import com.example.london_property_market.Loader.AirbnbDataLoader;
import com.example.london_property_market.Loader.AirbnbListing;
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
    @FXML private ComboBox minComboBox;
    @FXML private ComboBox maxComboBox;

//    @FXML
//    public void goMap(ActionEvent event)
//    {
//        scene = mapPage.getScene();
//        stage.setScene(scene);
//    }
    public MainViewer ()
    {
        propertyData = dataLoader.load();

    }

    @Override
    public void start(Stage stage) throws IOException {

        File file = new File("resources/welcome2.fxml");
        URL url = file.toURI().toURL();
        Pane root = FXMLLoader.load(url);
        scene = new Scene(root);
        setComboBox();

        stage.setTitle("London Property Viewer");
        stage.setScene(scene);
        stage.show();
    }

    private int comboBoxMaxValue()
    {
        return propertyData.stream()
                .map(AirbnbListing::getPrice)
                .max((price1, price2) -> price1 - price2)
                .get();
    }

    private void setComboBox()
    {
        List<String> values = new ArrayList<>();
        int maxPrice = comboBoxMaxValue();
        for(int i = maxPrice / 10; i <= maxPrice; i += maxPrice/10)
        {
            values.add("" + i);
        };

        minComboBox.getItems().addAll(values);
        maxComboBox.getItems().addAll(values);
    }
}