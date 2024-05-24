package org.example.models;

import org.example.enums.FoodType;

public class Pig extends Animal {

    private static final int LIFESPAN = 10;
    private static final double CHANCE_OF_GETTING_SICK = 0.3;
    private static final int FOOD_CONSUMPTION = 3;
    private static final int WATER_CONSUMPTION = 2;
    private static final int FOOD_PRODUCTION = 20;
    private static final FoodType FOOD_TYPE = FoodType.PORK;

    public Pig() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_PRODUCTION, FOOD_TYPE);
    }
}
