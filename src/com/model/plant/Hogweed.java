package com.model.plant;

import com.common.Position;
import com.model.Organism;
import com.model.WorldMap;
import com.model.animal.Animal;
import com.model.animal.CyberSheep;
import java.io.Serializable;
import java.util.List;

public class Hogweed extends Plant implements Serializable {

    public Hogweed(int x, int y) {
        super("plants/hogweed.png", x, y, 10);
    }

    @Override
    public void action(WorldMap worldMap, Position position) {
        killEmAll(worldMap);
        super.action(worldMap, position);
    }
    private void killEmAll(WorldMap worldMap) {
        List<Animal> nearbyAnimals = worldMap.getNearbyAnimals(this);
        nearbyAnimals.forEach(animal -> {
            if(!(animal instanceof CyberSheep)) animal.die();
        });
    }

    @Override
    public String toString() {
        return "Hogweed[" + x + "," + y + "]";
    }

    @Override
    public Organism reproduce(Position where) {
        return new Hogweed(where.getX(), where.getY());
    }
}
