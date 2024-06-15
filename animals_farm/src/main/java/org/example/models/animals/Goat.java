package org.example.models.animals;

import org.example.enums.ProductionType;
import org.example.enums.SeasonType;

public class Goat extends Animal {
    private static final int LIFESPAN = 10;
    private static final double CHANCE_OF_GETTING_SICK = 10;
    private static final int FOOD_CONSUMPTION = 3;
    private static final int WATER_CONSUMPTION = 3;
    private static final int MEDICINE_CONSUMPTION = 16;
    private static final int PRODUCTION_FREQUENCY = 2;
    private static final int FOOD_QUANTITY_PRODUCTION = 4;
    private static final ProductionType FOOD_TYPE = ProductionType.GOAT_MILK;

    public Goat(int id) {
        super(id, LIFESPAN, CHANCE_OF_GETTING_SICK, FOOD_CONSUMPTION, WATER_CONSUMPTION, MEDICINE_CONSUMPTION, PRODUCTION_FREQUENCY,  FOOD_QUANTITY_PRODUCTION, FOOD_TYPE);
    }

    @Override
    protected double calculateSeasonalEffect(SeasonType season) {
        return switch (season) {
            case SPRING -> 1.10;
            case SUMMER -> 0.80;
            case AUTUMN -> 0.85;
            case WINTER -> 0.72;
        };
    }
}
