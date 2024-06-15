package org.example.models.animals;

import org.example.GlobalClock;
import org.example.enums.ProductionType;
import org.example.enums.SeasonType;
import org.example.models.Farm;

import java.util.Random;

public abstract class Animal implements Runnable {
    private int id;
    private final int lifespan;
    private final double chanceOfGettingSick;
    private final int foodConsumption;
    private final int waterConsumption;
    private final int medicineConsumption;
    private final int productionFrequency;
    private int productionQuantity;
    private final ProductionType productionType;
    private boolean isHungry;
    private boolean isThirsty;
    private boolean isSick;

    public Animal(int id, int lifespan, double chanceOfGettingSick, int foodConsumption, int waterConsumption, int medicineConsumption, int productionFrequency, int productionQuantity, ProductionType productionType) {
        this.id = id;
        this.lifespan = lifespan;
        this.chanceOfGettingSick = chanceOfGettingSick;
        this.foodConsumption = foodConsumption;
        this.waterConsumption = waterConsumption;
        this.medicineConsumption = medicineConsumption;
        this.productionFrequency = productionFrequency;
        this.productionQuantity = productionQuantity;
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

    public int getProductionQuantity() {
        return productionQuantity;
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
        if (random.nextDouble(10) < chanceOfGettingSick) {
            System.out.println("Animal got sick.");
            setIsSick(true);
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

    @Override
    public void run() {
        int daysLived = 1;
        int sickDays = 0;
        int hungryDays = 0;
        int thirstyDays = 0;
        GlobalClock clock = GlobalClock.getInstance();
        Farm farm = Farm.getInstance();

        while (!Thread.currentThread().isInterrupted() && daysLived < lifespan) {
            try {
                synchronized (clock.getMonitor()) {
                    clock.getMonitor().wait();
                }
                System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " is living day " + daysLived);

                checkIfGetsSick();
                SeasonType currentSeason = clock.getSeason();
                updateProductivityBasedOnSeason(currentSeason);
                if (!isSick) {
                    if (isHungry) {
                        System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " is hungry.");
                        synchronized (farm) {
                            farm.feed(this);
                        }
                        if(!isHungry) {
                            hungryDays = 0;
                            System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " ate!");
                        } else {
                            hungryDays++;
                        }
                    }
                    if (isThirsty) {
                        System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " is thirsty.");
                        synchronized (farm) {
                            farm.giveWater(this);
                        }
                        if(!isThirsty) {
                            thirstyDays = 0;
                            System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " drank!");
                        } else {
                            thirstyDays++;
                        }
                    }
                    if (daysLived % productionFrequency == 0) {
                        farm.getInventory().addItem(productionType, productionQuantity);
                        System.out.println("______________Produced " + productionType + " " + productionType);
                    }
                } else {
                    System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " is sick.");
                    synchronized (farm) {
                        farm.putAnimalInHospital(this);
                    }
                    if(!isSick) {
                        sickDays = 0;
                        System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " is not sick anymore.");
                    } else {
                        sickDays++;
                    }
                }
                daysLived++;
                setIsHungry(true);
                setIsThirsty(true);
                System.out.println("--- SUMMARY OF THE DAY: ---");
                synchronized (farm) {
                    System.out.println(farm.getDailySummary());
                }
                System.out.println("\n");

                if (hungryDays >= 3) {
                    System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " died of hunger.");
                    synchronized (farm) {
                        farm.killAnimal(this);
                    }
                    Thread.currentThread().interrupt();
                } else if (thirstyDays >= 3) {
                    System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " died of thirst.");
                    synchronized (farm) {
                        farm.killAnimal(this);
                    }
                    Thread.currentThread().interrupt();
                } else if (sickDays >= 3) {
                    System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " died of sickness.");
                    synchronized (farm) {
                        farm.killAnimal(this);
                    }
                    Thread.currentThread().interrupt();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(this.getClass().getSimpleName() + " with ID: " + this.id + " died.");
    }
}