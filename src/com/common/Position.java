package com.common;

import com.model.Organism;
import com.model.animal.CyberSheep;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isOutOfBounds(int worldWidth, int worldLength){
        if(x >= 0 && x < worldWidth && y >= 0 && y < worldLength) return false;
        return true;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int distanceBetweenPositions(Position that){
        return Math.abs(x-that.getX())+Math.abs(y-that.getY());
    }

}
