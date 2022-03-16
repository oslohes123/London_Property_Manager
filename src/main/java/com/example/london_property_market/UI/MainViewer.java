package com.example.london_property_market.UI;

import com.example.london_property_market.UI.Map.MapController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewer extends Application {

    private final static String[] VIEWS_NAMES_FXML = {"WelcomeView.fxml", "map.fxml"};
    private static int VIEW_POINTER = 0;

    private Scene scene;
    private static BorderPane mainPane;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader root = new FXMLLoader(getClass().getClassLoader().getResource("views/MainView.fxml"));
        mainPane = root.load();

        mainPane.setCenter(new FXMLLoader(getClass().getClassLoader().getResource("views/WelcomeView.fxml")).load());
        mainPane.getStylesheets().add(MainViewer.class.getClassLoader().getResource("Styles/combo/validCombo.css").toExternalForm());

        scene = new Scene(mainPane);

        stage.setTitle("London Property Viewer");
        stage.setScene(scene);
        stage.show();
    }

    public static void setCenterLayout(int direction){

        if (VIEW_POINTER+direction != 1) {
            try {
                mainPane.setCenter((new FXMLLoader(MainViewer.class.getClassLoader().getResource("views/"+VIEWS_NAMES_FXML[VIEW_POINTER+direction])).load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            mainPane.setCenter(new MapController().initialize());

        VIEW_POINTER += direction;

    }

    public static void setMainStyleSheet(String styleSheetName){
        mainPane.getStylesheets().clear();
        mainPane.getStylesheets().add(MainViewer.class.getClassLoader().getResource("Styles/"+styleSheetName).toExternalForm());
    }

    public static boolean isNextPointerChangeValid(int direction){
        return VIEW_POINTER + direction < VIEWS_NAMES_FXML.length && VIEW_POINTER + direction >= 0;
    }
}