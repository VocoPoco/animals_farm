package org.example.models;

import org.example.GlobalClock;
import org.example.enums.AnimalState;
import org.example.enums.FoodType;
import org.example.enums.SeasonType;

import java.util.Random;

abstract class Animal implements Runnable {
    private final int lifespan;
    private final double chanceOfGettingSick;
    private final int foodConsumption;
    private final int waterConsumption;
    private final int productionFrequency;
    private int foodQuantityProduction; // how much
    private final FoodType foodType; // what produces
    private AnimalState state;
    private boolean isSick;
    private double seasonalEffectPercentage;

    public Animal(int lifespan, double chanceOfGettingSick, int foodConsumption, int waterConsumption, int productionFrequency, int foodProduction, FoodType foodType) {
        this.lifespan = lifespan;
        this.chanceOfGettingSick = chanceOfGettingSick;
        this.foodConsumption = foodConsumption;
        this.waterConsumption = waterConsumption;
        this.productionFrequency = productionFrequency;
        this.foodQuantityProduction = foodProduction;
        this.foodType = foodType;
        this.state = AnimalState.FULL;
        this.isSick = false;
        this.seasonalEffectPercentage = 1.0;

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

    public int getFoodQuantityProduction() {
        return foodQuantityProduction;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public AnimalState getState() {
        return state;
    }

    public void setState(AnimalState state) {
        this.state = state;
    }

    public void setFoodQuantityProduction(int foodQuantityProduction) {
        this.foodQuantityProduction = foodQuantityProduction;
        updateProductivityBasedOnSeason(GlobalClock.getInstance().getSeason());    }

    public void setSick(boolean sick) {
        isSick = sick;
    }

    public void setSeasonalEffectPercentage(double seasonalEffectPercentage) {
        this.seasonalEffectPercentage = seasonalEffectPercentage;
        updateProductivityBasedOnSeason(GlobalClock.getInstance().getSeason());
    }

    public double getSeasonalEffectPercentage() {
        return seasonalEffectPercentage;
    }

    protected abstract double calculateSeasonalEffect(SeasonType season);

    public void updateProductivityBasedOnSeason(SeasonType currentSeason) {
        this.seasonalEffectPercentage = calculateSeasonalEffect(currentSeason);
        int adjustedFoodProduction = (int) (foodQuantityProduction * seasonalEffectPercentage);
        setFoodQuantityProduction(adjustedFoodProduction);
    }

    private void checkIfGetsSick() {
        Random random = new Random();
        if (random.nextDouble() < chanceOfGettingSick) {
            setSick(true);
        }
    }


    @Override
    public void run() {
        int daysLived = 0;
        while (!Thread.currentThread().isInterrupted() && daysLived < lifespan) {
            System.out.println(this.getClass().getSimpleName() + " is running.");
            try {
                checkIfGetsSick();
                SeasonType currentSeason = GlobalClock.getInstance().getSeason();
                updateProductivityBasedOnSeason(currentSeason);

                // for the implementation of the other methods i need the inventory class
                if (!isSick) {
                    // get water and food from the storage reserve
                    System.out.println("Ate And Drank!");
                    if (daysLived % productionFrequency == 0) {
                        // get the
                        System.out.println("Produced " + foodQuantityProduction + " " + foodType);
                    }
                } else {
                    System.out.println(this.getClass().getSimpleName() + " is sick and cannot produce food today.");
                }
                Thread.sleep(1000);
                daysLived++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(this.getClass().getSimpleName() + " thread was interrupted.");
            }
        }
    }
}
