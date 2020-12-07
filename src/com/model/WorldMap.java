package com.model;

import com.common.Position;
import com.model.animal.Animal;
import com.model.field.AnimalField;
import com.model.field.EmptyField;
import com.model.field.WorldField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorldMap extends HashMap<Position, WorldField> implements Serializable {

    private final int worldWidth;
    private final int worldLength;

    public WorldMap(int worldWidth, int worldLength) {
        this.worldWidth = worldWidth;
        this.worldLength = worldLength;

        for (int x = 0; x < worldWidth; x++) {
            for (int y = 0; y < worldLength; y++) {
                Position worldPos = new Position(x, y);
                put(worldPos, new EmptyField());
            }
        }
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldLength() {
        return worldLength;
    }

    public Optional<Organism> getOrganismOnSpace(Position wantedPosition) {
        return get(wantedPosition).getOrganism();
    }

    private Stream<Entry<Position, WorldField>> getNearbySpaces(Organism organism, int distance) {
        Position position = organism.getPosition();
        int x = position.getX();
        int y = position.getY();

        return entrySet().stream()
                .filter(entry -> entry.getKey().equals(new Position(x - distance, y))
                        || entry.getKey().equals(new Position(x + distance, y))
                        || entry.getKey().equals(new Position(x, y - distance))
                        || entry.getKey().equals(new Position(x, y + distance)));
    }

    public List<Position> getFreePositionsNearby(Organism organism, int distance) {
        return getNearbySpaces(organism, distance)
                .filter(entry -> entry.getValue().isEmpty())
                .map(Entry::getKey)
                .collect(Collectors.toList());
    }

    public boolean isFreeSpaceNearby(Organism organism, int distance) {
        return getNearbySpaces(organism, distance).anyMatch(entry -> entry.getValue().isEmpty());
    }

    public boolean isSpaceReachableFrom(Organism organism, int distance) {
        return getNearbySpaces(organism, distance).count() > 0;
    }

    public List<Organism> getOrganisms() {
        return new ArrayList<>(values()).stream()
                .map(WorldField::getOrganism)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public boolean noFieldsNearby() {
        return size() == 1;
    }

    public List<Animal> getNearbyAnimals(Organism organism) {
        return getNearbySpaces(organism, 1)
                .map(Entry::getValue)
                .filter(WorldField::isAnimalField)
                .map(animalField -> (AnimalField) animalField)
                .map(AnimalField::getAnimal)
                .collect(Collectors.toList());
    }

    public Position getRandomFreeSpace() {
        List<Entry<Position, WorldField>> emptyEntries = entrySet().stream().filter(entry -> entry.getValue().isEmpty()).collect(Collectors.toList());
        return emptyEntries.get(new Random().nextInt(emptyEntries.size())).getKey();
    }
}
