package com.example.london_property_market.UI.LifeExpense;

import com.example.london_property_market.UI.LifeExpense.Services.CostTypes;
import com.example.london_property_market.UI.LifeExpense.Services.ServiceManager;


public class LifeExpensesModel {

    private ServiceManager serviceManager;
    private String[] servicesNames;
    private double addedCost;
    private int servicePointer;

    public LifeExpensesModel(){
        servicePointer = 0;
        addedCost = 0;
        serviceManager = new ServiceManager();
        servicesNames = serviceManager.getAllServicesNames();
    }

    protected String getNameForTheCurrentService(){
        return servicesNames[servicePointer];
    }

    protected CostTypes[] getOptionsForTheCurrentService(){
        return serviceManager.getAServiceByName(servicesNames[servicePointer]).getAvailableCosts();
    }

    protected void selectedOption(String serviceName, CostTypes type){
        addedCost += serviceManager.getAServiceByName(serviceName).getCost(type);
    }

    protected boolean nextService(){
        if (servicePointer < servicesNames.length) {
            servicePointer++;
            return true;
        }

        return false;
    }


}
