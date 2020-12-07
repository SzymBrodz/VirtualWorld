package com.model.animal;

import com.common.Position;
import com.model.Commentator;
import com.model.Organism;
import com.model.WorldMap;
import java.io.Serializable;
import java.util.Optional;
import java.util.Random;

public class Turtle extends Animal implements Serializable {
    private final double MOVE_CHANCE = 0.25;

    public Turtle(int x, int y) {
        super("animals/turtle.png", x, y, 1, 0, true, false, 2);
    }

    @Override
    public Organism reproduce(Position where) {
        return new Turtle(where.getX(), where.getY());
    }

    @Override
    public String toString() {
        return "Turtle[" + x + "," + y + "]";
    }

    public void collision(Organism other, Optional<Position> freeSpaceNearby, Commentator commentator) {

        if (isSameSpecies(other) && canReproduceWith(other) && freeSpaceNearby.isPresent()) {
            shouldReproduce = true;
            other.setShouldReproduce(true);
            return;
        }

        if(other.getPower()>5) {
            if (!isSameSpecies(other)) {
                if (power > other.getPower()) {
                    other.die();
                } else if (power == other.getPower()) {
                    if (age > other.getAge()) {
                        other.die();
                    } else die();
                } else {
                    die();
                }
                other.move(getPosition());
            }
        }
        else commentator.addTurtleDeflectStatement(this, other);
    }

    @Override
    public Position positionAfterAction(WorldMap worldMap, Commentator commentator) {
        Random r = new Random();
        if (r.nextDouble() < MOVE_CHANCE) {
            return super.positionAfterAction(worldMap, commentator);
        }
        commentator.addTurtleNotMoveStatement(this);
        return new Position(x, y);
    }
}
