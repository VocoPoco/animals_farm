package org.example.enums;

public enum OtherType implements Item {
    MEDICINE,
    WATER,
    FOOD,
    MONEY;
    @Override
    public String getName() {
        return this.name();
    }
}
