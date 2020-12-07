package com.model.animal;

import com.common.Position;
import com.model.Commentator;
import com.model.Organism;
import com.model.WorldMap;
import com.model.plant.Hogweed;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CyberSheep extends Animal implements Serializable {

    public CyberSheep(int x, int y) {
        super("animals/cybersheep.png", x, y, 4, 0, true, false, 11);
    }

    private Optional<Position> radarHogweedPosition(WorldMap worldMap) {
        return worldMap.getOrganisms().stream()
                .filter(organism -> organism instanceof Hogweed)
                .map(Organism::getPosition)
                .min(Comparator.comparingInt(p -> p.distanceBetweenPositions(this.getPosition())));
    }

    @Override
    public Position positionAfterAction(WorldMap worldMap, Commentator commentator) {
        Optional<Position> target = radarHogweedPosition(worldMap);
        if (target.isPresent()) {
            commentator.addCyberSheepStatement(this, target.get());
            List<Position> directions = List.of(
                    new Position(x, y - 1), new Position(x + 1, y),
                    new Position(x, y + 1), new Position(x - 1, y));
            return directions.stream()
                    .min(Comparator.comparingInt(d -> d.distanceBetweenPositions(target.get())))
                    .orElse(new Position(x, y));

        } else return super.positionAfterAction(worldMap, commentator);
    }

    @Override
    public String toString() {
        return "CyberSheep[" + x + "," + y + "]";
    }

    @Override
    public Organism reproduce(Position where) {
        return new CyberSheep(where.getX(), where.getY());
    }
}
