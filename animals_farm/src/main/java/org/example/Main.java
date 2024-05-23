package org.example;

import org.example.models.Cow;
import org.example.models.Pig;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Create animal instances
        Pig pig = new Pig();
        Cow cow = new Cow();

        Thread cowThread = new Thread(cow);
        Thread pigThread = new Thread(pig);
        // Create threads for each animal

        // Start animal threads
        pigThread.start();
        cowThread.start();

        // Create and start the global clock thread
        GlobalClock globalClock = new GlobalClock(1);
        Thread clockThread = new Thread(globalClock);
        //clockThread.setDaemon(true); // Optional: makes it a daemon thread
        clockThread.start();

        // Join threads to keep the main thread alive
        try {
            pigThread.join();
            cowThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}