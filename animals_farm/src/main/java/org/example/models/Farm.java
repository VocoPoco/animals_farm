package org.example.models;

import org.example.enums.AnimalState;
import org.example.enums.OtherType;
import org.example.enums.ProductionType;
import org.example.models.animals.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Farm {
    private static Farm instance;
    private ArrayList<Animal> animals;
    private final Inventory inventory;
    private final Hospital hospital;
    private final List<String> eventLog;

    private static final HashMap<String, Integer> animalPrices = new HashMap<>();

    static {
        animalPrices.put("COW", 100);
        animalPrices.put("DUCK", 20);
        animalPrices.put("GOAT", 50);
        animalPrices.put("HEN", 15);
        animalPrices.put("HORSE", 150);
        animalPrices.put("PIG", 70);
        animalPrices.put("SHEEP", 60);
    }
    private Farm() {
        this.animals = new ArrayList<>();
        initFarm();
        this.inventory = Inventory.getInstance();
        this.hospital = new Hospital(3);
        this.eventLog = new ArrayList<>();
    }

    private void initFarm() {
        List<Animal> animals = new ArrayList<>(List.of(
                new Cow(),
                new Duck(),
                new Hen(),
                new Horse(),
                new Pig(),
                new Sheep()
        ));

        for (var animal : animals) {
            this.animals.add(animal);
            new Thread(animal).start();
        }
    }

    public static Farm getInstance() {
        if (instance == null) {
            instance = new Farm();
        }
        return instance;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        new Thread(animal).start();
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    public void feed(Animal animal) {
        if (animal.getState() == AnimalState.FULL || animal.getState() == AnimalState.FED_THIRSTY) {
            System.out.println("ERROR: Animal is not hungry.");
            return;
        }
        if (animal.getFoodConsumption() > inventory.getItem(animal.getFoodType())) {
            System.out.println("ERROR: Not enough food.");
            return;
        }
        this.inventory.removeItem(animal.getFoodType(), animal.getFoodConsumption());
        if (animal.getState() == AnimalState.HUNGRY_THIRSTY) {
            animal.setState(AnimalState.FED_THIRSTY);
        } else {
            animal.setState(AnimalState.FULL);
        }
    }

    public void giveWater(Animal animal) {
        if (animal.getState() == AnimalState.FULL || animal.getState() == AnimalState.DRENCHED_HUNGRY) {
            System.out.println("ERROR: Animal is not thirsty.");
            return;
        }
        if (animal.getWaterConsumption() > inventory.getItem(OtherType.WATER)) {
            System.out.println("ERROR: Not enough water.");
            return;
        }
        inventory.removeItem(OtherType.WATER, animal.getWaterConsumption());
        if (animal.getState() == AnimalState.HUNGRY_THIRSTY) {
            animal.setState(AnimalState.DRENCHED_HUNGRY);
        } else {
            animal.setState(AnimalState.FULL);
        }
    }

    public void heal(Animal animal) {
        if (!animal.getIsSick()) {
            System.out.println("ERROR: Animal is not sick.");
            return;
        }
        if (animal.getMedicineConsumption() > inventory.getItem(OtherType.MEDICINE)) {
            System.out.println("ERROR: Not enough medicine.");
            return;
        }
        inventory.removeItem(OtherType.MEDICINE, animal.getMedicineConsumption());
        animal.setIsSick(false);
    }

    public void buy(String animalType, int quantity) {
        if (quantity <= 0) {
            System.out.println("ERROR: You must buy a positive amount of " + animalType);
            return;
        }

        animalType = animalType.toUpperCase();
        if (!animalPrices.containsKey(animalType)) {
            System.out.println("ERROR: Invalid animal type.");
            return;
        }

        int price = animalPrices.get(animalType);
        int totalCost = price * quantity;

        if (totalCost > inventory.getItem(OtherType.MONEY)) {
            System.out.println("ERROR: Not enough money.");
            return;
        }

        inventory.removeItem(OtherType.MONEY, totalCost);

        for (int i = 0; i < quantity; i++) {
            System.out.println("WOO hey");
            Animal animal = switch (animalType) {
                case "COW" -> new Cow();
                case "DUCK" -> new Duck();
                case "GOAT" -> new Goat();
                case "HEN" -> new Hen();
                case "HORSE" -> new Horse();
                case "PIG" -> new Pig();
                case "SHEEP" -> new Sheep();
                default -> throw new IllegalStateException("Unexpected value: " + animalType);
            };
            addAnimal(animal);
        }
    }

    public void sell(ProductionType production, int quantity) {
        if (quantity <= 0) {
            System.out.println("ERROR: You must sell a positive amount of " + production);
            return;
        }
        if (inventory.getItem(production) < quantity) {
            System.out.println("ERROR: Not enough of this production.");
            return;
        }
        inventory.removeItem(production, quantity);
        inventory.addItem(OtherType.MONEY, production.getPrice() * quantity);
    }

    public void killAnimal(Animal animal) {
        animals.remove(animal);
        logEvent(animal.getClass().getSimpleName() + " has died.");
    }

    public void logEvent(String event) {
        eventLog.add(event);
    }

    public List<String> getDailySummary() {
        List<String> summary = new ArrayList<>();
        int hungryAnimals = 0;
        int thirstyAnimals = 0;
        int sickAnimals = 0;
        int deadAnimals = 0;
        int waitingForHospital = hospital.getWaitingListSize();

        for (Animal animal : animals) {
            if (animal.getState() == AnimalState.HUNGRY_THIRSTY || animal.getState() == AnimalState.DRENCHED_HUNGRY) {
                hungryAnimals++;
            }
            if (animal.getState() == AnimalState.FED_THIRSTY || animal.getState() == AnimalState.HUNGRY_THIRSTY) {
                thirstyAnimals++;
            }
            if (animal.getIsSick()) {
                sickAnimals++;
            }
        }

        for (String event : eventLog) {
            if (event.contains("has died")) {
                deadAnimals++;
            }
        }

        if (inventory.getItem(OtherType.FOOD) < 10) {
            summary.add("WARNING: Low on food!");
        }
        if (inventory.getItem(OtherType.WATER) < 10) {
            summary.add("WARNING: Low on water!");
        }
        if (inventory.getItem(OtherType.MEDICINE) < 5) {
            summary.add("WARNING: Low on medicine!");
        }

        summary.add("Hungry animals: " + hungryAnimals);
        summary.add("Thirsty animals: " + thirstyAnimals);
        summary.add("Sick animals: " + sickAnimals);
        summary.add("Animals waiting for hospital: " + waitingForHospital);
        summary.add("Animals died today: " + deadAnimals);

        return summary;
    }

    public void resetDailyLog() {
        eventLog.clear();
    }
}
