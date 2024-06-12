package org.example.models.animals;

import org.example.GlobalClock;
import org.example.enums.ProductionType;
import org.example.enums.SeasonType;
import org.example.models.Farm;
import org.example.models.Inventory;
import org.example.enums.AnimalState;


import java.util.Random;

public abstract class Animal implements Runnable {
    private final int lifespan;
    private final double chanceOfGettingSick;
    private final int foodConsumption;
    private final int waterConsumption;
    private final int medicineConsumption;
    private final int productionFrequency;
    private int productionQuantity;
    private final ProductionType productionType;
    private boolean isThirsty;
    private boolean isHungry;
    private boolean isSick;

    public Animal(int lifespan, double chanceOfGettingSick, int foodConsumption, int waterConsumption, int medicineConsumption, int productionFrequency, int productionQuantity, ProductionType productionType) {
        this.lifespan = lifespan;
        this.chanceOfGettingSick = chanceOfGettingSick;
        this.foodConsumption = foodConsumption;
        this.waterConsumption = waterConsumption;
        this.medicineConsumption = medicineConsumption;
        this.productionFrequency = productionFrequency;
        this.productionQuantity = productionQuantity;
        this.productionType = productionType;
        this.isThirsty = false;
        this.isHungry = false;
        this.isSick = false;
    }
    public void run() {
        int daysLived = 0;
        int sickDays = 0;
        int hungryDays = 0;
        int thirstyDays = 0;
        GlobalClock clock = GlobalClock.getInstance();

        while (!Thread.currentThread().isInterrupted() && (daysLived / 365) < lifespan && sickDays < 10 && hungryDays < 10 && thirstyDays < 10) {
            try {
                synchronized (clock.getMonitor()) {
                    clock.getMonitor().wait();
                }
                System.out.println("Living day " +  daysLived + "as" + Thread.currentThread().getName());
                // randomized getting sick
                checkIfGetsSick();
                // gets productivity depending on the season
                SeasonType currentSeason = GlobalClock.getInstance().getSeason();
                updateProductivityBasedOnSeason(currentSeason);

                if (!isSick) {
                    sickDays = 0;
                    try {
                        Farm.getInstance().feed(this);
                    } catch (RuntimeException e) {
                        hungryDays++;
                        setHungry(true);
                    }
                    try {
                        Farm.getInstance().giveWater(this);
                    } catch (RuntimeException e) {
                        setThirsty(true);
                        thirstyDays++;
                    }
                    if (daysLived % productionFrequency == 0) {
                        Farm.getInstance().getInventory().addItem(productionType, productionQuantity);
                        System.out.println("Produced " + productionType);
                    }
                } else {
                    sickDays++;
                    try {
                        Farm.getInstance().getHospital().admitAnimal(this);
                    } catch (RuntimeException e) {
                        Thread.sleep(1000);
                    }
                }
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

    public int getProductionQuantity() {
        return productionQuantity;
    }

    public ProductionType getProductionType() {
        return productionType;
    }


    public void updateProductivityBasedOnSeason(SeasonType currentSeason) {
        double seasonalEffectPercentage = calculateSeasonalEffect(currentSeason);
        int adjustedFoodProduction = (int) (productionQuantity * seasonalEffectPercentage);
        setProductionQuantity(adjustedFoodProduction);
    }

    public void setProductionQuantity(int productionQuantity) {
        this.productionQuantity = productionQuantity;
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

    public boolean isThirsty() {
        return isThirsty;
    }

    public void setThirsty(boolean thirsty) {
        isThirsty = thirsty;
    }

    public boolean isHungry() {
        return isHungry;
    }

    public void setHungry(boolean hungry) {
        isHungry = hungry;
    }
}