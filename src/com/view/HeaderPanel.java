package com.view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class HeaderPanel extends JPanel {
    private final JLabel signature;
    private JLabel turnNumber;

    public HeaderPanel() {
        this.signature = new JLabel("Szymon Brodziński 180017");
        turnNumber = new JLabel("Turn number: " + 0);
        add(signature);
        add(turnNumber);
    }
    public void redraw(int newTurnNum){
        turnNumber.setText("Turn number: " + newTurnNum);
    }
}
