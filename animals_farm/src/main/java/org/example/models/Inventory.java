package org.example.models;

import java.util.HashMap;

public class Inventory {
    public HashMap<String, Integer> inventory;

    public Inventory() {
        inventory = new HashMap<>();
        inventory.put("pig_food", 10);
        inventory.put("cow_food", 10);
        inventory.put("hen_food", 10);
        inventory.put("goat_food", 10);
        inventory.put("sheep_food", 10);
        inventory.put("duck_food", 10);
        inventory.put("horse_food", 10);
        inventory.put("water", 10);
        inventory.put("pills", 10);
    }

    public void addItem(String item, int quantity) {
        inventory.put(item, inventory.getOrDefault(item, 0) + quantity);
    }

    public void removeItem(String item, int quantity) {
        if (inventory.containsKey(item)) {
            int currentQuantity = inventory.get(item);
            if (currentQuantity <= quantity) {
                inventory.remove(item);
            } else {
                inventory.put(item, currentQuantity - quantity);
            }
        }
    }

    public int getItemQuantity(String item) {
        return inventory.getOrDefault(item, 0);
    }

    public void printInventory() {
        for (String item : inventory.keySet()) {
            System.out.println(item + ": " + inventory.get(item));
        }
    }
}
