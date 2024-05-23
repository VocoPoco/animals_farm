package org.example.models;

public class Goat extends Animal {
    private static final int LIFESPAN = 10;
    private static final double CHANCE_OF_GETTING_SICK = 0.3;
    private static final int FOOD_CONSUMPTION = 3;
    private static final int WATER_CONSUMPTION = 3;
    private static final int FOOD_PRODUCTION = 6;
    public Goat() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_PRODUCTION);
    }

    @Override
    public String getFoodType() {
        return "goat_food";
    }
}
