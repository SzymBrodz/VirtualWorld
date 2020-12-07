package com.view;

import com.common.Position;
import com.common.StretchIcon;
import com.model.Organism;
import java.awt.Insets;
import javax.swing.JButton;

public class Field extends JButton {
    Position position;

    public Field(Position position, Organism organism) {
        super();
        this.position = position;

        setMargin(new Insets(0, 0, 0, 0));
        if (organism != null)
            setIcon(new StretchIcon(organism.getImageUrl()));

    }

    public Field(Position position) {
        this(position, null);
    }

    public Position getPosition() {
        return position;
    }
}
