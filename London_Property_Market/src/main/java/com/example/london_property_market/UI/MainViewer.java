package com.example.london_property_market.UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

import java.io.IOException;

public class MainViewer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        File file = new File("resources/welcome2.fxml");
        URL url = file.toURI().toURL();
        Pane root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}