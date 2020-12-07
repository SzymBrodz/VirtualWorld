package com.model;

import com.common.Position;
import com.model.animal.Human;
import com.model.field.AnimalField;
import com.model.field.OrganismField;
import com.model.generator.OrganismGenerator;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class WorldModel implements OrganismGenerator {

    Commentator commentator = new Commentator();
    private int worldWidth;
    private int worldLength;
    private WorldMap worldMap;
    private boolean humanInGame = false;

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public void loadWorldMap(WorldMap worldMap, int width, int length) {
        this.worldMap = worldMap;
        this.worldWidth = width;
        this.worldLength = length;
    }

    public void initialize(int worldWidth, int worldLength, int numOfOrganisms) {

        List<Organism> organisms = generateOrganisms(worldWidth, worldLength, numOfOrganisms);
        initialize(worldWidth, worldLength);
        initWorldMapWithRandomOrganismsPositions(organisms);
    }

    public void initialize(int worldWidth, int worldLength) {
        this.worldWidth = worldWidth;
        this.worldLength = worldLength;
    }

    public void initWorldMapWithRandomOrganismsPositions(List<Organism> organisms) {
        WorldMap refreshedWorldMap = new WorldMap(worldWidth, worldLength);

        humanInGame = organisms.stream().anyMatch(organism -> organism instanceof Human && organism.isAlive());

        organisms.stream()
                .filter(Organism::isAlive)
                .forEach(organism -> {
                    organism.setPosition(refreshedWorldMap.getRandomFreeSpace());
                    if (organism.isAnimal()) {
                        refreshedWorldMap.put(organism.getPosition(), new AnimalField(organism));
                    } else {
                        refreshedWorldMap.put(organism.getPosition(), new OrganismField(organism));
                    }
                });

        this.worldMap = refreshedWorldMap;
    }

    public void refreshWorldMap(List<Organism> organisms) {

        WorldMap refreshedWorldMap = new WorldMap(worldWidth, worldLength);

        humanInGame = organisms.stream().anyMatch(organism -> organism instanceof Human && organism.isAlive());

        organisms.stream()
                .filter(Organism::isAlive)
                .forEach(organism -> {
                    if (organism.isAnimal()) {
                        refreshedWorldMap.put(organism.getPosition(), new AnimalField(organism));
                    } else {
                        refreshedWorldMap.put(organism.getPosition(), new OrganismField(organism));
                    }
                });

        this.worldMap = refreshedWorldMap;
    }

    public List<Organism> getOrganisms() {
        return worldMap.getOrganisms();
    }

    public Human getHuman() {
        for (Organism o : getOrganisms()) {
            if (o instanceof Human) return (Human) o;
        }
        return new Human(0, 0);
    }

    public List<Organism> getReproducingOrganisms() {
        return getOrganisms().stream()
                .filter(organism -> organism.shouldReproduce)
                .collect(Collectors.toList());
    }

    public List<Organism> handleReproduction(Organism reproducingOrganism) {
        List<Organism> reproducingOrganisms = getReproducingOrganisms();
        Optional<Position> maybeFreeSpace = findFreeSpaceNearby(reproducingOrganisms);

        if (maybeFreeSpace.isPresent()) {
            Organism babyOrganism = reproducingOrganism.reproduce(maybeFreeSpace.get());
            reproducingOrganisms.forEach(Organism::shouldNoLongerReproduce);
            return List.of(reproducingOrganism, babyOrganism);
        }

        reproducingOrganisms.forEach(Organism::shouldNoLongerReproduce);
        return List.of(reproducingOrganism);
    }

    private Optional<Position> findFreeSpaceNearby(List<Organism> organisms) {
        return organisms.stream()
                .map(this::findFreeSpaceNearby)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<Position> findFreeSpaceNearby(Organism organism) {
        List<Position> freePositions = worldMap.getFreePositionsNearby(organism, 1);

        if (freePositions.isEmpty()) return Optional.empty();
        return Optional.of(freePositions.get(new Random().nextInt(freePositions.size())));
    }

    public Optional<Organism> getOrganismOnSpace(Position wantedPosition) {
        return worldMap.getOrganismOnSpace(wantedPosition);
    }

    public void handleCollision(Organism victim, Organism aggressor) {
        Optional<Position> maybeFreeSpaceNearby = findFreeSpaceNearby(victim);
        victim.collision(aggressor, maybeFreeSpaceNearby, commentator);
    }

    public boolean isHumanInGame() {
        return humanInGame;
    }

    public void takeStep(Organism organism) {
        Position wantedPosition = organism.positionAfterAction(worldMap, commentator);
        Optional<Organism> maybeVictimOrganism = getOrganismOnSpace(wantedPosition);
        if (maybeVictimOrganism.isEmpty() || organism.isPlant()) {
            //commentator.addMoveStatement(organism, wantedPosition);
            organism.action(worldMap, wantedPosition);
        } else if (!maybeVictimOrganism.get().equals(organism)) {
            handleCollision(maybeVictimOrganism.get(), organism);
        }

        organism.ageByOneYear();
    }

    public void setOrganism(Organism organism){
        worldMap.put(organism.getPosition(),new OrganismField(organism));
    }

    public String getComments() {
        return commentator.getComments();
    }
}