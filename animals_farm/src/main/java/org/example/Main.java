package org.example;

import org.example.enums.ProductionType;
import org.example.models.Hospital;
import org.example.models.animals.*;
import org.example.models.Farm;
import org.example.models.Inventory;

public class Main {
    public static void main(String[] args) {
        Farm farm = Farm.getInstance();
        Inventory inventory = farm.getInventory();
        System.out.println("Starting with your inventory:");
        inventory.printInventory();
        Hospital hospital = farm.getHospital();
        
        GlobalClock globalClock = GlobalClock.getInstance();
        Thread globalClockThread = new Thread(globalClock);
        globalClockThread.start();

        Cow cow = new Cow();
        Duck duck = new Duck();
        Goat goat = new Goat();
        Hen hen = new Hen();
        Horse horse = new Horse();
        Pig pig = new Pig();
        Sheep sheep = new Sheep();
        
        Thread cowThread = new Thread(cow);
        Thread duckThread = new Thread(duck);
        Thread goatThread = new Thread(goat);
        Thread henThread = new Thread(hen);
        Thread horseThread = new Thread(horse);
        Thread pigThread = new Thread(pig);
        Thread sheepThread = new Thread(sheep);

        cowThread.start();
        duckThread.start();
        goatThread.start();
        henThread.start();
        horseThread.start();
        pigThread.start();
        sheepThread.start();
    }
}