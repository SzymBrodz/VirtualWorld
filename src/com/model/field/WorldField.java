package com.model.field;

import com.model.Organism;
import java.util.Optional;

public interface WorldField {

    boolean isEmpty();

    Optional<Organism> getOrganism();

    boolean isOrganismField();

    default boolean isAnimalField() {
        return false;
    }

    default boolean nonEmpty() {
        return !isEmpty();
    }

}
