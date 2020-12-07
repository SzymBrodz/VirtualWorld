package com.model.field;

import com.model.Organism;

import java.io.Serializable;
import java.util.Optional;

public class OrganismField implements WorldField, Serializable {

    private final Organism organism;

    public OrganismField(Organism organism) {
        this.organism = organism;
    }

    public Optional<Organism> getOrganism() {
        return Optional.of(organism);
    }

    @Override
    public boolean isOrganismField() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
