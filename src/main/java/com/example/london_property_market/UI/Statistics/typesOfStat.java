package com.example.london_property_market.UI.Statistics;
/**
 * @auther Ashley Tyagi K21008496
 *
 * v1
 */




/**
 * Represents statistics that can be represented in the program
 * Each constant in the enum is a view in the database
 * ID is for looping purposes
 * The name is used as the title
 *
 * To add a statistic
 * Create the view in the database online
 * Add a constant in the enum with ids and names to automatically include them in the program.
 */
public enum typesOfStat {
    avg_reviews_per_property_view(0,"Average number of reviews per property"),
    number_of_properties_available_view(1,"number of properties available"),
    number_of_room_types_view(2,"Number of room types"),
    most_expensive_property_view(3,"Most expensive property"),
    avg_price_per_night(4,"Average price per night"),
    avg_price_per_min_stay(5, "Average price Per minimum stay"),
    cheapest_property_view(6,"Cheapest property per minimum stay"),
    best_seller(7,"Best sellers for these neighbourhood");



    /**
     * id allows looping
     * Name returns a name to be given to the label.
     */
    private final int id;
    private final String name;


    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }

    /***
     * @return returns the typeOfStat after the current one,
     * also loops around to 0 from the end
     */
    public typesOfStat loopForward(){
        typesOfStat[] type = typesOfStat.values();
        return type[(id + 1) % typesOfStat.values().length];
    }

    /**
     * @return returns the typeOfStat before the current one
     * also loops around when you go to the start.
     */
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
