package com.example.london_property_market.UI.LifeExpense;

import com.example.london_property_market.UI.FXMLIRRepresentable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class LifeExpensesController implements FXMLIRRepresentable {

    @Override
    public Pane initialize() {
        VBox lifeExpensesMainPane = new VBox();

        return lifeExpensesMainPane;
    }

    @Override
    public void onChangeInformation() {

    }
}
