package com.model.plant;

import com.common.Position;
import com.model.Organism;
import com.model.WorldMap;
import java.io.Serializable;

public class Dandelion extends Plant implements Serializable {

    private static final int REPRODUCE_TRIES = 3;

    public Dandelion(int x, int y) {
        super("plants/dandelion.png", x, y, 0);
    }

    @Override
    public void action(WorldMap worldMap, Position position) {
        for (int i = 0; i < REPRODUCE_TRIES; i++) {
            if (tryToReproduce(worldMap, position)) return;
        }
    }

    @Override
    public String toString() {
        return "Dandelion[" + x + "," + y + "]";
    }

    public boolean tryToReproduce(WorldMap worldMap, Position position) {
        super.action(worldMap, position);
        return shouldReproduce();
    }

    @Override
    public Organism reproduce(Position where) {
        return new Dandelion(where.getX(), where.getY());
    }
}
