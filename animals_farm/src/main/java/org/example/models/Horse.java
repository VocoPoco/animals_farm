package org.example.models;

public class Horse extends Animal {
    private static final int LIFESPAN = 25;
    private static final double CHANCE_OF_GETTING_SICK = 0.8;
    private static final int FOOD_CONSUMPTION = 8;
    private static final int WATER_CONSUMPTION = 7;
    private static final int FOOD_PRODUCTION = 2;
    public Horse() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_PRODUCTION);
    }

    @Override
    public String getFoodType() {
        return "horse_food";
    }
}
