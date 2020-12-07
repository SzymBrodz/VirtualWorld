package com.model.plant;

import com.common.Position;
import com.model.Organism;
import java.io.Serializable;

public class Grass extends Plant implements Serializable {

    public Grass(int x, int y) {
        super("plants/grass.png", x, y, 0);
    }

    @Override
    public String toString() {
        return "Grass[" + x + "," + y + "]";
    }

    @Override
    public Organism reproduce(Position where) {
        return new Grass(where.getX(), where.getY());
    }
}
