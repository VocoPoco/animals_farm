package org.example.enums;

public enum ProductionType implements Item{
    COW_MILK,
    GOAT_MILK,
    EGGS,
    FEATHER,
    HORSE_MEAT,
    WOOL,
    PORK;

    @Override
    public String getName() {
        return this.name();
    }
}
