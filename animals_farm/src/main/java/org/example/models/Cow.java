package org.example.models;

public class Cow extends Animal {
    private static final int LIFESPAN = 30;
    private static final double CHANCE_OF_GETTING_SICK = 0.4;
    private static final int FOOD_CONSUMPTION = 14;
    private static final int WATER_CONSUMPTION = 16;
    private static final int FOOD_PRODUCTION = 16;
    public Cow() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_PRODUCTION);
    }

    @Override
    public String getFoodType() {
        return "cow_food";
    }
}
