package org.example.models.animals;

import org.example.GlobalClock;
import org.example.enums.ProductionType;
import org.example.enums.SeasonType;
import org.example.models.Farm;
import org.example.models.Inventory;

import java.sql.SQLOutput;
import java.util.Random;

public abstract class Animal implements Runnable {
    private int id;
    private final int lifespan;
    private final double chanceOfGettingSick;
    private final int foodConsumption;
    private final int waterConsumption;
    private final int medicineConsumption;
    private final int productionFrequency;
    private int foodQuantityProduction;
    private final ProductionType productionType;
    private boolean isHungry;
    private boolean isThirsty;
    private boolean isSick;

    public Animal(int id, int lifespan, double chanceOfGettingSick, int foodConsumption, int waterConsumption, int medicineConsumption, int productionFrequency, int foodQuantityProduction, ProductionType productionType) {
        this.id = id;
        this.lifespan = lifespan;
        this.chanceOfGettingSick = chanceOfGettingSick;
        this.foodConsumption = foodConsumption;
        this.waterConsumption = waterConsumption;
        this.medicineConsumption = medicineConsumption;
        this.productionFrequency = productionFrequency;
        this.foodQuantityProduction = foodQuantityProduction;
        this.productionType = productionType;
        this.isHungry = true;
        this.isThirsty = true;
        this.isSick = false;
    }

    public int getId() {
        return id;
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

    public boolean getIsHungry() {
        return isHungry;
    }

    public boolean getIsThirsty() {
        return isThirsty;
    }

    public void setIsHungry(boolean isHungry) {
        this.isHungry = isHungry;
    }

    public void setIsThirsty(boolean isThirsty) {
        this.isThirsty = isThirsty;
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
            System.out.println("Animal got sick.");
            setIsSick(true);
        }
    }
    private void determineState(int hungryDays, int thirstyDays) {
        if (hungryDays != 0 && thirstyDays != 0) {
            setIsHungry(true);
            setIsThirsty(true);
        } else if (hungryDays != 0) {
            setIsHungry(true);
            setIsThirsty(false);
        } else if (thirstyDays != 0) {
            setIsHungry(false);
            setIsThirsty(true);
        } else {
            setIsHungry(false);
            setIsThirsty(false);
        }
    }

    public String getAnimalInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append(" with ID: ").append(this.id).append("\n");
        sb.append("Lifespan: ").append(lifespan).append("\n");
        sb.append("Chance of getting sick: ").append(chanceOfGettingSick).append("\n");
        sb.append("Food consumption: ").append(foodConsumption).append("\n");
        sb.append("Water consumption: ").append(waterConsumption).append("\n");
        sb.append("Medicine consumption: ").append(medicineConsumption).append("\n");
        sb.append("Production frequency: ").append(productionFrequency).append("\n");
        sb.append("Production quantity: ").append(productionFrequency).append("\n");
        sb.append("Production type: ").append(productionType).append("\n");
        sb.append("IsHungry: ").append(isHungry).append(", IsThirsty: ").append(isThirsty).append(", isSick: ").append(isSick).append("\n\n");
        return sb.toString();

    }

    public void run() {
        //da proizvejdat neshto
        //da pravqt vsichki jivotni neshtata ednovremenno
        //kogato ovcata umre pri printiraneto na fermata broqt e 0 no kogato kupq nova ovca tq pochva da jivee da qde i tn
        //no vuv fermata stavat 2 ovcete veche?????
        //malko da se slojat entery za da e po-chetimo!
        //da se napravi da mogat da se kupuvat i drugi neshta - gotovo
        //da se zanulqvat dnite na glad i jajda - gotovo
        int daysLived = 0;
        int sickDays = 0;
        int hungryDays = 0;
        int thirstyDays = 0;
        GlobalClock clock = GlobalClock.getInstance();

        while (!Thread.currentThread().isInterrupted() && daysLived < lifespan && sickDays < 5 && hungryDays < 5 && thirstyDays < 5) {
            try {
                synchronized (clock.getMonitor()) {
                    clock.getMonitor().wait();
                }
                System.out.println("Living day " +  daysLived);
                checkIfGetsSick();
                SeasonType currentSeason = GlobalClock.getInstance().getSeason();
                updateProductivityBasedOnSeason(currentSeason);

                if (!this.isSick) {
                    sickDays = 0;
                    if(this.isHungry) {
                        System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " is hungry.");
                        try {
                            System.out.println("Trying to feed animal.");
                            Farm.getInstance().feed(this);
                            hungryDays = 0;
                        } catch (RuntimeException e) {
                            hungryDays++;
                        }
                    }
                    System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " ate!");
                    if(this.isThirsty) {
                        System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " is thirsty.");
                        try {
                            System.out.println("Trying to give animal water.");
                            Farm.getInstance().giveWater(this);
                            thirstyDays = 0;
                        } catch (RuntimeException e) {
                            thirstyDays++;
                        }
                        System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " drank!");
                    }
                } else {
                    System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " is sick.");
                    sickDays++;
                    try {
                        System.out.println("Trying to put animal in hospital.");
                        Farm.getInstance().putAnimalInHospital(this);
                        System.out.println(this.getClass().getSimpleName() + "with ID: " + this.id + " is not sick anymore.");
                    } catch (RuntimeException e) {
                        Thread.sleep(1000);
                    }
                }
                determineState(hungryDays, thirstyDays);
                daysLived++;
                this.setIsHungry(true);
                this.setIsThirsty(true);
                System.out.println("--- SUMMARY OF THE DAY: ---");
                System.out.println(Farm.getInstance().getDailySummary());
                System.out.println("\n");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (hungryDays >= 3) {
            System.out.println(this.getClass().getSimpleName() + "with ID: " + this.id + " died of hunger.");
            Farm.getInstance().killAnimal(this);
        }
        if (thirstyDays >= 3) {
            System.out.println(this.getClass().getSimpleName() + "with ID: " + this.id + " died of thirst.");
        }
        if (sickDays >= 3) {
            System.out.println(this.getClass().getSimpleName() + "with ID: " + this.id + " died of sickness.");
            Farm.getInstance().killAnimal(this);
        }
        else {
            System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " died.");
            Farm.getInstance().killAnimal(this);
        }
    }
}