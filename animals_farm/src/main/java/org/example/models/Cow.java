package org.example.models;

import org.example.enums.FoodType;

public class Cow extends Animal {
    private static final int LIFESPAN = 30;
    private static final double CHANCE_OF_GETTING_SICK = 0.4;
    private static final int FOOD_CONSUMPTION = 14;
    private static final int WATER_CONSUMPTION = 16;
    private static final int FOOD_QUANTITY_PRODUCTION = 16;
    private static final FoodType FOOD_TYPE = FoodType.COW_MILK;

    public Cow() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_QUANTITY_PRODUCTION, FOOD_TYPE);
    }

}
