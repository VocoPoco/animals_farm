package org.example.models;

public class Sheep extends Animal {
    private static final int LIFESPAN = 12;
    private static final double CHANCE_OF_GETTING_SICK = 0.5;
    private static final int FOOD_CONSUMPTION = 2;
    private static final int WATER_CONSUMPTION = 1;
    private static final int FOOD_PRODUCTION = 4;
    public Sheep() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_PRODUCTION);
    }

    @Override
    public String getFoodType() {
        return "sheep_food";
    }
}
