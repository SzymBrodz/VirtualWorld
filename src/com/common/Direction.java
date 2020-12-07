package com.common;

import java.util.Arrays;

public enum Direction {
    UP(0),
    RIGHT(1),
    DOWN(2),
    LEFT(3);

    private final int id;

    Direction(int id) {
        this.id = id;
    }

    public static Direction getById(int directionId) {
        return Arrays.stream(values())
                .filter(dir -> dir.id == directionId)
                .findFirst()
                .orElse(null);
    }
}
