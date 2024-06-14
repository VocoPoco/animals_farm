package org.example.models;

import org.example.enums.Item;
import org.example.enums.OtherType;
import org.example.enums.ProductionType;

import java.util.HashMap;

public class Inventory {
    private HashMap<Item, Integer> inventory;
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
            } else {
                inventory.put(type, 100);
            }
        }
    }
    public static Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    public void addItem(Item item, int quantity) {
        if (quantity <= 0) {
            System.out.println("ERROR: Quantity to add must be positive.");
        }
        inventory.put(item, inventory.getOrDefault(item,0) + quantity);
    }

    public void removeItem(Item item, int quantity) {
        if (!inventory.containsKey(item)) {
            System.out.println("ERROR: No such item in inventory. ");
            return;
        }
        if (quantity <= 0) {
            System.out.println("ERROR: Quantity to remove must be positive. Provided: " + quantity);
            return;
        }
        int currentQuantity = inventory.get(item);
        if (currentQuantity <= quantity) {
            inventory.remove(item);
        } else {
            inventory.put(item, currentQuantity - quantity);
        }
    }

    public int getItem(Item item) {
        if (!inventory.containsKey(item)) {
            System.out.println("ERROR: No such item in inventory. ");
            return 0;
        }
        return inventory.getOrDefault(item, 0);
    }

    public void printInventory() {
        for (Item item : inventory.keySet()) {
            System.out.println(item + ": " + inventory.get(item));
        }
    }
}
