package org.example.models;

import java.util.concurrent.Semaphore;

public class Hospital {
    private final Semaphore capacity;

    public Hospital(int capacity) {
        this.capacity = new Semaphore(capacity, true);
    }

    public void admit(Animal animal) throws InterruptedException {
        capacity.acquire();
        try {
            System.out.println(animal.getClass().getSimpleName() + " is being treated.");
            Thread.sleep(2000);
            Farm.getInstance().heal(animal);
        } finally {
            System.out.println(animal.getClass().getSimpleName() + " has been treated and is leaving the hospital.");
            capacity.release();
        }
    }
}
