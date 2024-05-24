package org.example;

import org.example.enums.SeasonType;

import java.time.Month;

@SuppressWarnings("ALL")
public class GlobalClock implements Runnable {
    private int second;
    private int minute;
    private int hour;
    private int day;
    private int week;
    private int month;
    private int year;
    private SeasonType season;
    private final int speed;

    private static final int[] DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public GlobalClock(int speed) {
        this.speed = speed;
        this.second = 0;
        this.minute = 0;
        this.hour = 0;
        this.day = 1;
        this.week = 1;
        this.month = 1;
        this.year = 0;
        this.season = SeasonType.WINTER;
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int speedTime =  1000 / speed;
                Thread.sleep(speedTime);
                addSecond();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getSecond() {
        return second;
    }

    private void addSecond() {
        if (this.second < 59) {
            second++;
        } else {
            addMinute();
            this.second = 0;
        }
    }

    private int getMinute() {
        return minute;
    }
    private void addMinute() {
        if (this.minute < 59) {
            minute++;
        } else {
            addHour();
            this.minute = 0;
        }
    }

    public int getHour() {
        return hour;
    }

    private void addHour() {
        if (hour < 23) {
            hour++;
        } else {
            addDay();
            hour = 0;
        }
    }

    private void addDay() {
        if (day < getDaysInMonth()) {
            this.day++;
        } else {
            addMonth();
            this.day = 1;
        }
        if (day % 7 == 1) {
            addWeek();
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
}
