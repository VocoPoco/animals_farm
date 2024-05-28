package org.example.models;

import org.example.enums.AnimalState;
import org.example.enums.OtherType;
import org.example.enums.ProductionType;

import java.util.ArrayList;

public class Farm {
    public ArrayList<Animal> animals;
    public Inventory inventory;

    public Farm() {
        this.animals = new ArrayList<>();
        this.inventory = Inventory.getInstance();
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
            throw new RuntimeException("ERROR: Animal is not thirsty.");
        }
        if(animal.getWaterConsumption() > inventory.getItem(OtherType.WATER)) {
            throw new RuntimeException("ERROR: Not enough water.");
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
            throw new RuntimeException("ERROR: Animal is not sick");
        }
        if(animal.getMedicineConsumption() > inventory.getItem(OtherType.MEDICINE)) {
            throw new RuntimeException("ERROR: Not enough medicine. The animal may die soon.");
        }
        inventory.removeItem(OtherType.MEDICINE, animal.getMedicineConsumption());
        animal.setIsSick(false);
    }

    public void buy(OtherType item, int quantity) {
        if(item.getPrice() > inventory.getItem(OtherType.MONEY)) {
            throw new RuntimeException("ERROR: Not enough money. Maybe you want to sell some production.");
        }
        inventory.removeItem(OtherType.MONEY, item.getPrice());
        inventory.addItem(item, quantity);
    }

    public void sell(ProductionType production, int quantity) {
        if(inventory.getItem(production) < quantity) {
            throw new RuntimeException("ERROR: Not enough of this production.");
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
