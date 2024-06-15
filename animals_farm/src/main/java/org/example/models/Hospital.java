package org.example.models;

import org.example.models.animals.Animal;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Hospital {
    private static Hospital instance;
    private final int capacity;
    private final Queue<Animal> waitingList;
    private final Semaphore semaphore;

    public Hospital(int capacity) {
        this.capacity = capacity;
        this.waitingList = new LinkedList<>();
        this.semaphore = new Semaphore(capacity, true); // Semaphore to manage the capacity
    }

    public static Hospital getInstance() {
        if (instance == null) {
            instance = new Hospital(1);
        }
        return instance;
    }

    public void admitAnimal(Animal animal) {
        try {
            semaphore.acquire();
            synchronized (this) {
                waitingList.add(animal);
            }
            System.out.println("Animal admitted in hospital.");
            heal(animal);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void heal(Animal animal) {
        synchronized (this) {
            animal.setIsSick(false);
            waitingList.remove(animal);
            semaphore.release();
            System.out.println("The healing process ended.");
        }
    }

    public int getWaitingListSize() {
        synchronized (this) {
            return waitingList.size();
        }
    }
}