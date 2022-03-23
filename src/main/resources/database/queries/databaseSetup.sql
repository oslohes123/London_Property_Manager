-- This file needs to do a couple of things:
    -- Create the locations table in the database
    -- Populate the locations table with the data from the csv

CREATE TABLE IF NOT EXISTS airbnb_locations (
    id int,
    name varchar(255),
    host_id int,
    host_name varchar(255),
    neighbourhood varchar(255),
    latitude decimal,
    longitude decimal,
    room_type varchar(255),
    price int,
    minimum_nights int,
    number_of_reviews int,
    last_review date,
    reviews_per_month double,
    calculated_host_listings_count int,
    availability_365 int
) AS
    SELECT
        *
    FROM
        CSVREAD('./src/main/resources/database/airbnb.csv');

-- Create an average reviews per borough]
CREATE VIEW IF NOT EXISTS avg_reviews_per_property_view AS
    SELECT
        AVG(NUMBER_OF_REVIEWS) AS AVERAGE_REVIEWS,
        NEIGHBOURHOOD
    FROM
        airbnb_locations
    group by
        NEIGHBOURHOOD;

-- Total Number of Available Properties
CREATE VIEW IF NOT EXISTS number_of_properties_available_view AS
    SELECT
        COUNT(*) AS NO_PROPERTIES_AVAILABLE,
        NEIGHBOURHOOD
    FROM
        airbnb_locations
    WHERE
        AVAILABILITY_365 > 0
    GROUP BY
        NEIGHBOURHOOD;

-- Number of different room types
CREATE VIEW IF NOT EXISTS number_of_room_types_view AS
    SELECT
        COUNT(*) AS AMOUNT,
        ROOM_TYPE,
        NEIGHBOURHOOD
    FROM
        airbnb_locations
    GROUP BY
        NEIGHBOURHOOD,
        ROOM_TYPE;

-- The most expensive airbnb in the borough, multiply min nights by price
CREATE VIEW IF NOT EXISTS most_expensive_property_view AS
    SELECT
        MAX(PRICE * MINIMUM_NIGHTS) AS PRICE,
        NEIGHBOURHOOD
    FROM
         airbnb_locations
    GROUP BY
        NEIGHBOURHOOD;

-- Average Price per night per borough
CREATE VIEW IF NOT EXISTS avg_price_per_night AS
    SELECT
        AVG(PRICE) AS AVG_PRICE,
        NEIGHBOURHOOD
    FROM
        airbnb_locations
    GROUP BY
        NEIGHBOURHOOD;

-- Average Price Per min stay
CREATE VIEW IF NOT EXISTS avg_price_per_min_stay AS
    SELECT
        AVG(PRICE*MINIMUM_NIGHTS) AS AVG_PRICE_PER_STAY,
        NEIGHBOURHOOD
    FROM
        airbnb_locations
    GROUP BY
        NEIGHBOURHOOD;

-- Cheapest property
CREATE VIEW IF NOT EXISTS cheapest_property_view AS
    SELECT
    MIN(airbnb_locations.price * airbnb_locations.minimum_nights) AS PRICE, neighbourhood
    FROM airbnb_locations
    GROUP BY
             neighbourhood;

-- Longest description
CREATE VIEW IF NOT EXISTS longest_description_view AS
SELECT
    name,LENGTH(name)AS length,neighbourhood
FROM airbnb_locations
GROUP BY
    length, neighbourhood
order by
    length desc;