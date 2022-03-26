package com.example.london_property_market.UI.LifeExpense.Services;

import java.util.HashMap;

/**
 * This class represent a complete service.
 *
 * @author Yousef Altaher, K20047484
 * @version 26-03-2022
 */
public class Service {

    // The services' usage types and their corresponding cost.
    private HashMap<CostTypes, Double> costs;

    /**
     * This method construct a service with the provided cost map.
     * @param costs the costs map.
     */
    public Service(HashMap<CostTypes, Double> costs) {
        this.costs = costs;
    }

    /**
     * This method set the costs of the service.
     * @param costs the new costs map.
     */
    public void setCosts(HashMap<CostTypes, Double> costs){
        this.costs = costs;
    }

    /**
     * This method returns the set of cost types (spending classes) for the current service.
     * @return the set of cost types (spending classes) for the current service.
     */
    public CostTypes[] getAvailableCosts(){
        return costs.keySet().toArray(CostTypes[]::new);
    }

    /**
     * This method get the cost for the class of spending of the current service.
     * @param costTypes the services costType (usage class)
     * @return the cost for the class of spending of the current service.
     */
    public double getCost(CostTypes costTypes){
        return costs.get(costTypes);
    }
}
