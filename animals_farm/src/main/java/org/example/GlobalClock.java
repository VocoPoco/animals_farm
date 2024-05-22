package org.example;

@SuppressWarnings("ALL")
public class GlobalClock implements Runnable {
    private int second;
    private int minute;
    private int hour;
    private final int speed;

    public GlobalClock(int speed) {
        this.speed = speed;
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
        if (this.second < 60) {
            second++;
        } else {
            addMinute();
            this.second = 0;
        }
    }
    private void addMinute() {
        if (this.minute < 60) {
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
        if (hour < 24) {
            hour++;
        } else {
            System.out.println("A Day Has Passed!");
            hour =0;
        }
    }
}
