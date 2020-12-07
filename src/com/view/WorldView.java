package com.view;

import com.common.Direction;
import com.model.Organism;
import com.model.animal.Human;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class WorldView extends JFrame {

    private MainPanel mainPanel;
    private HudPanel hudPanel;
    private CommentatorPanel commentatorPanel;
    private HeaderPanel headerPanel;
    private int worldWidth;
    private int worldLength;

    public WorldView() {
        this(true);
    }

    public WorldView(boolean showDialog) {
        super("Virtual World");
        if (showDialog) {
            worldWidth = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Enter world width", "Virtual World", JOptionPane.PLAIN_MESSAGE, null, null, 20));
            worldLength = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Enter world height", "Virtual World", JOptionPane.PLAIN_MESSAGE, null, null, 20));
        }
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldLength() {
        return worldLength;
    }

    public void initialize(int width, int length, List<Organism> organisms) {
        this.hudPanel = new HudPanel();
        this.mainPanel = new MainPanel(width, length, organisms);
        this.commentatorPanel = new CommentatorPanel();
        this.headerPanel = new HeaderPanel();
        setPreferredSize(new Dimension(1300, 1000));

        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(commentatorPanel, BorderLayout.EAST);
        add(hudPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);//musi byc po setsize i po pack()
        setVisible(true);//musi byc ostatni
    }

    public void redrawWithHuman(List<Organism> organisms, String comments, ActionListener createOrganismAction, int turnNum, Human.Ability as) {
        redrawWithoutHuman(organisms, comments, createOrganismAction, turnNum);
        hudPanel.updateFirstAbilityButtonTimer(as);
    }

    public void redrawWithoutHuman(List<Organism> organisms, String comments, ActionListener createOrganismAction, int turnNum) {
        mainPanel.redraw(organisms, createOrganismAction);
        commentatorPanel.writeComments(comments);
        headerPanel.redraw(turnNum);
    }

    public void loadWorldWithHuman(int worldWidth, int worldLength, List<Organism> organisms, String comments,
                                   ActionListener createOrganismAction, int turnNum, Human.Ability as) {
        loadWorldWithoutHuman(worldWidth, worldLength, organisms, comments, createOrganismAction, turnNum);
        hudPanel.updateFirstAbilityButtonTimer(as);
    }

    public void loadWorldWithoutHuman(int worldWidth, int worldLength, List<Organism> organisms, String comments,
                                      ActionListener createOrganismAction, int turnNum) {
        this.worldWidth = worldWidth;
        this.worldLength = worldLength;
        mainPanel.redraw(organisms, createOrganismAction);
        commentatorPanel.writeComments(comments);
        headerPanel.redraw(turnNum);
    }

    public void noSaveFoundMessage() {
        JOptionPane.showMessageDialog(this, "No save found!");
    }

    public void noHumanMessage() {
        JOptionPane.showMessageDialog(this, "No human in game!");
    }

    public void addTurnListener(ActionListener turnButtonListener) {
        hudPanel.addTurnListener(turnButtonListener);
    }

    public void addStepListener(ActionListener stepButtonListener) {
        hudPanel.addStepListener(stepButtonListener);
    }

    public void addSaveStateListener(ActionListener saveStateAction) {
        hudPanel.addSaveStateListener(saveStateAction);
    }

    public void addLoadStateListener(ActionListener saveLoadAction) {
        hudPanel.addLoadStateListener(saveLoadAction);
    }

    public void addCreateOrganismListener(ActionListener createOrganismAction){mainPanel.addCreateOrganismListener(createOrganismAction); }

    public void addFirstAbilityListener(ActionListener firstAbilityAction) { hudPanel.addFirstAbilityListener(firstAbilityAction); }

    public void addMovementListener(Function<Direction, AbstractAction> movementActionFunction) {
        Map<Direction, Integer> directionToKey = Map.of(
                Direction.UP, KeyEvent.VK_UP,
                Direction.DOWN, KeyEvent.VK_DOWN,
                Direction.LEFT, KeyEvent.VK_LEFT,
                Direction.RIGHT, KeyEvent.VK_RIGHT);

        directionToKey.forEach((direction, key) -> {
            mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, 0), direction);
            mainPanel.getActionMap().put(direction, movementActionFunction.apply(direction));
        });
    }
}