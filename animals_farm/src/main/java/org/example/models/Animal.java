package org.example.models;

abstract class Animal {
    private final int lifespan;
    private final int chanceOfGettingSick;
    private final int foodConsumption;
    private final int foodProduction;

    public Animal(int lifespan, int chanceOfGettingSick, int foodConsumption, int foodProduction ) {
        this.lifespan = lifespan;
        this.chanceOfGettingSick = chanceOfGettingSick;
        this.foodConsumption = foodConsumption;
        this.foodProduction = foodProduction;
    }

    public int getLifespan() {
        return lifespan;
    }

    public int getChanceOfGettingSick() {
        return chanceOfGettingSick;
    }

    public int getFoodConsumption() {
        return foodConsumption;
    }

    public int getFoodProduction() {
        return foodProduction;
    }
}
