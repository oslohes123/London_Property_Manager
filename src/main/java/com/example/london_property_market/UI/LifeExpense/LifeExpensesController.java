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

public class LifeExpensesController implements FXMLIRRepresentable {

    private LifeExpensesModel model;
    private VBox lifeExpensesMainPane;
    private ArrayList<Button> currentOptions;
    private TextField budgetTextField;
    private double maximumBudget;

    public static void main(String[] args) {
        LifeExpensesController lec = new LifeExpensesController();
        lec.initialize();

    }
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
        startButton.setOnAction(this::onStartAction);

        startVBox.getChildren().addAll(startText, startButton);

        lifeExpensesMainPane.getChildren().add(startVBox);
    }

    private void onStartAction(ActionEvent actionEvent){

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

    private void onServiceStartAction(ActionEvent actionEvent){

        if (isValidBudget()) {
            ((Button) actionEvent.getSource()).setDisable(true);

            maximumBudget = Double.parseDouble(budgetTextField.getText());
            budgetTextField.setDisable(true);
            if (model.nextService())
                lifeExpensesMainPane.getChildren().add(getNextDialogue());
        }else{
            invalidErrorHandler();
        }
    }

    private void invalidErrorHandler(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid budget");
        alert.setTitle("The budget must be a positive number");
        alert.setContentText("The budget must be a positive number.");
        alert.show();
    }

    //https://www.baeldung.com/java-check-string-number
    private boolean isValidBudget(){
        return NumberUtils.isCreatable(budgetTextField.getText()) && Double.parseDouble(budgetTextField.getText()) > 0;
    }

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

    private void selectedOptionOnAction(ActionEvent actionEvent){
        model.setSelectedOption((CostTypes) ((Button) actionEvent.getSource()).getProperties().get("cost"));
        currentOptions.forEach(i -> i.setDisable(true));
        currentOptions.clear();

        if (model.nextService())
            lifeExpensesMainPane.getChildren().add(getNextDialogue());
        else
            finalAction();
    }

    private void finalAction(){

        VBox finalVBox = new VBox();
        finalVBox.getStyleClass().add("options");

        Label totalCost = new Label("It appears that you maybe expected to pay an average of "
                + ((int) model.getAddedCost()*100)/100.0
        + " for the above services");

        Label finalCostRecommendation = new Label("We recommend that you spend at most "
                + (((int) ((maximumBudget-model.getAddedCost())*100))/100.0) + " in rent");


        Button resetButton = new Button("Start Again");
        resetButton.setOnAction(this::resetButton);

        finalVBox.getChildren().addAll(totalCost, finalCostRecommendation, resetButton);
        lifeExpensesMainPane.getChildren().add(finalVBox);
    }

    private void resetButton(ActionEvent actionEvent){
        lifeExpensesMainPane.getChildren().clear();
        model.resetModelPointer();
        startDialogue();
    }

    @Override
    public void onChangeInformation() {

    }
}
