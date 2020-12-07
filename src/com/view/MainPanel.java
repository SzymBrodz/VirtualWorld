package com.view;

import com.common.Position;
import com.model.Organism;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

    private int gridFieldNum;
    private int width;
    private int length;
    private final ArrayList<Field> buttons;


    public MainPanel(int width, int length, List<Organism> organisms) {
        this.width = width;
        this.length = length;
        this.gridFieldNum = width * length;

        buttons = new ArrayList<>();
        setLayout(new GridLayout(width, length));

        Collections.sort(organisms);
        if (!organisms.isEmpty()) organisms.get(0).setActive();

        drawOrganisms(organisms);
    }

    private void drawOrganisms(List<Organism> organisms) {
        for (int i = 0; i < gridFieldNum; i++) {
            Position gfPos = getGridFieldPos(i);

            if (anyAliveOrganismInPosition(organisms, gfPos)) {
                Organism organism = getAliveOrganismInPosition(organisms, gfPos);
                Field field = new Field(gfPos, organism);
                buttons.add(field);
            } else buttons.add(new Field(gfPos));
            add(buttons.get(i));
        }
    }

    public void addCreateOrganismListener(ActionListener createOrganismListener) {
        for(Field b : buttons){
            b.addActionListener(createOrganismListener);
        }
    }

    private boolean anyAliveOrganismInPosition(List<Organism> organisms, Position pos) {
        return organisms.stream()
                .filter(Organism::isAlive)
                .anyMatch(o -> organismInPosition(o, pos));
    }

    private boolean anyOrganismInAdjacentPosition(List<Organism> organisms, Position pos) {
        return organisms.stream().anyMatch(o -> organismInPosition(o, pos));
    }

    private Organism getAliveOrganismInPosition(List<Organism> organisms, Position pos) {
        return organisms.stream()
                .filter(o -> o.isAlive())
                .filter(o -> organismInPosition(o, pos))
                .findFirst().orElse(null);
    }

    private boolean organismInPosition(Organism organism, Position pos) {
        return organism.getX() == pos.getX() && organism.getY() == pos.getY();
    }

    private Position getGridFieldPos(int gridFieldId) {
        return new Position(gridFieldId % width, (gridFieldId - gridFieldId % width) / width);
    }

    public void redraw(List<Organism> organisms, ActionListener createOrganismListener) {
        buttons.clear();
        removeAll();
        drawOrganisms(organisms);
        addCreateOrganismListener(createOrganismListener);
        revalidate();
        repaint();
    }

    public void setGridFieldNum(int width, int length){
        this.width = width;
        this.length = length;
        this.gridFieldNum = width * length;
    }
}
