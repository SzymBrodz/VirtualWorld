package com.model.animal;

import com.common.Position;
import com.model.Commentator;
import com.model.Organism;
import com.model.WorldMap;

import java.io.Serializable;
import java.util.Optional;

public class Fox extends Animal implements Serializable {

    public Fox(int x, int y) {
        super("animals/fox.png", x, y, 7, 0, true, false, 3);
    }

    @Override
    public Position positionAfterAction(WorldMap worldMap, Commentator commentator) {

        switch (chooseDirection(worldMap)) {
            case UP -> {
                if (this.y != 0) {
                    Position newPosition = new Position(x, y - 1);
                    return goodSmell(worldMap, newPosition, commentator);
                }
                return positionAfterAction(worldMap, commentator);
            }
            case RIGHT -> {
                if (x != worldMap.getWorldWidth() - 1) {
                    Position newPosition = new Position(x + 1, y);
                    return goodSmell(worldMap, newPosition, commentator);
                }
                return positionAfterAction(worldMap, commentator);
            }
            case DOWN -> {
                if (y != worldMap.getWorldLength() - 1) {
                    Position newPosition = new Position(x, y + 1);
                    return goodSmell(worldMap, newPosition, commentator);
                }
                return positionAfterAction(worldMap, commentator);
            }
            case LEFT -> {
                if (x != 0) {
                    Position newPosition = new Position(x - 1, y);
                    return goodSmell(worldMap, newPosition, commentator);
                }
                return positionAfterAction(worldMap, commentator);
            }
        }
        return new Position(x, y);
    }

    private Position goodSmell(WorldMap worldMap, Position newPosition, Commentator commentator) {
        if (isStrongerOrganismOnSpace(worldMap, newPosition)) {
            commentator.addFoxStatement(this, newPosition);
            if (!worldMap.isFreeSpaceNearby(this, 1)) {
                return new Position(x, y);
            } else {
                return positionAfterAction(worldMap, commentator);
            }
        }
        return newPosition;
    }

    private boolean isStrongerOrganismOnSpace(WorldMap worldMap, Position newPosition) {
        Optional<Organism> maybeOrganism = worldMap.getOrganismOnSpace(newPosition);
        if (maybeOrganism.isEmpty()) {
            return false;
        }

        Organism organism = maybeOrganism.get();
        return organism.strongerThan(this);

    }

    @Override
    public Organism reproduce(Position where) {
        return new Fox(where.getX(), where.getY());
    }

    @Override
    public String toString() {
        return "Fox[" + x + "," + y + "]";
    }
}
