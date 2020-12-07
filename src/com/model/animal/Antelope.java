package com.model.animal;

import com.common.Position;
import com.model.Commentator;
import com.model.Organism;
import com.model.WorldMap;
import java.io.Serializable;
import java.util.Optional;
import java.util.Random;

public class Antelope extends Animal implements Serializable {
    private final double EVADE_CHANCE = 0.5;

    public Antelope(int x, int y) {
        super("animals/antelope.png", x, y, 4, 0, true, false, 4);
        this.range = 2;
    }

    @Override
    public String toString() {
        return "Antelope[" + x + "," + y + "]";
    }

    @Override
    public Organism reproduce(Position where) {
        return new Antelope(where.getX(), where.getY());
    }

    public double getEvadeChance() {
        return EVADE_CHANCE;
    }

    @Override
    public Position positionAfterAction(WorldMap worldMap, Commentator commentator) {

        if (!canMove(worldMap)) {
            return new Position(x, y);
        }
        return super.positionAfterAction(worldMap, commentator);
    }

    @Override
    public void collision(Organism other, Optional<Position> freeSpaceNearby, Commentator commentator) {

        if (isSameSpecies(other) && canReproduceWith(other) && freeSpaceNearby.isPresent()) {
            copulateWith(other);
            return;
        }
        if (!isSameSpecies(other)) {

            if (new Random().nextDouble() > getEvadeChance() || freeSpaceNearby.isEmpty()) {
                fightWith(other);
                return;
            }
            commentator.addAntelopeStatement(this,other);
            other.move(getPosition());
            move(freeSpaceNearby.get());
        }
    }

}
