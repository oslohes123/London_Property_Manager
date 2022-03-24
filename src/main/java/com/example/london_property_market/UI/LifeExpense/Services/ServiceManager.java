package com.example.london_property_market.UI.LifeExpense.Services;

import java.util.HashMap;

public class ServiceManager {


    private HashMap<String, Service> services;

    public ServiceManager(){
        services = new HashMap<>();

        HashMap<CostTypes, Double> electricityCosts = new HashMap<>();
        electricityCosts.put(CostTypes.LOW, 50.12);
        electricityCosts.put(CostTypes.AVG, 85.43);
        electricityCosts.put(CostTypes.HIGH, 150.58);
        Service electricityService  = new Service( electricityCosts);

        HashMap<CostTypes, Double> waterCosts = new HashMap<>();
        waterCosts.put(CostTypes.LOW, 20.65);
        waterCosts.put(CostTypes.AVG, 35.34);
        waterCosts.put(CostTypes.HIGH, 50.87);
        Service waterService  = new Service( waterCosts);

        HashMap<CostTypes, Double> internetCosts = new HashMap<>();
        internetCosts.put(CostTypes.LOW, 30.12);
        internetCosts.put(CostTypes.AVG, 55.43);
        internetCosts.put(CostTypes.HIGH, 60.58);
        Service internetService = new Service(internetCosts);

        services.put("Electricity", electricityService);
        services.put("Water", waterService);
        services.put("Internet",internetService);

    }

    public String[] getAllServicesNames(){
        return services.keySet().toArray(String[]::new);
    }

    public double getCost(String serviceName, CostTypes costType){
        return services.get(serviceName).getCost(costType);
    }

    public Service getAServiceByName(String name){
        return services.get(name);
    }

}
