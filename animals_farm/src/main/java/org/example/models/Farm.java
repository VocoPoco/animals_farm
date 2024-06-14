package org.example.models;

import org.example.enums.OtherType;
import org.example.enums.ProductionType;
import org.example.models.animals.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Farm {
    private static Farm instance;
    private ArrayList<Animal> animals;
    private final Inventory inventory;
    private final Hospital hospital;
    private final List<String> eventLog;
    private static final HashMap<String, Integer> animalCounts = new HashMap<>();

    private static final HashMap<String, Integer> animalPrices = new HashMap<>();

    static {
        animalPrices.put("COW", 100);
        animalPrices.put("DUCK", 20);
        animalPrices.put("GOAT", 50);
        animalPrices.put("HEN", 15);
        animalPrices.put("HORSE", 150);
        animalPrices.put("PIG", 70);
        animalPrices.put("SHEEP", 60);

        animalCounts.put("COW", 0);
        animalCounts.put("DUCK", 0);
        animalCounts.put("GOAT", 0);
        animalCounts.put("HEN", 0);
        animalCounts.put("HORSE", 0);
        animalCounts.put("PIG", 0);
        animalCounts.put("SHEEP", 0);
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
                new Cow(0),
                new Duck(0),
                new Hen(0),
                new Horse(0),
                new Pig(0),
                new Goat(0),
                new Sheep(0)
        ));

        for (Animal animal : animals) {
            this.animals.add(animal);
            incrementAnimalCount(animal);
            new Thread(animal).start();
        }
    }

    private void incrementAnimalCount(Animal animal) {
        String animalType = animal.getClass().getSimpleName().toUpperCase();
        animalCounts.put(animalType, animalCounts.get(animalType) + 1);
    }

    private void decrementAnimalCount(Animal animal) {
        String animalType = animal.getClass().getSimpleName().toUpperCase();
        animalCounts.put(animalType, animalCounts.get(animalType) - 1);
    }

    public void printFarm() {
        for (String animalType : animalCounts.keySet()) {
            System.out.println("Animal type: " + animalType + ", Count: " + animalCounts.get(animalType));
        }
        System.out.println("\n");
        for(Animal animal : animals) {
            System.out.println(animal.getAnimalInfo());
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
        incrementAnimalCount(animal);
        new Thread(animal).start();
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    public void feed(Animal animal) {
        if (!animal.getIsHungry()) {
            System.out.println("ERROR: Animal is not hungry.");
            return;
        }
        if (animal.getFoodConsumption() > inventory.getItem(animal.getFoodType())) {
            System.out.println("ERROR: Not enough food.");
            return;
        }
        this.inventory.removeItem(OtherType.FOOD, animal.getFoodConsumption());
        animal.setIsHungry(false);
    }

    public void giveWater(Animal animal) {
        if (!animal.getIsThirsty()) {
            System.out.println("ERROR: Animal is not thirsty.");
            return;
        }
        if (animal.getWaterConsumption() > inventory.getItem(OtherType.WATER)) {
            System.out.println("ERROR: Not enough water.");
            return;
        }
        inventory.removeItem(OtherType.WATER, animal.getWaterConsumption());
        animal.setIsThirsty(false);
    }

    public void putAnimalInHospital(Animal animal) {
        if (!animal.getIsSick()) {
            System.out.println("ERROR: Animal is not sick.");
            return;
        }
        if (animal.getMedicineConsumption() > inventory.getItem(OtherType.MEDICINE)) {
            System.out.println("ERROR: Not enough medicine.");
            return;
        }
        inventory.removeItem(OtherType.MEDICINE, animal.getMedicineConsumption());
        hospital.admitAnimal(animal);
    }

    private int generateID() {
        return new Random().nextInt(10);
    }

    public void buy(String item, int quantity) {
        if (quantity <= 0) {
            System.out.println("ERROR: You must buy a positive amount of " + item);
            return;
        }

        item = item.toUpperCase();
        int totalCost = 0;

        if (animalPrices.containsKey(item)) {
            int price = animalPrices.get(item);
            totalCost = price * quantity;

            if (totalCost > inventory.getItem(OtherType.MONEY)) {
                System.out.println("ERROR: Not enough money.");
                return;
            }

            inventory.removeItem(OtherType.MONEY, totalCost);

            for (int i = 0; i < quantity; i++) {
                int id;
                do {
                    id = generateID();
                } while (!checkIfIdIsUnique(item, id));

                Animal animal = switch (item) {
                    case "COW" -> new Cow(id);
                    case "DUCK" -> new Duck(id);
                    case "GOAT" -> new Goat(id);
                    case "HEN" -> new Hen(id);
                    case "HORSE" -> new Horse(id);
                    case "PIG" -> new Pig(id);
                    case "SHEEP" -> new Sheep(id);
                    default -> throw new IllegalStateException("Unexpected value: " + item);
                };
                addAnimal(animal);
            }
        } else {
            OtherType otherType;
            switch (item) {
                case "FOOD" -> otherType = OtherType.FOOD;
                case "WATER" -> otherType = OtherType.WATER;
                case "MEDICINE" -> otherType = OtherType.MEDICINE;
                default -> {
                    System.out.println("ERROR: Invalid item type.");
                    return;
                }
            }

            totalCost = otherType.getPrice() * quantity;

            if (totalCost > inventory.getItem(OtherType.MONEY)) {
                System.out.println("ERROR: Not enough money.");
                return;
            }

            inventory.removeItem(OtherType.MONEY, totalCost);
            inventory.addItem(otherType, quantity);
        }
    }

    public boolean checkIfIdIsUnique(String animalType, int id) {
        for(Animal animal : animals) {
            if(animal.getClass().getSimpleName() == animalType) {
                if(animal.getId() == id) {
                    return false;
                }
            }
        }
        return true;
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
        decrementAnimalCount(animal);
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
            if (animal.getIsHungry()) {
                hungryAnimals++;
            }
            if (animal.getIsThirsty()) {
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