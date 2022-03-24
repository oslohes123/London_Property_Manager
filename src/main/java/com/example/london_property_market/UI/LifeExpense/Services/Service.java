package com.example.london_property_market.UI.LifeExpense.Services;

import java.util.HashMap;

public class Service {

    private HashMap<CostTypes, Double> costs;

    public Service(HashMap<CostTypes, Double> costs) {
        this.costs = costs;
    }

    public void setCosts(HashMap<CostTypes, Double> costs){
        this.costs = costs;
    }

    public CostTypes[] getAvailableCosts(){
        return costs.keySet().toArray(CostTypes[]::new);
    }

    public double getCost(CostTypes costTypes){
        return costs.get(costTypes);
    }
}
