package com.controller;

import com.common.Direction;
import com.common.Position;
import com.common.SaveState;
import com.model.Organism;
import com.model.WorldModel;
import com.model.animal.Human;
import com.model.generator.OrganismGenerator;
import com.view.Field;
import com.view.WorldView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

public class WorldController {

    private final int numOfOrganisms = 1;
    private int worldWidth;
    private int worldLength;
    private WorldModel worldModel;
    private WorldView worldView;
    private int stepIndex = 0;
    private int turnNum = 0;
    private Human humanController;

    public WorldController(WorldModel worldModel, WorldView worldView) {
        this.worldModel = worldModel;
        this.worldView = worldView;
        this.worldWidth = worldView.getWorldWidth();
        this.worldLength = worldView.getWorldLength();
    }

    public void initialize() {
        worldView.addTurnListener(new TurnButtonAction());
        worldView.addStepListener(new StepButtonAction());
        worldView.addSaveStateListener(new SaveStateAction());
        worldView.addLoadStateListener(new LoadStateAction());
        worldView.addFirstAbilityListener(new FirstAbilityAction());
        worldView.addMovementListener(MovementAction::new);
        worldView.addCreateOrganismListener(new CreateOrganismAction());
    }

    public void drawWorld() {
        worldModel.initialize(worldWidth, worldLength, numOfOrganisms);
        if (worldModel.isHumanInGame()) humanController = worldModel.getHuman();
        worldView.initialize(worldWidth, worldLength, worldModel.getOrganisms());
    }

    public List<Organism> performStepAction() {
        List<Organism> organisms = worldModel.getOrganisms();
        Collections.sort(organisms);

        List<Organism> updatedOrganisms = IntStream.range(0, organisms.size())
                .mapToObj(i -> {
                    Organism selectedOrganism = organisms.get(i);
                    selectedOrganism.setInactive();
                    if (stepIndex == i) {
                        worldModel.takeStep(selectedOrganism);
                        if (selectedOrganism.shouldReproduce()) {
                            return worldModel.handleReproduction(selectedOrganism).stream();
                        }
                    }
                    return Stream.of(selectedOrganism);
                })
                .flatMap(s -> s)
                .collect(Collectors.toList());

        stepIndex += 1;
        stepIndex = stepIndex >= organisms.size() ? 0 : stepIndex;
        organisms.get(stepIndex).setActive();
        worldModel.refreshWorldMap(updatedOrganisms);
        return updatedOrganisms;
    }

    public void performTurnAction() {
        List<Organism> updatedOrganisms = new ArrayList<>(worldModel.getOrganisms());
        updatedOrganisms.forEach(organism -> performStepAction());
        turnNum += 1;
        if (worldModel.isHumanInGame())
            worldView.redrawWithHuman(updatedOrganisms, worldModel.getComments(), new CreateOrganismAction(), turnNum, humanController.getAntelopeSpeed());
        else
            worldView.redrawWithoutHuman(updatedOrganisms, worldModel.getComments(), new CreateOrganismAction(), turnNum);
    }

    public void performStepActionWithUIRefresh() {
        List<Organism> updatedOrganisms = performStepAction();
        if (worldModel.isHumanInGame())
            worldView.redrawWithHuman(updatedOrganisms, worldModel.getComments(), new CreateOrganismAction(), turnNum, humanController.getAntelopeSpeed());
        else
            worldView.redrawWithoutHuman(updatedOrganisms, worldModel.getComments(), new CreateOrganismAction(), turnNum);
    }

    public List<Organism> getOrganisms() {
        return worldModel.getOrganisms();
    }

    public Optional<Organism> getOrganismOnSpace(Position position) {
        return worldModel.getOrganismOnSpace(position);
    }

