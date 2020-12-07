package com.model.plant;

import com.common.Position;
import com.model.Organism;
import com.model.WorldMap;
import java.io.Serializable;
import java.util.Random;

public abstract class Plant extends Organism implements Serializable {

    protected double chanceToSpread = 0.1;

    public Plant(String iconLocation, int x, int y, int power) {
        super(iconLocation, x, y, 0, 0, true, false, power);
    }

    @Override
    public void action(WorldMap worldMap, Position position) {
        if (shouldSpread()) {
            setShouldReproduce(true);
        }
    }

    @Override
    public boolean isPlant() {
        return true;
    }

    public boolean shouldSpread() {
        return new Random().nextDouble() < chanceToSpread;
    }

    public abstract Organism reproduce(Position where);

}