package org.example.enums;

public enum ProductionType implements Item{
    COW_MILK(10),
    GOAT_MILK(12),
    EGGS(8),
    FEATHER(25),
    HORSE_MEAT(30),
    WOOL(23),
    PORK(15);

    private final int price;

    ProductionType(int price) {
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
