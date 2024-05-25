package org.example.models;

import org.example.enums.ProductionType;
import org.example.enums.SeasonType;

public class Cow extends Animal {
    private static final int LIFESPAN = 30;
    private static final double CHANCE_OF_GETTING_SICK = 0.4;
    private static final int FOOD_CONSUMPTION = 14;
    private static final int WATER_CONSUMPTION = 16;
    private static final int FOOD_QUANTITY_PRODUCTION = 16;
    private static final ProductionType FOOD_TYPE = ProductionType.COW_MILK;

    public Cow() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, FOOD_QUANTITY_PRODUCTION, FOOD_TYPE);
    }

    @Override
    protected double calculateSeasonalEffect(SeasonType season) {
        return 0;
    }
}
