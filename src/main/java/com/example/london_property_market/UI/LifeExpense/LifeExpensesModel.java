package com.example.london_property_market.UI.LifeExpense;

import com.example.london_property_market.UI.LifeExpense.Services.CostTypes;
import com.example.london_property_market.UI.LifeExpense.Services.ServiceManager;


public class LifeExpensesModel {

    private ServiceManager serviceManager;
    private String[] servicesNames;
    private double addedCost;
    private int servicePointer;

    public LifeExpensesModel(){
        servicePointer = -1;
        addedCost = 0;
        serviceManager = new ServiceManager();
        servicesNames = serviceManager.getAllServicesNames();
    }

    protected void resetModelPointer(){
        servicePointer = -1;
        addedCost = 0;
    }

    protected String getNameForTheCurrentService(){
        return servicesNames[servicePointer];
    }

    protected CostTypes[] getOptionsForTheCurrentService(){
        return serviceManager.getAServiceByName(servicesNames[servicePointer]).getAvailableCosts();
    }

    protected void setSelectedOption(CostTypes type){
        addedCost += serviceManager.getAServiceByName(getNameForTheCurrentService()).getCost(type);
    }

    public double getAddedCost() {
        return addedCost;
    }

    protected boolean nextService(){
        if (servicesNames.length > 0 && servicePointer < servicesNames.length -1) {
            servicePointer++;
            return true;
        }

        return false;
    }


}
