package com.example.london_property_market.UI.LifeExpense;

import com.example.london_property_market.UI.LifeExpense.Services.CostTypes;
import com.example.london_property_market.UI.LifeExpense.Services.ServiceManager;

/**
 *
 * This class represent the model of the life expanse calculator. This class includes everything that is logically related
 * to the calculator.
 *
 * @author Yousef Altaher
 * @version 26-03-2022
 */
public class LifeExpensesModel {

    // The service manager that has all the services
    private ServiceManager serviceManager;
    // The names of all services
    private String[] servicesNames;
    // The added cost from the services
    private double addedCost;
    // The user's maximum budget
    private double maximumBudget;
    // The pointer that indicate which service the user is currently choosing
    private int servicePointer;


    /**
     * This method initialize the model object
     */
    public LifeExpensesModel(){
        servicePointer = -1;
        addedCost = 0;
        maximumBudget = 0;
        serviceManager = new ServiceManager();
        servicesNames = serviceManager.getAllServicesNames();
    }

    /**
     * This method reset the pointer in order to start the calculation again.
     */
    protected void resetModelPointer(){
        servicePointer = -1;
        addedCost = 0;
        maximumBudget = 0;
    }

    /**
     * This method returns the name of the current service that the pointer points to
     * @return the name of the current service that the pointer points to
     */
    protected String getNameForTheCurrentService(){
        return servicesNames[servicePointer];
    }

    /**
     * This method returns the options for the current service
     * @return the options for the current service
     */
    protected CostTypes[] getOptionsForTheCurrentService(){
        return serviceManager.getAServiceByName(servicesNames[servicePointer]).getAvailableCosts();
    }

    /**
     * This method set the user's maximum budget
     * @param maximumBudget user's maximum budget
     */
    protected void setMaximumBudget(double maximumBudget){
        this.maximumBudget = maximumBudget;
    }

    /**
     * This method returns the recommenced budget
     * @return the recommenced budget
     */
    protected double getRecommendedBudget(){
        return ((int)((maximumBudget-addedCost)*100))/100.0;
    }

    /**
     * This method manipulate the accumulated added costs based on the selected cost type for the current service
     * @param type the costType (spending class)
     */
    protected void setSelectedOption(CostTypes type){
        addedCost += serviceManager.getAServiceByName(getNameForTheCurrentService()).getCost(type);
    }

    /**
     * This method returns the added cost from the services
     * @return the added cost from the services
     */
    public double getAddedCost() {
        return addedCost;
    }

    /**
     * This method increment the pointer if a next service exists
     * @return true if the increment was possible, false otherwise.
     */
    protected boolean nextService(){
        if (servicesNames.length > 0 && servicePointer < servicesNames.length -1) {
            servicePointer++;
            return true;
        }

        return false;
    }


}
