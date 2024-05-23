package org.example.models;

public class Duck extends Animal {
    private static final int LIFESPAN = 7;
    private static final double CHANCE_OF_GETTING_SICK = 0.1;
    private static final int FOOD_CONSUMPTION = 2;
    private static final int WATER_CONSUMPTION = 3;
    private static final int FOOD_PRODUCTION = 9;
    public Duck() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_PRODUCTION);
    }

    @Override
    public String getFoodType() {
        return "duck_food";
    }
}
