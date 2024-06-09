package org.example.models;

import org.example.models.animals.Animal;

import java.util.LinkedList;
import java.util.Queue;

public class Hospital {
    private final int capacity;
    private final Queue<Animal> waitingList;

    public Hospital(int capacity) {
        this.capacity = capacity;
        this.waitingList = new LinkedList<>();
    }

    public synchronized void admitAnimal(Animal animal) {
        while (waitingList.size() >= capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        waitingList.add(animal);
        notifyAll();
    }

    public synchronized void treatAnimals() {
        int treatedCount = 0;
        while (!waitingList.isEmpty() && treatedCount < capacity) {
            Animal animal = waitingList.poll();
            animal.setIsSick(false);
            treatedCount++;
        }
        notifyAll();
    }

    public int getWaitingListSize() {
        return waitingList.size();
    }
}