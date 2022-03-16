package com.example.london_property_market.UI;

import com.example.london_property_market.Loader.AirbnbDataLoader;
import com.example.london_property_market.Loader.AirbnbListing;
import com.example.london_property_market.UI.Controller;
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

    Controller cont;

    public MainViewer() {
    }

    //  -valid: #55524b;
    //    -invalid:#ed0000;

    @Override
    public void start(Stage stage) throws IOException {

        File file = new File("src/main/resources/welcome2.fxml");
        URL url = file.toURI().toURL();
        Pane root = FXMLLoader.load(url);
        scene = new Scene(root);
        file = new File("src/main/resources/styles.css");
        scene.getStylesheets().add(file.toURI().toURL().toExternalForm());
        stage.setTitle("London Property Viewer");
        stage.setScene(scene);
        stage.show();
    }


}