package com.example.london_property_market.Loader;

import com.univocity.parsers.annotations.Parsed;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Represents one listing of a property for rental on Airbnb.
 * This is essentially one row in the data table. Each column
 * has a corresponding field.
 */ 

public class AirbnbListing {
    /**
     * The id and name of the individual property
     */
    @Parsed(index=0)
    private String id;
    @Parsed(index=1)
    private String name;
    /**
     * The id and name of the host for this listing.
     * Each listing has only one host, but one host may
     * list many properties.
     */
    @Parsed(index=2)
    private String host_id;
    @Parsed(index=3)
    private String host_name;

    /**
     * The grouped location to where the listed property is situated.
     * For this data set, it is a london borough.
     */
    @Parsed(index=4)
    private String neighbourhood;

    /**
     * The location on a map where the property is situated.
     */
    @Parsed(index=5)
    private double latitude;
    @Parsed(index=6)
    private double longitude;

    /**
     * The type of property, either "Private room" or "Entire Home/apt".
     */
    @Parsed(index=7)
    private String room_type;

    /**
     * The price per night's stay
     */
    @Parsed(index=8)
    private int price;

    /**
     * The minimum number of nights the listed property must be booked for.
     */
    @Parsed(index=9)
    private int minimumNights;
    @Parsed(index=10)
    private int numberOfReviews;

    /**
     * The date of the last review, but as a String
     */
    @Parsed(index=11)
    private String lastReview;
    @Parsed(index=12)
    private double reviewsPerMonth;

    /**
     * The total number of listings the host holds across AirBnB
     */
    @Parsed(index=13)
    private int calculatedHostListingsCount;
    /**
     * The total number of days in the year that the property is available for
     */
    @Parsed(index=14)
    private int availability365;

    public AirbnbListing(){}

    public AirbnbListing(String id, String name, String host_id,
                         String host_name, String neighbourhood, double latitude,
                         double longitude, String room_type, int price,
                         int minimumNights, int numberOfReviews, String lastReview,
                         double reviewsPerMonth, int calculatedHostListingsCount, int availability365) {
        this.id = id;
        this.name = name;
        this.host_id = host_id;
        this.host_name = host_name;
        this.neighbourhood = neighbourhood;
        this.latitude = latitude;
        this.longitude = longitude;
        this.room_type = room_type;
        this.price = price;
        this.minimumNights = minimumNights;
        this.numberOfReviews = numberOfReviews;
        this.lastReview = lastReview;
        this.reviewsPerMonth = reviewsPerMonth;
        this.calculatedHostListingsCount = calculatedHostListingsCount;
        this.availability365 = availability365;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHost_id() {
        return host_id;
    }

    public String getHost_name() {
        return host_name;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getRoom_type() {
        return room_type;
    }

    public int getPrice() {
        return price;
    }

    public int getMinimumNights() {
        return minimumNights;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public String getLastReview() {
        return lastReview;
    }

    public double getReviewsPerMonth() {
        return reviewsPerMonth;
    }

    public int getCalculatedHostListingsCount() {
        return calculatedHostListingsCount;
    }

    public int getAvailability365() {
        return availability365;
    }

    protected static String[] getDateNames(){
        return Arrays.stream(AirbnbListing.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);
    }

    @Override
    public String toString() {
        return "AirbnbListing{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", host_id='" + host_id + '\'' +
                ", host_name='" + host_name + '\'' +
                ", neighbourhood='" + neighbourhood + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", room_type='" + room_type + '\'' +
                ", price=" + price +
                ", minimumNights=" + minimumNights +
                ", numberOfReviews=" + numberOfReviews +
                ", lastReview='" + lastReview + '\'' +
                ", reviewsPerMonth=" + reviewsPerMonth +
                ", calculatedHostListingsCount=" + calculatedHostListingsCount +
                ", availability365=" + availability365 +
                '}';
    }
}
