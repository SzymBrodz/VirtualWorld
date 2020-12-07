package com.model.plant;

import com.common.Position;
import com.model.Organism;
import java.io.Serializable;

public class Guarana extends Plant implements Serializable {

    private static final int ENERGETIC_POWER = 3;

    public Guarana(int x, int y) {
        super("plants/guarana.png", x, y, 0);
    }

    @Override
    public String toString() {
        return "Guarana[" + x + "," + y + "]";
    }

    @Override
    public Organism reproduce(Position where) {
        return new Guarana(where.getX(), where.getY());
    }

    @Override
    protected void fightWith(Organism other) {
        if (power > other.getPower()) {
            other.die();
        } else if (power == other.getPower()) {
            if (age > other.getAge()) {
                other.die();
            } else {
                other.setPower(other.getPower() + ENERGETIC_POWER);
                die();
            }
        } else {
            other.setPower(other.getPower() + ENERGETIC_POWER);
            die();
        }
    }
}
