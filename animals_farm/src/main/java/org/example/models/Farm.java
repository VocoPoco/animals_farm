package org.example.models;

import org.example.enums.AnimalState;
import org.example.enums.OtherType;
import org.example.enums.ProductionType;
import org.example.models.animals.Animal;

import java.util.ArrayList;

public class Farm {
    private static Farm instance;
    private final ArrayList<Animal> animals;
    private final Inventory inventory;
    private final Hospital hospital;

    private Farm() {
        this.animals = new ArrayList<>();
        this.inventory = Inventory.getInstance();
        this.hospital = new Hospital(3);
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

    public void feed(Animal animal)  {
        if(animal.getState() == AnimalState.FULL || animal.getState() == AnimalState.FED_THIRSTY) {
            throw new RuntimeException("ERROR: Animal is not hungry.");
        }
        if(animal.getFoodConsumption() > inventory.getItem(animal.getFoodType())) {
            throw new RuntimeException("ERROR: Not enough food.");
        }
        this.inventory.removeItem(animal.getFoodType(), animal.getFoodConsumption());
        if(animal.getState() == AnimalState.HUNGRY_THIRSTY) {
            animal.setState(AnimalState.FED_THIRSTY);
        } else {
            animal.setState(AnimalState.FULL);
        }
    }

    public void giveWater(Animal animal) {
        if(animal.getState() == AnimalState.FULL || animal.getState() == AnimalState.DRENCHED_HUNGRY) {
            System.out.println("ERROR: Animal is not thirsty.");
            return;
        }
        if(animal.getWaterConsumption() > inventory.getItem(OtherType.WATER)) {
            System.out.println("ERROR: Not enough water.");
            return;
        }
        inventory.removeItem(OtherType.WATER, animal.getWaterConsumption());
        if(animal.getState() == AnimalState.HUNGRY_THIRSTY) {
            animal.setState(AnimalState.DRENCHED_HUNGRY);
        } else {
            animal.setState(AnimalState.FULL);
        }
    }

    public void heal(Animal animal) {
        if(!animal.getIsSick()) {
            System.out.println("ERROR: Animal is not sick");
            return;
        }
        if(animal.getMedicineConsumption() > inventory.getItem(OtherType.MEDICINE)) {
            System.out.println("ERROR: Not enough medicine. The animal may die soon.");
            return;
        }
        inventory.removeItem(OtherType.MEDICINE, animal.getMedicineConsumption());
        animal.setIsSick(false);
    }

    public void buy(OtherType item, int quantity) {
        if (quantity <= 0) {
            System.out.println("ERROR: You must buy a positive amount of " + item);
            return;
        }
        if (item.getPrice() > inventory.getItem(OtherType.MONEY)) {
            System.out.println("ERROR: Not enough money. Maybe you want to sell some production.");
            return;
        }
        inventory.removeItem(OtherType.MONEY, item.getPrice());
        inventory.addItem(item, quantity);
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
        inventory.addItem(OtherType.MONEY, production.getPrice());
    }

    public void killAnimal(Animal animal) {
        animals.remove(animal);
    }

    public void bornAnimal(Animal animal) {
        animals.add(animal);
    }
}
