package org.example.models;

import org.example.enums.ProductionType;
import org.example.enums.SeasonType;

public class Horse extends Animal {
    private static final int LIFESPAN = 25;
    private static final double CHANCE_OF_GETTING_SICK = 0.8;
    private static final int FOOD_CONSUMPTION = 8;
    private static final int WATER_CONSUMPTION = 7;
    private static final int FOOD_PRODUCTION = 30;
    private static final ProductionType FOOD_TYPE = ProductionType.HORSE_MEAT;
    public Horse() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_PRODUCTION, FOOD_TYPE);
    }

    @Override
    protected double calculateSeasonalEffect(SeasonType season) {
        return 0;
    }
}
