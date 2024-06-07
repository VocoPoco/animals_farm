package org.example.models;

import org.example.models.animals.Animal;

import java.util.concurrent.Semaphore;

public class Hospital {
    private static Hospital instance;
    private final Semaphore capacity;

    public Hospital() {
        this.capacity = new Semaphore(1, true);
    }
    public Hospital(int capacity) {
        this.capacity = new Semaphore(capacity, true);
    }

    public static Hospital getInstance() {
        if (instance == null) {
            instance = new Hospital();
        }
        return instance;
    }

    public void admit(Animal animal) throws InterruptedException {
        capacity.acquire();
        try {
            System.out.println(animal.getClass().getSimpleName() + " is being treated.");
            Thread.sleep(2000);
            Farm.getInstance().heal(animal);
            System.out.println(animal.getClass().getSimpleName() + " has been treated and is leaving the hospital.");
        } catch (Exception e) {
            System.out.println("ERROR: Could not treat animal. ");
        } finally {
            capacity.release();
        }
    }
}
