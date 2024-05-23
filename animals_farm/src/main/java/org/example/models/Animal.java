package org.example.models;

import org.example.enums.AnimalState;

abstract class Animal implements Runnable {
    private final int lifespan;
    private final double chanceOfGettingSick;
    private final int foodConsumption;
    private final int waterConsumption;
    private final int foodProduction;
    private AnimalState state;
    private boolean isSick;

    public Animal(int lifespan, double chanceOfGettingSick, int foodConsumption, int waterConsumption, int foodProduction ) {
        this.lifespan = lifespan;
        this.chanceOfGettingSick = chanceOfGettingSick;
        this.foodConsumption = foodConsumption;
        this.waterConsumption = waterConsumption;
        this.foodProduction = foodProduction;
        this.state = AnimalState.FULL;
        this.isSick = false;
    }

    public int getLifespan() {
        return lifespan;
    }

    public double getChanceOfGettingSick() {
        return chanceOfGettingSick;
    }

    public int getFoodConsumption() {
        return foodConsumption;
    }
    public int getWaterConsumption() {
        return waterConsumption;
    }

    public int getFoodProduction() {
        return foodProduction;
    }

    public AnimalState getState() {
        return state;
    }

    public void setState(AnimalState state) {
        this.state = state;
    }

    public String getFoodType() {
        return null;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println(this.getClass().getSimpleName() + " is running.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(this.getClass().getSimpleName() + " thread was interrupted.");
            }
        }
    }
}
