package org.example.models.animals;

import org.example.enums.ProductionType;
import org.example.enums.SeasonType;

public class Cow extends Animal {
    private static final int LIFESPAN = 30;
    private static final double CHANCE_OF_GETTING_SICK = 0.4;
    private static final int FOOD_CONSUMPTION = 14;
    private static final int WATER_CONSUMPTION = 16;
    private static final int MEDICINE_CONSUMPTION = 16;
    private static final int PRODUCTION_FREQUENCY = 2;
    private static final int FOOD_QUANTITY_PRODUCTION = 16;
    private static final ProductionType FOOD_TYPE = ProductionType.COW_MILK;

    public Cow() {
        super(LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, MEDICINE_CONSUMPTION, PRODUCTION_FREQUENCY, FOOD_QUANTITY_PRODUCTION, FOOD_TYPE);
    }

    @Override
    protected double calculateSeasonalEffect(SeasonType season) {
        return switch (season) {
            case SPRING -> 1.2;
            case SUMMER -> 1.0;
            case AUTUMN -> 0.8;
            case WINTER -> 0.5;
        };
    }
}
