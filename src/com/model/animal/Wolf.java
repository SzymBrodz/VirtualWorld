package com.model.animal;

import com.common.Position;
import com.model.Organism;
import java.io.Serializable;

public class Wolf extends Animal implements Serializable {

    public Wolf(int x, int y) {
        super("animals/wolf.png", x, y, 5, 0, true, false, 9);
    }

    @Override
    public String toString() {
        return "Wolf[" + x + "," + y + "]";
    }

    @Override
    public Organism reproduce(Position where) {
        return new Wolf(where.getX(), where.getY());
    }
}
