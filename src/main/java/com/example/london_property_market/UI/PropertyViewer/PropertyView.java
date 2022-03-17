package com.example.london_property_market.UI.PropertyViewer;

import com.example.london_property_market.Loader.CsvLoader;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * The class that will show all the property listings
 * that are in one of the selected boroughs, and is within
 * the selected price range.
 * These values will be passed in by the map page.
 * @author Shaheer Effandi (K21013734)
 */
public class PropertyView extends Stage
{
    @FXML private HBox propertyDisplayBox;
    private List<String> borough;
    private int minPrice;
    private int maxPrice;
    private CsvLoader loader;

    /**
     * Constructor for DisplayView Class
     * @param boroughs
     * @param minPrice
     * @param maxPrice
     */
    public PropertyView (List<String> boroughs, int minPrice, int maxPrice)
    {

        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        addPropertiesToViewer(boroughs);
    }

    private void addPropertiesToViewer (List<String> boroughs)
    {

    }

}