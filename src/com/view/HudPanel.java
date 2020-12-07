package com.view;

import com.model.animal.Human;

import java.awt.event.ActionListener;
import javax.swing.*;

public class HudPanel extends JPanel {

    private final JButton turnButton;
    private final JButton stepButton;
    private final JButton saveStateButton;
    private final JButton loadStateButton;
    private final JButton firstAbilityButton;
    private final JLabel firstAbilityButtonTimer;


    public HudPanel() {
        this.turnButton = new JButton("Turn");
        add(turnButton);

        this.stepButton = new JButton("Step");
        add(stepButton);

        this.saveStateButton = new JButton("Save");
        add(saveStateButton);

        this.loadStateButton = new JButton("Load");
        add(loadStateButton);

        this.firstAbilityButton = new JButton("Antelope Speed");
        add(firstAbilityButton);

        this.firstAbilityButtonTimer = new JLabel("Ready");
        add(firstAbilityButtonTimer);
    }

    public void updateFirstAbilityButtonTimer(Human.Ability ability) {
        if (ability != null) {
            if (ability.isActivated()) {
                firstAbilityButtonTimer.setText("Actived (" + ability.getDuration() + ")");
            } else if (ability.isOnCooldown()) {
                firstAbilityButtonTimer.setText("Not Ready (" + ability.getCooldown() + ")");
            } else firstAbilityButtonTimer.setText("Ready");
        }
    }

    public void addTurnListener(ActionListener turnButtonListener) {
        turnButton.addActionListener(turnButtonListener);
    }

    public void addStepListener(ActionListener stepButtonListener) {
        stepButton.addActionListener(stepButtonListener);
    }

    public void addSaveStateListener(ActionListener saveStateAction) { saveStateButton.addActionListener(saveStateAction); }

    public void addLoadStateListener(ActionListener loadStateAction) { loadStateButton.addActionListener(loadStateAction); }

    public void addFirstAbilityListener(ActionListener FirstAbilityAction) { firstAbilityButton.addActionListener(FirstAbilityAction); }
}
