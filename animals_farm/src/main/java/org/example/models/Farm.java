package org.example.models;

import org.example.enums.AnimalState;

import java.util.ArrayList;

public class Farm {
    public ArrayList<Animal> animals;

    public Inventory inventory;
    public int coins;

    public void feed(Animal animal)  {
        //nqkvi proverki oshte
        if(animal.getState() == AnimalState.FULL && animal.getState() == AnimalState.FED_THIRSTY) {
            throw new RuntimeException("ERROR: Animal is not hungry.");
        }
        if(animal.getFoodConsumption() > inventory.getItemQuantity(animal.getFoodType())) {
            throw new RuntimeException("ERROR: Not enough food.");
        }

        this.inventory.removeItem(animal.getFoodType(), animal.getFoodConsumption());
        if(animal.getFoodConsumption() > inventory.getItemQuantity(animal.getFoodType())) {
            //moje i da se iznese pri samata igra no trqbva da suobshtavame
            // na potrebitelq che shte trqbva da kupuva hrana
        }
        if(animal.getState() == AnimalState.HUNGRY_THIRSTY) {
            animal.setState(AnimalState.FED_THIRSTY);
        } else {
            animal.setState(AnimalState.FULL);
        }
    }
}
