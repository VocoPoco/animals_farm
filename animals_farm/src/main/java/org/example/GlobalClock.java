package org.example;

import org.example.enums.SeasonType;

public class GlobalClock implements Runnable {
    private final Object monitor = new Object();
    private int day;
    private int week;
    private int month;
    private int year;
    private SeasonType season;
    private final double speed;
    private static GlobalClock instance;

    private static final int[] DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private GlobalClock(double speed) {
        this.speed = speed;
        this.day = 1;
        this.week = 1;
        this.month = 1;
        this.year = 0;
        this.season = SeasonType.SPRING;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int speedTime = (int) (1000 / speed);
                Thread.sleep(speedTime);
                addDay();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void addDay() {
        synchronized (monitor) {
            if (day < getDaysInMonth()) {
                this.day++;
            } else {
                addMonth();
                this.day = 1;
            }
            if (day % 7 == 1) {
                addWeek();
            }
            monitor.notifyAll();
        }
    }

    private int getDaysInMonth() {
        if (month == 2 && isLeapYear()) {
            return 29;
        }
        return DAYS_IN_MONTH[month - 1];
    }

    private boolean isLeapYear() {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public int getDay() {
        return day;
    }

    public int getWeek() {
        return week;
    }

    private void addWeek() {
        week++;
    }

    public int getMonth() {
        return month;
    }

    private void addMonth() {
        if (this.month < 12) {
            this.month++;
        } else {
            addYear();
            this.month = 1;
        }
        updateSeason();
    }

    public int getYear() {
        return year;
    }

    private void addYear() {
        year++;
    }

    public SeasonType getSeason() {
        return season;
    }

    private void updateSeason() {
        if (month >= 3 && month <= 5) {
            season = SeasonType.SPRING;
        } else if (month >= 6 && month <= 8) {
            season = SeasonType.SUMMER;
        } else if (month >= 9 && month <= 11) {
            season = SeasonType.AUTUMN;
        } else {
            season = SeasonType.WINTER;
        }
    }

    public static GlobalClock getInstance() {
        if (instance == null) {
            instance = new GlobalClock(0.2);  // Example speed
        }
        return instance;
    }

    public Object getMonitor() {
        return monitor;
    }
}
