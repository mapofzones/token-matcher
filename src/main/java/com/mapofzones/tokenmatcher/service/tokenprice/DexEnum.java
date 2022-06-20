package com.mapofzones.tokenmatcher.service.tokenprice;

public enum DexEnum {

    COINGECKO("Coingecko"),
    OSMOSIS("Osmosis"),
    SUPPLY("Supply");

    private final String name;

    DexEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
