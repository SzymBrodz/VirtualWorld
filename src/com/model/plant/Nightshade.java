package com.model.plant;

import com.common.Position;
import com.model.Commentator;
import com.model.Organism;
import java.io.Serializable;
import java.util.Optional;

public class Nightshade extends Plant implements Serializable {

    public Nightshade(int x, int y) {
        super("plants/nightshade.png ", x, y, 99);
    }

    @Override
    public void collision(Organism other, Optional<Position> freeSpaceNearby, Commentator commentator) {
        if (!isSameSpecies(other)) {
            fightWith(other);
            die();
        }
    }

    @Override
    public String toString() {
        return "Nightshade[" + x + "," + y + "]";
    }

    @Override
    public Organism reproduce(Position where) {
        return new Nightshade(where.getX(), where.getY());
    }
}
