package org.example.models;

public class Hen extends Animal {
    private static final int LIFESPAN = 5;
    private static final double CHANCE_OF_GETTING_SICK = 0.1;
    private static final int FOOD_CONSUMPTION = 1;
    private static final int WATER_CONSUMPTION = 1;
    private static final int FOOD_PRODUCTION = 10;
    public Hen() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_PRODUCTION);
    }

    @Override
    public String getFoodType() {
        return "hen_food";
    }
}
