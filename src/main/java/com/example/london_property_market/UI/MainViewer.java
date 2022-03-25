package com.example.london_property_market.UI;

import com.example.london_property_market.UI.LifeExpense.LifeExpensesController;
import com.example.london_property_market.UI.Map.MapController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class represents the main UI class of the application. This class acts as the main viewer (manager) for the
 * panels that are on the main panel.
 */
public class MainViewer extends Application {

    // The hashmap that contains the objects (screens references) and their tag (to access)
    private final static HashMap<Integer, Object> screenRegister = new HashMap<>();
    // The pointer that points to the screen on the hashmap
    private static int VIEW_POINTER = 0;
    // The main scene
    private Scene scene;
    // The main pane for the center pain on the scene
    private static BorderPane mainPane;

    /**
     * The main application start point
     * @param stage stage
     * @throws IOException IOException
     */
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader root = new FXMLLoader(getClass().getClassLoader().getResource("views/MainView.fxml"));
        mainPane = root.load();

        createScreenRegisterEntries();

        mainPane.setCenter(new FXMLLoader(getClass().getClassLoader().getResource("views/WelcomeView.fxml")).load());

        mainPane.getStylesheets().add(MainViewer.class.getClassLoader().getResource("Styles/combo/validCombo.css").toExternalForm());

        scene = new Scene(mainPane, 1040, 740);

        stage.setTitle("London Property Viewer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method populate tha hashmap with the panel reference, either as a string to represent an fxml file name, or
     * a class declaration to store the reference
     */
    private void createScreenRegisterEntries(){
        screenRegister.put(0, "WelcomeView.fxml");
        screenRegister.put(1, new MapController());
        screenRegister.put(2, new LifeExpensesController());
    }

    /**
     * This method sets the central layout of the mainViewer to the required layout.
     * @param direction 1 for right, -1 for left
     */
    public static void setCenterLayout(int direction){

        if (screenRegister.get(VIEW_POINTER+direction) instanceof FXMLIRRepresentable) {
            mainPane.setCenter(((FXMLIRRepresentable) screenRegister.get(VIEW_POINTER + direction)).initialize());
            updatePanels();
        }else
            try {
                mainPane.setCenter((new FXMLLoader(MainViewer.class.getClassLoader().getResource("views/"+screenRegister.get(VIEW_POINTER+direction))).load()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        VIEW_POINTER += direction;

    }

    /**
     * This method sets the stylesheet for the mainView. The reason for the task separation is that different scenarios
     * requires the change of the mainView layout. The style sheet has to be on the designated style folder.
     * @param styleSheetName the name of the style sheet.
     */
    public static void setMainStyleSheet(String styleSheetName){
        mainPane.getStylesheets().clear();
        mainPane.getStylesheets().add(MainViewer.class.getClassLoader().getResource("Styles/"+styleSheetName).toExternalForm());
    }

    /**
     * This method returns true if it is possible to navigate to a panel
     * @param direction 1 for right, -1 for left
     * @return true if it is possible to navigate, false otherwise
     */
    public static boolean isNextPointerChangeValid(int direction){
        return VIEW_POINTER + direction < screenRegister.size() && VIEW_POINTER + direction >= 0;
    }

    /**
     * This method updates the FXMLIRRepresentable panels when a change on them is needed.
     */
    public static void updatePanels(){
        for (Object panel : screenRegister.values())
            if (panel instanceof FXMLIRRepresentable)
                ((FXMLIRRepresentable) panel).onChangeInformation();

    }
}