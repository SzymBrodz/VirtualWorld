package com.model.animal;

import com.common.Position;
import com.model.Organism;
import com.model.WorldMap;
import java.io.Serializable;

public abstract class Animal extends Organism implements Serializable {

    public Animal(String iconLocation, int x, int y, int initiative, int age, boolean alive, boolean pregnant, int power) {
        super(iconLocation, x, y, initiative, age, alive, pregnant, power);
    }

    public void action(WorldMap worldMap, Position position) {
        move(position);
    }

    @Override
    public boolean isAnimal() {
        return true;
    }

    protected boolean canMove(WorldMap worldMap) {
        return worldMap.isSpaceReachableFrom(this, range);
    }
}
