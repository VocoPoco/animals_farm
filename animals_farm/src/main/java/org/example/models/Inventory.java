package org.example.models;

import org.example.enums.Item;
import org.example.enums.OtherType;
import org.example.enums.ProductionType;

import java.util.HashMap;

public class Inventory {
    public HashMap<Item, Integer> inventory;
    private static Inventory instance;

    private Inventory() {
        inventory = new HashMap<>();
        initInventory();
    }

    private void initInventory() {
        for (var type : ProductionType.values()) {
            inventory.put(type, 10);
        }
        for (var type : OtherType.values()) {
            if (type.equals(OtherType.MONEY)) {
                inventory.put(type, 1000);
            }
            inventory.put(type, 10);
        }
    }
    public static Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    public void addItem(Item item, int quantity) {
        inventory.put(item, inventory.getOrDefault(item,0) + quantity);
    }

    public void removeItem(Item item, int quantity) {
        if (inventory.containsKey(item)) {
            int currentQuantity = inventory.get(item);
            if (currentQuantity <= quantity) {
                inventory.remove(item);
            } else {
                inventory.put(item, currentQuantity - quantity);
            }
        }
    }

    public int getItem(Item item) {
        return inventory.getOrDefault(item, 0);
    }

    public void printInventory() {
        for (Item item : inventory.keySet()) {
            System.out.println(item + ": " + inventory.get(item));
        }
    }
}
