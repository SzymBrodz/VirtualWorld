package com.common;

import com.model.WorldMap;
import java.io.Serializable;

public class SaveState implements Serializable {

    private final WorldMap worldMap;
    private final int turnNumber;
    private final int worldWidth;
    private final int worldLength;
    private final boolean humanInGame;

    public SaveState(WorldMap worldMap, int turnNumber, int worldWidth, int worldLength, boolean humanInGame) {
        this.worldMap = worldMap;
        this.turnNumber = turnNumber;
        this.worldWidth = worldWidth;
        this.worldLength = worldLength;
        this.humanInGame = humanInGame;
    }

    public boolean isHumanInGame() {
        return humanInGame;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldLength() {
        return worldLength;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
}
