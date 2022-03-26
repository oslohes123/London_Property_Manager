package com.example.london_property_market.UI.LifeExpense.Services;

import java.util.HashMap;

/**
 *  This class represent a service manager. This class creates the services and provide access to the models that requires
 *  it. The exclusion of logical methods that handles the interaction with the services, like a pointer to the current
 *  service or the added cost, was made to provide more control to the model in order to separate the responsibilities.
 *
 * @author Yousef Altaher
 * @version 26-03-2022
 */
public class ServiceManager {

    // The services' name and their corresponding objects
    private HashMap<String, Service> services;

    /**
     * This method initiate the service manager object with all its necessary information
     */
    public ServiceManager(){
        services = new HashMap<>();
        createServices();
    }

    /**
     * This method set up the services with their necessary information
     */
    private void createServices(){
        HashMap<CostTypes, Double> electricityCosts = new HashMap<>();
        electricityCosts.put(CostTypes.Low, 50.12);
        electricityCosts.put(CostTypes.Average, 85.43);
        electricityCosts.put(CostTypes.High, 150.58);
        Service electricityService  = new Service(electricityCosts);

        HashMap<CostTypes, Double> waterCosts = new HashMap<>();
        waterCosts.put(CostTypes.Low, 20.65);
        waterCosts.put(CostTypes.Average, 35.34);
        waterCosts.put(CostTypes.High, 50.87);
        Service waterService  = new Service( waterCosts);

        HashMap<CostTypes, Double> internetCosts = new HashMap<>();
        internetCosts.put(CostTypes.Low, 30.12);
        internetCosts.put(CostTypes.Average, 55.43);
        internetCosts.put(CostTypes.High, 60.58);
        Service internetService = new Service(internetCosts);

        services.put("Electricity", electricityService);
        services.put("Water", waterService);
        services.put("Internet",internetService);
    }

    /**
     * This method returns the names of the available services
     * @return the names of the available services
     */
    public String[] getAllServicesNames(){
        return services.keySet().toArray(String[]::new);
    }

    /**
     * This method returns a service by its name
     * @param name name of the service
     * @return the service object
     */
    public Service getAServiceByName(String name){
        return services.get(name);
    }

}
