package com.example.london_property_market.UI;

import javafx.scene.layout.Pane;

/**
 *
 * This interface provides essential methods that perform common operations for applicable classes. This interface were
 * created to make the integration of Java-based views and FXML-based view more coherent and compatible. The main reason
 * for including both designs is the technical issue that was present when transforming ArcGIS control component into
 * FXML, in which the transformation was not completed due to sceneViewer issues with including and rendering ArcGIS
 * library. In which it was a necessity to incorporate FXML and Java based views. This interface also provide more
 * extensibility, as if future issues were present that needed to make an FXML into a Java-based view, it would be more
 * reasonable for this interface to serve as a template for the classes and easing the invocation from Main viewer.
 *
 * @author Yousef Althaer
 * @version 18-03-2022
 */
public interface FXMLIRRepresentable {
    /**
     * This method initialize a view with its necessary objects. The use of a different method other than the constructor
     * serve as a generalization, as it will ease the creating of views from mainView.
     * @return the pane for the view
     */
    Pane initialize();

    /**
     * This method provide the class the capability of reacting to change in the mainModel to, possibly, update the view.
     */
    void onChangeInformation();
}
