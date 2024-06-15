package org.example;

import org.example.enums.ProductionType;
import org.example.models.Farm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main{
    private static Farm farm = Farm.getInstance();
    private static GlobalClock clock = GlobalClock.getInstance();
    private static Thread clockThread;

    public static void main(String[] args) {
        clockThread = new Thread(clock);
        clockThread.start();
        System.out.println("Clock started");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String command;

        while (true) {
            try {
                if (reader.ready()) {
                    command = reader.readLine().trim();
                    processPlayerInput(command);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void endGame() {
        clockThread.interrupt();
        System.out.println("Game ended.");
        System.exit(0);
    }

    private static void processPlayerInput(String input) {
        String[] parts = input.split(" ");
        if (parts.length < 1) {
            System.out.println("Invalid command");
            return;
        }

        String command = parts[0].toUpperCase();
        String item = parts.length > 1 ? parts[1].toUpperCase() : "";
        int quantity = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

        switch (command) {
            case "BUY":
                if (!item.isEmpty() && quantity > 0) {
                    try {
                        farm.buy(item, quantity);
                        System.out.println("Bought " + quantity + " " + item);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid BUY command. Usage: BUY <item> <quantity>");
                }
                break;
            case "SELL":
                if (!item.isEmpty() && quantity > 0) {
                    try {
                        farm.sell(ProductionType.valueOf(item), quantity);
                        System.out.println("Sold " + quantity + " " + item);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid SELL command. Usage: SELL <item> <quantity>");
                }
                break;
            case "END":
                endGame();
                break;
            case "INVENTORY":
                farm.getInventory().printInventory();
                break;
            case "FARM":
                farm.printFarm();
                break;
            default:
                System.out.println("Invalid command. Valid commands are: BUY, SELL, END, INVENTORY, FARM");
                break;
        }
    }
}
