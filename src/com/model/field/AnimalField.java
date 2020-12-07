package com.model.field;

import com.model.Organism;
import com.model.animal.Animal;
import java.io.Serializable;

public class AnimalField extends OrganismField implements Serializable {

    @Override
    public boolean isAnimalField() {
        return true;
    }

    public AnimalField(Organism animal) {
        super(animal);
    }

    public Animal getAnimal() {
        return (Animal) getOrganism().get();
    }
}