    private void loadSaveState(SaveState saveState) {
        worldWidth = saveState.getWorldWidth();
        worldLength = saveState.getWorldLength();

        worldModel = new WorldModel();
        worldModel.initialize(worldWidth, worldLength, 0);
        worldModel.loadWorldMap(saveState.getWorldMap(), worldWidth, worldLength);

        worldView.setVisible(false);
        worldView = new WorldView(false);


        worldView.initialize(worldWidth, worldLength, Collections.emptyList());

        initialize();

        if (worldModel.isHumanInGame()) humanController = worldModel.getHuman();
        stepIndex = saveState.getTurnNumber();
        turnNum = saveState.getTurnNumber();
        if (saveState.isHumanInGame())
            worldView.loadWorldWithHuman(worldWidth, worldLength, worldModel.getOrganisms(), worldModel.getComments(), new CreateOrganismAction(), turnNum, humanController.getAntelopeSpeed());
        else {
            worldView.loadWorldWithoutHuman(worldWidth, worldLength, worldModel.getOrganisms(), worldModel.getComments(), new CreateOrganismAction(), turnNum);
            int i = 0;
        }
    }

    public class MovementAction extends AbstractAction {

        private final Direction direction;

        public MovementAction(Direction direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!worldModel.isHumanInGame()) {
                worldView.noHumanMessage();
                return;
            }

            humanController.chooseMovementDirection(direction);
            performTurnAction();
        }
    }

    private class TurnButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            performTurnAction();
        }
    }

    private class StepButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            performStepActionWithUIRefresh();
        }
    }

    private class FirstAbilityAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (worldModel.isHumanInGame()) {
                humanController.activateAntelopeSpeed();
            }
        }
    }

    public class SaveStateAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String filePath = "saves/SaveState.ser";
            Path path = Paths.get(filePath);
            try {
                if (!Files.exists(path)) {
                    Files.createDirectories(path.getParent());
                    Files.createFile(path);
                } else {
                    Files.deleteIfExists(path);
                    Files.createFile(path);
                }
            } catch (IOException exc) {
                exc.printStackTrace();
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath);
                 ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(new SaveState(worldModel.getWorldMap(), turnNum, worldWidth, worldLength, worldModel.isHumanInGame()));
            } catch (IOException i) {
                i.printStackTrace();
            }

        }
    }

    public class LoadStateAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String filePath = "saves/SaveState.ser";
            Path path = Paths.get(filePath);
            try {
                if (Files.exists(path)) {
                    try (FileInputStream fileIn = new FileInputStream(filePath);
                         ObjectInputStream in = new ObjectInputStream(fileIn)) {
                        SaveState state = (SaveState) in.readObject();
                        loadSaveState(state);
                    } catch (IOException | ClassNotFoundException i) {
                        i.printStackTrace();
                    }
                } else {
                    worldView.noSaveFoundMessage();
                }
            } catch (SecurityException s) {
                s.printStackTrace();
            }
        }
    }

    private class CreateOrganismAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object[] organismTypes = {
                    "Sheep",
                    "Wolf",
                    "Fox",
                    "Turtle",
                    "Antelope",
                    "CyberSheep",
                    "Grass",
                    "Dandelion",
                    "Guarana",
                    "Nightshade",
                    "Hogweed"
            };
            Position where = ((Field) e.getSource()).getPosition();
            String choice = (String) JOptionPane.showInputDialog(null, "Choose which organism to add", "Add Organism", JOptionPane.PLAIN_MESSAGE, null, organismTypes, null);
            if (!choice.isEmpty()) {
                Organism generatedOrganism = OrganismGenerator.generateOrganism(choice, where);
                worldModel.setOrganism(generatedOrganism);
                if (worldModel.isHumanInGame())
                    worldView.redrawWithHuman(worldModel.getOrganisms(), worldModel.getComments(), new CreateOrganismAction(), turnNum, humanController.getAntelopeSpeed());
                else
                    worldView.redrawWithoutHuman(worldModel.getOrganisms(), worldModel.getComments(), new CreateOrganismAction(), turnNum);
            }
        }
    }
}
