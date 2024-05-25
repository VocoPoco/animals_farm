package org.example.models;

import org.example.enums.ProductionType;
import org.example.enums.SeasonType;

public class Hen extends Animal {
    private static final int LIFESPAN = 5;
    private static final double CHANCE_OF_GETTING_SICK = 0.1;
    private static final int FOOD_CONSUMPTION = 1;
    private static final int WATER_CONSUMPTION = 1;
    private static final int FOOD_PRODUCTION = 3;
    private static final ProductionType FOOD_TYPE = ProductionType.EGGS;

    public Hen() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_PRODUCTION, FOOD_TYPE);
    }

    @Override
    protected double calculateSeasonalEffect(SeasonType season) {
        return 0;
    }
}
