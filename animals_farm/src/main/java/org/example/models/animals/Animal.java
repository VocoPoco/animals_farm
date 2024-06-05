package org.example.models.animals;

import org.example.GlobalClock;
import org.example.enums.AnimalState;
import org.example.enums.OtherType;
import org.example.enums.ProductionType;
import org.example.enums.SeasonType;
import org.example.models.Farm;
import org.example.models.Inventory;

import java.util.Random;

public abstract class Animal implements Runnable {
    private final int lifespan;
    private final double chanceOfGettingSick;
    private final int foodConsumption;
    private final int waterConsumption;
    private final int medicineConsumption;
    private final int productionFrequency;
    private int foodQuantityProduction;
    private final ProductionType productionType;
    private AnimalState state;
    private boolean isSick;

    public Animal(int lifespan, double chanceOfGettingSick, int foodConsumption, int waterConsumption, int medicineConsumption, int productionFrequency, int foodQuantityProduction, ProductionType productionType) {
        this.lifespan = lifespan;
        this.chanceOfGettingSick = chanceOfGettingSick;
        this.foodConsumption = foodConsumption;
        this.waterConsumption = waterConsumption;
        this.medicineConsumption = medicineConsumption;
        this.productionFrequency = productionFrequency;
        this.foodQuantityProduction = foodQuantityProduction;
        this.productionType = productionType;
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
    public int getMedicineConsumption() {
        return medicineConsumption;
    }

    public int getFoodQuantityProduction() {
        return foodQuantityProduction;
    }

    public ProductionType getFoodType() {
        return productionType;
    }

    public AnimalState getState() {
        return state;
    }

    public void setState(AnimalState state) {
        this.state = state;
    }

    public void updateProductivityBasedOnSeason(SeasonType currentSeason) {
        double seasonalEffectPercentage = calculateSeasonalEffect(currentSeason);
        int adjustedFoodProduction = (int) (foodQuantityProduction * seasonalEffectPercentage);
        setFoodQuantityProduction(adjustedFoodProduction);
    }

    public void setFoodQuantityProduction(int foodQuantityProduction) {
        this.foodQuantityProduction = foodQuantityProduction;
    }

    protected abstract double calculateSeasonalEffect(SeasonType season);

    public boolean getIsSick() {
        return isSick;
    }

    public void setIsSick(boolean sick) {
        isSick = sick;
    }

    private void checkIfGetsSick() {
        Random random = new Random();
        if (random.nextDouble() < chanceOfGettingSick) {
            setIsSick(true);
        }
    }

    public void run() {
        int daysLived = 0;
        int sickDays = 0;
        int hungryDays = 0;
        int thirstyDays = 0;
        while (!Thread.currentThread().isInterrupted() && daysLived < lifespan && sickDays < 10 && hungryDays < 10 && thirstyDays < 10) {
            System.out.println(this.getClass().getSimpleName() + " is running.");
            try {
                checkIfGetsSick();
                SeasonType currentSeason = GlobalClock.getInstance().getSeason();
                updateProductivityBasedOnSeason(currentSeason);
                Farm farm = Farm.getInstance();
                if (!isSick) {
                    sickDays = 0;
                    try {
                        farm.feed(this);
                    } catch (RuntimeException e) {
                        hungryDays++;
                    }
                    try {
                        farm.giveWater(this);
                    } catch (RuntimeException e) {
                        thirstyDays++;
                    }

                    System.out.println("Ate and Drank!");
                    if (daysLived % productionFrequency == 0) {
                        Inventory.getInstance().addItem(productionType, foodQuantityProduction);
                        System.out.println("Produced " + foodQuantityProduction + " " + productionType);
                    }
                } else {
                    sickDays++;
                    System.out.println(this.getClass().getSimpleName() + " is sick and cannot produce food today.");
                    try {
                        farm.getHospital().admit(this);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println(this.getClass().getSimpleName() + " thread was interrupted while trying to get treated.");
                    }
                }
                Thread.sleep(1000);
                daysLived++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(this.getClass().getSimpleName() + " thread was interrupted.");
            }
        }

        if (hungryDays >= 10) {
            System.out.println(this.getClass().getSimpleName() + " died of hunger.");
        }
        if (thirstyDays >= 10) {
            System.out.println(this.getClass().getSimpleName() + " died of thirst.");
        }
        if (sickDays >= 10) {
            System.out.println(this.getClass().getSimpleName() + " died of sickness.");
        }

        if (hungryDays >= 10 || thirstyDays >= 10 || sickDays >= 10) {
            Farm.getInstance().killAnimal(this);
        }
    }
}