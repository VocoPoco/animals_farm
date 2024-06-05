package org.example.models;

import org.example.enums.ProductionType;
import org.example.enums.SeasonType;

public class Horse extends Animal {
    private static final int LIFESPAN = 25;
    private static final double CHANCE_OF_GETTING_SICK = 0.8;
    private static final int FOOD_CONSUMPTION = 8;
    private static final int WATER_CONSUMPTION = 7;
    private static final int MEDICINE_CONSUMPTION = 16;
    private static final int PRODUCTION_FREQUENCY = 10;
    private static final int FOOD_QUANTITY_PRODUCTION = 30;
    private static final ProductionType FOOD_TYPE = ProductionType.HORSE_MEAT;
    public Horse() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, MEDICINE_CONSUMPTION, PRODUCTION_FREQUENCY, FOOD_QUANTITY_PRODUCTION, FOOD_TYPE);
    }

    @Override
    protected double calculateSeasonalEffect(SeasonType season) {
        return switch (season) {
            case SPRING -> 1.10;
            case SUMMER -> 0.90;
            case AUTUMN -> 0.85;
            case WINTER -> 0.70;
        };
    }
}
