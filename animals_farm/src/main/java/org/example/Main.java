package org.example;

import org.example.EndOfDayListener;
import org.example.enums.OtherType;
import org.example.enums.ProductionType;
import org.example.models.Farm;
import org.example.models.animals.*;

import java.util.List;
import java.util.Scanner;

public class Main implements EndOfDayListener {
    private static Farm farm = Farm.getInstance();

    public static void main(String[] args) {
        Cow cow = new Cow();
        Duck duck = new Duck();
        Goat goat = new Goat();
        Hen hen = new Hen();
        Horse horse = new Horse();
        Pig pig = new Pig();
        Sheep sheep = new Sheep();
        farm.addAnimal(cow);
        farm.addAnimal(duck);
        farm.addAnimal(goat);
        farm.addAnimal(hen);
        farm.addAnimal(horse);
        farm.addAnimal(pig);
        farm.addAnimal(sheep);

        new Thread(cow).start();
        new Thread(duck).start();
        new Thread(goat).start();
        new Thread(hen).start();
        new Thread(horse).start();
        new Thread(pig).start();
        new Thread(sheep).start();

        GlobalClock clock = GlobalClock.getInstance();
        clock.addEndOfDayListener(new Main());
        Thread clockThread = new Thread(clock);
        clockThread.start();

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                processPlayerInput(input);
            }
        }).start();
    }

    private static void processPlayerInput(String input) {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            System.out.println("Invalid command");
            return;
        }

        String command = parts[0];
        String item = parts[1];
        int quantity = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

        switch (command) {
            case "buy":
                try {
                    farm.buy(OtherType.valueOf(item.toUpperCase()), quantity);
                    System.out.println("Bought " + quantity + " " + item);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            case "sell":
                try {
                    farm.sell(ProductionType.valueOf(item.toUpperCase()), quantity);
                    System.out.println("Sold " + quantity + " " + item);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            default:
                System.out.println("Invalid command");
                break;
        }
    }

    @Override
    public void onEndOfDay() {
        printEndOfDaySummary();
    }

    private static void printEndOfDaySummary() {
        List<String> summary = farm.getDailySummary();
        System.out.println("End of Day Summary:");
        for (String line : summary) {
            System.out.println(line);
        }
        farm.resetDailyLog();
    }
}