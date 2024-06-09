package org.example.models;

import org.example.enums.AnimalState;
import org.example.enums.OtherType;
import org.example.enums.ProductionType;
import org.example.models.animals.Animal;

import java.util.ArrayList;
import java.util.List;

public class Farm {
    private static Farm instance;
    private final ArrayList<Animal> animals;
    private final Inventory inventory;
    private final Hospital hospital;
    private final List<String> eventLog;

    private Farm() {
        this.animals = new ArrayList<>();
        this.inventory = Inventory.getInstance();
        this.hospital = new Hospital(3);
        this.eventLog = new ArrayList<>();
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
    }

    public void feed(Animal animal) {
        if (animal.getState() == AnimalState.FULL || animal.getState() == AnimalState.FED_THIRSTY) {
            throw new RuntimeException("ERROR: Animal is not hungry.");
        }
        if (animal.getFoodConsumption() > inventory.getItem(animal.getFoodType())) {
            throw new RuntimeException("ERROR: Not enough food.");
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
            throw new RuntimeException("ERROR: Animal is not thirsty.");
        }
        if (animal.getWaterConsumption() > inventory.getItem(OtherType.WATER)) {
            throw new RuntimeException("ERROR: Not enough water.");
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
            throw new RuntimeException("ERROR: Animal is not sick");
        }
        if (animal.getMedicineConsumption() > inventory.getItem(OtherType.MEDICINE)) {
            throw new RuntimeException("ERROR: Not enough medicine.");
        }
        inventory.removeItem(OtherType.MEDICINE, animal.getMedicineConsumption());
        animal.setIsSick(false);
    }

    public void buy(OtherType item, int quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("ERROR: You must buy a positive amount of " + item);
        }
        if (item.getPrice() * quantity > inventory.getItem(OtherType.MONEY)) {
            throw new RuntimeException("ERROR: Not enough money.");
        }
        inventory.removeItem(OtherType.MONEY, item.getPrice() * quantity);
        inventory.addItem(item, quantity);
    }

    public void sell(ProductionType production, int quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("ERROR: You must sell a positive amount of " + production);
        }
        if (inventory.getItem(production) < quantity) {
            throw new RuntimeException("ERROR: Not enough of this production.");
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
            summary.add("Warning: Low on food!");
        }
        if (inventory.getItem(OtherType.WATER) < 10) {
            summary.add("Warning: Low on water!");
        }
        if (inventory.getItem(OtherType.MEDICINE) < 5) {
            summary.add("Warning: Low on medicine!");
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