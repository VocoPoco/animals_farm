package org.example;

import org.example.enums.SeasonType;
import org.example.models.*;

public class GamePlayer implements Runnable {
    private final GlobalClock globalClock;
    private final Farm farm;
    private final Thread globalClockThread;

    public GamePlayer(Farm farm) {
        this.globalClock = GlobalClock.getInstance();
        this.farm = farm;
        this.globalClockThread = new Thread(globalClock);
    }

    @Override
    public void run() {
        globalClockThread.start();
        for (Animal animal : farm.animals) {
            Thread animalThread = new Thread(animal);
            animalThread.start();
        }
        while (true) {
            try {
                Thread.sleep(1000);
                printCurrentTimeAndDate();
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void printCurrentTimeAndDate() {
        int second = globalClock.getSecond();
        int minute = globalClock.getMinute();
        int hour = globalClock.getHour();
        int day = globalClock.getDay();
        int month = globalClock.getMonth();
        int year = globalClock.getYear();
        SeasonType season = globalClock.getSeason();

        System.out.printf("Current time: %02d:%02d:%02d, Date: %04d-%02d-%02d, Season: %s%n",
                hour, minute, second, year, month, day, season);
    }

    public static void main(String[] args) {
        Pig pig = new Pig();
        Cow cow = new Cow();
        Sheep sheep = new Sheep();

        Farm farm = new Farm();
        farm.animals.add(pig);
        farm.animals.add(cow);
        farm.animals.add(sheep);

        GamePlayer gamePlayer = new GamePlayer(farm);
        Thread gameThread = new Thread(gamePlayer);
        gameThread.start();
    }
}
