package org.example.models;

import org.example.enums.ProductionType;
import org.example.enums.SeasonType;

public class Duck extends Animal {
    private static final int LIFESPAN = 7;
    private static final double CHANCE_OF_GETTING_SICK = 0.1;
    private static final int FOOD_CONSUMPTION = 2;
    private static final int WATER_CONSUMPTION = 3;
    private static final int FOOD_QUANTITY_PRODUCTION = 9;
    private static final ProductionType FOOD_TYPE = ProductionType.FEATHER;

    public Duck() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_QUANTITY_PRODUCTION, FOOD_TYPE);
    }

    @Override
    protected double calculateSeasonalEffect(SeasonType season) {
        return 0;
    }
}
