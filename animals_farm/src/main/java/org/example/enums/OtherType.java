package org.example.enums;

public enum OtherType implements Item {
    MEDICINE(10),
    WATER(1),
    FOOD(5),
    MONEY(0);

    private final int price;

    OtherType(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String getName() {
        return this.name();
    }
}

