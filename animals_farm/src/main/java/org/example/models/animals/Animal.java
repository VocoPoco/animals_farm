package org.example.models.animals;

import org.example.GlobalClock;
import org.example.enums.AnimalState;
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
    private void determineState(int hungryDays, int thirstyDays) {
        if (hungryDays != 0 && thirstyDays != 0) {
            setState(AnimalState.HUNGRY_THIRSTY);
        } else if (hungryDays != 0) {
            setState(AnimalState.DRENCHED_HUNGRY);
        } else if (thirstyDays != 0) {
            setState(AnimalState.FED_THIRSTY);
        } else {
            setState(AnimalState.FULL);
        }
    }

    public void run() {
        int daysLived = 0;
        int sickDays = 0;
        int hungryDays = 0;
        int thirstyDays = 0;
        GlobalClock clock = GlobalClock.getInstance();

        while (!Thread.currentThread().isInterrupted() && daysLived < lifespan && sickDays < 10 && hungryDays < 10 && thirstyDays < 10) {
            try {
                synchronized (clock.getMonitor()) {
                    clock.getMonitor().wait();
                }
                System.out.println("Living day " +  daysLived);
                checkIfGetsSick();
                SeasonType currentSeason = GlobalClock.getInstance().getSeason();
                updateProductivityBasedOnSeason(currentSeason);

                if (!isSick) {
                    sickDays = 0;
                    try {
                        Farm.getInstance().feed(this);
                    } catch (RuntimeException e) {
                        hungryDays++;
                    }
                    System.out.println("Ate and Drank!");
                    try {
                        Farm.getInstance().giveWater(this);
                    } catch (RuntimeException e) {
                        thirstyDays++;
                    }
                    if (daysLived % productionFrequency == 0) {
                        Inventory.getInstance().addItem(productionType, foodQuantityProduction);
                        System.out.println("Produced thingy wingy");
                    }
                } else {
                    sickDays++;
                    try {
                        Farm.getInstance().getHospital().admitAnimal(this);
                    } catch (RuntimeException e) {
                        Thread.sleep(1000);
                    }
                }
                determineState(hungryDays, thirstyDays);
                daysLived++;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (hungryDays >= 10 || thirstyDays >= 10 || sickDays >= 10) {
            Farm.getInstance().killAnimal(this);
        }
    }
}