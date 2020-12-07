package com.model;

import com.common.Direction;
import com.common.Position;
import java.io.Serializable;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public abstract class Organism implements Comparable<Organism>, Serializable {
    protected static int globalId = -1;
    protected final int initiative;
    protected final Integer id;
    protected int age;
    protected boolean shouldReproduce;
    protected int x;
    protected int y;
    protected boolean alive;
    protected int MINIMAL_BREEDING_AGE = 5;
    protected int power;
    protected boolean active = false;
    protected int range = 1;
    private String iconLocation;

    public Organism(String iconLocation, int x, int y, int initiative, int age, boolean alive,
                    boolean shouldReproduce, int power) {
        this.iconLocation = iconLocation;
        globalId += 1;
        this.id = globalId;
        this.x = x;
        this.y = y;
        this.initiative = initiative;
        this.age = age;
        this.alive = alive;
        this.shouldReproduce = shouldReproduce;
        this.power = power;
    }

    public URL getImageUrl() {
        return Objects.requireNonNull(getClass().getClassLoader().getResource(iconLocation));
    }

    public void setActive() {
        active = true;
    }

    public void setInactive() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public boolean shouldReproduce() {
        return shouldReproduce;
    }

    public Position getPosition() {
        return new Position(x, y);
    }

    public void setPosition(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    public int getAge() {
        return age;
    }

    public void ageByOneYear() {
        age += 1;
    }

    public void ageBy(int years) {
        age += years;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getInitiative() {
        return initiative;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public boolean isAnimal() {
        return false;
    }

    public int getRange() {
        return range;
    }

    public Position positionAfterAction(WorldMap worldMap, Commentator commentator) {

        if (worldMap.noFieldsNearby()) {
            return new Position(x, y);
        }

        switch (chooseDirection(worldMap)) {
            case UP -> {
                if (this.y > range - 1) return new Position(x, y - range);
                return positionAfterAction(worldMap, commentator);
            }
            case RIGHT -> {
                if (x < worldMap.getWorldWidth() - range) return new Position(x + range, y);
                return positionAfterAction(worldMap, commentator);
            }
            case DOWN -> {
                if (y < worldMap.getWorldLength() - range) return new Position(x, y + range);
                return positionAfterAction(worldMap, commentator);
            }
            case LEFT -> {
                if (x > range - 1) return new Position(x - range, y);
                return positionAfterAction(worldMap, commentator);
            }
        }
        return new Position(x, y);
    }

    public abstract void action(WorldMap worldMap, Position position);

    public void move(Position where) {
        x = where.getX();
        y = where.getY();
    }

    public boolean isSameSpecies(Organism that) {
        return that.getClass() == this.getClass();

    }

    public boolean canReproduceWith(Organism that) {
        return isSameSpecies(that)
                && this.getAge() >= MINIMAL_BREEDING_AGE && that.getAge() >= MINIMAL_BREEDING_AGE;
    }

    public boolean isPlant() {
        return false;
    }

    public void collision(Organism other, Optional<Position> freeSpaceNearby, Commentator commentator) {

        if (isSameSpecies(other) && canReproduceWith(other) && freeSpaceNearby.isPresent()) {
            copulateWith(other);
            commentator.addCopulationStatement(this, other);
            return;
        }

        if (!isSameSpecies(other)) {
            fightWith(other);
            commentator.addFightStatement(this, other);
            other.move(getPosition());
        }
    }

    protected void copulateWith(Organism other) {
        shouldReproduce = true;
        other.setShouldReproduce(true);
    }

    protected void fightWith(Organism other) {
        if (power > other.getPower()) {
            other.die();
        } else if (power == other.getPower()) {
            if (age > other.getAge()) {
                other.die();
            } else die();
        } else {
            die();
        }
    }

    public abstract Organism reproduce(Position where);

    public int compareTo(Organism that) {
        if (this.getInitiative() > that.getInitiative()) {
            return -1;
        } else if (this.getInitiative() < that.getInitiative()) {
            return 1;
        } else if (this.getInitiative() == that.getInitiative()) {
            if (this.getAge() > that.getAge()) {
                return -1;
            } else if (this.getAge() < that.getAge()) {
                return 1;
            }
        }

        return id.compareTo(that.id);
    }

    public Direction chooseDirection(WorldMap worldMap) {
        //losuje liczbe calkowita od 0 do 3
        int directionId = new Random().nextInt(4);
        return Direction.getById(directionId);
    }

    public void die() {
        alive = false;
    }

    public void setShouldReproduce(boolean shouldReproduce) {
        this.shouldReproduce = shouldReproduce;
    }

    public void shouldNoLongerReproduce() {
        shouldReproduce = false;
    }

    public boolean strongerThan(Organism that) {
        return this.power > that.power;
    }
}
