package com.model.animal;

import com.common.Direction;
import com.common.Position;
import com.model.Commentator;
import com.model.Organism;
import com.model.WorldMap;
import java.io.Serializable;
import java.util.Random;

public class Human extends Animal implements Serializable {

    private Ability antelopeSpeed = new Ability();
    private Direction movementDirection;

    public Ability getAntelopeSpeed() {
        return antelopeSpeed;
    }

    public Human(int x, int y) {
        super("animals/human.png", x, y, 4, 0, true, false, 5);
    }

    public void activateAntelopeSpeed() {
        if (antelopeSpeed.getCooldown() == 0) {
            antelopeSpeed.setActivated(true);
            antelopeSpeed.setDuration(5);
            antelopeSpeed.setCooldown(10);
        } else System.err.println("Ability on cooldown");
    }

    private void checkAntelopeSpeed() {
        if (antelopeSpeed.getDuration() > 2 && antelopeSpeed.getDuration() <= 5) range = 2;
        else if (antelopeSpeed.getDuration() > 0) {
            if (new Random().nextDouble() < 0.5) range = 2;
            else range = 1;
        }
        antelopeSpeed.tickDuration();
         if (antelopeSpeed.getDuration() == 0) {
             antelopeSpeed.setActivated(false);
            range = 1;
        }
    }

    @Override
    public Direction chooseDirection(WorldMap worldMap) {
        return movementDirection;
    }

    public void chooseMovementDirection(Direction direction) {
        movementDirection = direction;
    }

    public int getAntelopeSpeedCooldown() {
        return antelopeSpeed.getCooldown();
    }

    public int getAntelopeSpeedDuration() {
        return antelopeSpeed.getDuration();
    }

    public boolean isAntelopeSpeedActivated() {
        return antelopeSpeed.isActivated();
    }

    @Override
    public Position positionAfterAction(WorldMap worldMap, Commentator commentator) {
        reduceCooldown();
        if (antelopeSpeed.isActivated()) checkAntelopeSpeed();

        if (!canMove(worldMap)) {
            return new Position(x, y);
        }
        switch (chooseDirection(worldMap)) {
            case UP -> {
                if (this.y > range - 1) return new Position(x, y - range);
                return new Position(x, y);
            }
            case RIGHT -> {
                if (x < worldMap.getWorldWidth() - range) return new Position(x + range, y);
                return new Position(x, y);
            }
            case DOWN -> {
                if (y < worldMap.getWorldLength() - range) return new Position(x, y + range);
                return new Position(x, y);
            }
            case LEFT -> {
                if (x > range - 1) return new Position(x - range, y);
                return new Position(x, y);
            }
        }
        return new Position(x, y);
    }

    private void reduceCooldown() {
        if (antelopeSpeed.getCooldown() > 0) antelopeSpeed.tickCooldown();
    }


    @Override
    public String toString() {
        return "Human[" + x + "," + y + "]";
    }

    @Override
    public Organism reproduce(Position where) {
        return new Human(where.getX(), where.getY());
    }

    public class Ability implements Serializable{
        private int cooldown = 0;
        private int duration = 0;
        private boolean activated = false;
        public int getCooldown() {
            return cooldown;
        }

        public void setCooldown(int cooldown) {
            this.cooldown = cooldown;
        }

        public void tickCooldown(){
            if(cooldown > 0) cooldown -= 1;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public void tickDuration(){
            if(duration > 0) duration -= 1;
        }

        public boolean isActivated() {
            return activated;
        }

        public void setActivated(boolean activated) {
            this.activated = activated;
        }

        public boolean isOnCooldown(){
            if (cooldown == 0) return false;
            else return true;
        }
    }
}
