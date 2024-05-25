package org.example.models;

import org.example.enums.ProductionType;
import org.example.enums.SeasonType;

public class Goat extends Animal {
    private static final int LIFESPAN = 10;
    private static final double CHANCE_OF_GETTING_SICK = 0.3;
    private static final int FOOD_CONSUMPTION = 3;
    private static final int WATER_CONSUMPTION = 3;
    private static final int FOOD_PRODUCTION = 2;
    private static final ProductionType FOOD_TYPE = ProductionType.GOAT_MILK;

    public Goat() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_PRODUCTION, FOOD_TYPE);
    }

    @Override
    protected double calculateSeasonalEffect(SeasonType season) {
        return 0;
    }
}
