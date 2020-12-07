package com.model.generator;

import com.common.Position;
import com.model.Organism;
import com.model.animal.Antelope;
import com.model.animal.CyberSheep;
import com.model.animal.Fox;
import com.model.animal.Human;
import com.model.animal.Sheep;
import com.model.animal.Turtle;
import com.model.animal.Wolf;
import com.model.plant.Dandelion;
import com.model.plant.Grass;
import com.model.plant.Guarana;
import com.model.plant.Hogweed;
import com.model.plant.Nightshade;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public interface OrganismGenerator {

    static Organism generateOrganism(String organismName, Position where) {
        switch (organismName) {
            case "Sheep":
                return new Sheep(where.getX(), where.getY());
            case "Wolf":
                return new Wolf(where.getX(), where.getY());
            case "Fox":
                return new Fox(where.getX(), where.getY());
            case "Turtle":
                return new Turtle(where.getX(), where.getY());
            case "Antelope":
                return new Antelope(where.getX(), where.getY());
            case "CyberSheep":
                return new CyberSheep(where.getX(), where.getY());
            case "Grass":
                return new Grass(where.getX(), where.getY());
            case "Dandelion":
                return new Dandelion(where.getX(), where.getY());
            case "Guarana":
                return new Guarana(where.getX(), where.getY());
            case "Nightshade":
                return new Nightshade(where.getX(), where.getY());
            case "Hogweed":
                return new Hogweed(where.getX(), where.getY());
        }
        return null;
    }

    default List<Organism> generateOrganisms(int worldWidth, int worldLength, int numOfOrganisms) {

        class Pair {
            private final int number;
            private final BiFunction<Integer, Integer, Organism> createFunction;

            public Pair(int number, BiFunction<Integer, Integer, Organism> createFunction) {
                this.number = number;
                this.createFunction = createFunction;
            }
        }


        List<Pair> organismsWithNum = List.of(
                new Pair(1, Human::new),
                new Pair(0, Sheep::new),
                new Pair(0, Wolf::new),
                new Pair(0, Fox::new),
                new Pair(0, Turtle::new),
                new Pair(0, Antelope::new),
                new Pair(0, CyberSheep::new),
                new Pair(0, Grass::new),
                new Pair(0, Dandelion::new),
                new Pair(0, Guarana::new),
                new Pair(0, Nightshade::new),
                new Pair(0, Hogweed::new));

        if (organismsWithNum.stream().mapToInt(p -> p.number).sum() > worldWidth * worldLength) {
            System.err.println("Too much organisms");
            throw new IllegalArgumentException("Too much organisms");
        }

        List<Organism> generatedOrganisms = new ArrayList<>(numOfOrganisms);


        organismsWithNum.forEach(pair -> IntStream.range(0, (int) (pair.number * numOfOrganisms))
                .mapToObj(i -> pair.createFunction.apply(0, 0))
                .forEach(generatedOrganisms::add));

        return generatedOrganisms;
    }
}