package com.example.london_property_market.UI.LifeExpense;

import com.example.london_property_market.UI.FXMLIRRepresentable;
import com.example.london_property_market.UI.LifeExpense.Services.CostTypes;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * This class represent the UI part of the life expense calculator. It contains all the aspects of the calculator's UI.
 *
 * @author Yousef Altaher
 * @version 26-03-2022
 */
public class LifeExpensesController implements FXMLIRRepresentable {

    // The model for the calculator
    private LifeExpensesModel model;
    // The main panel for the UI
    private VBox lifeExpensesMainPane;
    // The list of the references of the buttons for the current service. Needed to disable selection.
    private ArrayList<Button> currentOptions;
    // The text field for entering the budget
    private TextField budgetTextField;

    /**
     * This method initialize the calculator UI part with its all necessary information.
     * @return The pane that contains the UI elements.
     */
    @Override
    public Pane initialize() {
        lifeExpensesMainPane = new VBox();
        lifeExpensesMainPane.getStyleClass().addAll("mainVBox", "innerPane");

        model = new LifeExpensesModel();
        currentOptions = new ArrayList<>();

        lifeExpensesMainPane.getStylesheets().add("Styles/views/recommendationView.css");

        startDialogue();

        return lifeExpensesMainPane;
    }

    /**
     * This method starts the dialogue of the calcultor.
     */
    private void startDialogue(){
        VBox startVBox = new VBox();
        startVBox.getStyleClass().add("options");

        Label startText = new Label("Welcome Sir/Madam. This page contains a simple calculator that aids people to" +
                " decide the range of rent that they should search for. It is important to calculate certain factors as" +
                " rent is not the only thing a customer is expected to pay. This simple calculator will provide you" +
                " with a rough approximation of what is your search range. To start this service, please click on the" +
                " Start button");
        startText.setWrapText(true);
        Button startButton = new Button("Start");
        startButton.setOnAction(this::onBudgetStartAction);

        startVBox.getChildren().addAll(startText, startButton);

        lifeExpensesMainPane.getChildren().add(startVBox);
    }

    /**
     * This method starts the budget part of the calculator in the dialogue
     * @param actionEvent actionEvent
     */
    private void onBudgetStartAction(ActionEvent actionEvent){

        lifeExpensesMainPane.getChildren().clear();
        ((Button) actionEvent.getSource()).setDisable(true);

        VBox budgetVBox = new VBox();
        budgetVBox.getStyleClass().add("options");

        Label expectedBudget = new Label("It is important that we know the price you are ready to spend on renting," +
                "can you please provide your absolute maximum budget.");
        budgetTextField = new TextField();
        Button next = new Button("Next");
        next.setOnAction(this::onServiceStartAction);

        budgetVBox.getChildren().addAll(expectedBudget, budgetTextField, next);

        lifeExpensesMainPane.getChildren().add(budgetVBox);

    }

    /**
     * This method performs the start of the dialogue part that contains the services and their usage
     * @param actionEvent actionEvent
     */
    private void onServiceStartAction(ActionEvent actionEvent){
        lifeExpensesMainPane.getChildren().clear();
        if (isValidBudget()) {
            ((Button) actionEvent.getSource()).setDisable(true);

            model.setMaximumBudget(Double.parseDouble(budgetTextField.getText()));
            budgetTextField.setDisable(true);
            if (model.nextService())
                lifeExpensesMainPane.getChildren().add(getNextDialogue());
        }else{
            invalidErrorHandler();
        }
    }

    /**
     * This method invokes the appropriate response when the user enters an invalid budget
     */
    private void invalidErrorHandler(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid budget");
        alert.setTitle("The budget must be a positive number");
        alert.setContentText("The budget must be a positive number.");
        alert.show();
    }

    /**
     * This method validate if the entered budget is a valid numerical number
     *
     * Reference: https://www.baeldung.com/java-check-string-number
     * Author: baeldung, date: October 10, 2021
     * @return true if the budget is a positive number, false otherwise
     */
    private boolean isValidBudget(){
        return NumberUtils.isCreatable(budgetTextField.getText()) && Double.parseDouble(budgetTextField.getText()) > 0;
    }

    /**
     * This method perform the UI actions of adding the next dialogue from the calculator.
     * @return the layout that contains the new UI elements for the calculator
     */
    private VBox getNextDialogue(){

        VBox serviceTab = new VBox();
        serviceTab.getStyleClass().add("serviceTab");

        Label serviceText = new Label("Can you describe your usage of the following service: "
                + model.getNameForTheCurrentService());

        HBox options = new HBox();
        options.getStyleClass().add("options");

        CostTypes[] serviceOptions = model.getOptionsForTheCurrentService();

        for (CostTypes costType: serviceOptions){
            Button costTypeButton = new Button(costType.name());
            costTypeButton.getProperties().put("cost", costType);
            costTypeButton.setOnAction(this::selectedOptionOnAction);

            currentOptions.add(costTypeButton);
        }

        currentOptions.sort(Comparator.comparing(Labeled::getText));
        options.getChildren().addAll(currentOptions);
        serviceTab.getChildren().addAll(serviceText, options);

        return serviceTab;
    }

    /**
     * This method model the changes that occurred from the user's selection of an option for a service
     * @param actionEvent actionEvent
     */
    private void selectedOptionOnAction(ActionEvent actionEvent){
        model.setSelectedOption((CostTypes) ((Button) actionEvent.getSource()).getProperties().get("cost"));
        currentOptions.forEach(i -> i.setDisable(true));
        currentOptions.clear();

        if (model.nextService())
            lifeExpensesMainPane.getChildren().add(getNextDialogue());
        else
            finalAction();
    }

    /**
     * This method represent the final action that is performed by the calculator. I.e, when everything is finished.
     */
    private void finalAction(){

        VBox finalVBox = new VBox();
        finalVBox.getStyleClass().add("options");

        Label totalCost = new Label("It appears that you maybe expected to pay an average of "
                + ((int) model.getAddedCost()*100)/100.0
        + " for the above services");

        Label finalCostRecommendation = new Label("We recommend that you spend at most "
                + model.getRecommendedBudget() + " in rent");

        Button resetButton = new Button("Start Again");
        resetButton.setOnAction(this::resetButton);

        finalVBox.getChildren().addAll(totalCost, finalCostRecommendation, resetButton);
        lifeExpensesMainPane.getChildren().add(finalVBox);
    }

    /**
     * This method perform the reset functionality of the calculator
     * @param actionEvent actionEvent
     */
    private void resetButton(ActionEvent actionEvent){
        lifeExpensesMainPane.getChildren().clear();
        model.resetModelPointer();
        startDialogue();
    }

    /**
     * This method is invoked when a change occurs on the main controller. This method is empty as there is no need
     * in the meanwhile to implement it as there currently no effects on it.
     */
    @Override
    public void onChangeInformation() {

    }
}
