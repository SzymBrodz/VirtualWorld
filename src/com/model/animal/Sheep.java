package com.model.animal;


import com.common.Position;
import com.model.Organism;
import java.io.Serializable;

public class Sheep extends Animal implements Serializable {


    public Sheep(int x, int y) {
        super("animals/sheep.png", x, y, 4, 0, true, false, 4);
    }

    @Override
    public String toString() {
        return "Sheep[" + x + "," + y + "]";
    }

    @Override
    public Organism reproduce(Position where) {
        return new Sheep(where.getX(), where.getY());
    }

}
