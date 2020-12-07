package com.model.field;

import com.model.Organism;

import java.io.Serializable;
import java.util.Optional;

public class EmptyField implements WorldField, Serializable {

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Optional<Organism> getOrganism() {
        return Optional.empty();
    }

    @Override
    public boolean isOrganismField() {
        return false;
    }
}
