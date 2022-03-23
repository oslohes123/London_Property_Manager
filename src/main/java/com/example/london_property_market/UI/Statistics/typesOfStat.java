package com.example.london_property_market.UI.Statistics;

public enum typesOfStat {
    avgReviewsPerProperty(0,"Average reviews per property"),
    number_of_properties_available(1,"number of properties available"),
    numberOfRoomTypes(2,"Number of room types"),
    mostExpensiveProperty(3,"Most expensive property"),
    averagePricePerNight(4,"Average price per night"),
    averagePricePerMinStay(5, "Average price Per minimum stay");

    private int id;
    private String name;

    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public typesOfStat loopForward(){
        typesOfStat[] type = typesOfStat.values();
        return type[(id + 1) % typesOfStat.values().length];
    }

    public typesOfStat loopBackward(){
        int total = typesOfStat.values().length;
        typesOfStat[] type = typesOfStat.values();
        return type[((id -1) + total) % total];
    }


    typesOfStat(int id,String name){
        this.id = id;
        this.name = name;
    }


}
