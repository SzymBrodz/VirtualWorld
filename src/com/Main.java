package com;

import com.controller.WorldController;
import com.model.WorldModel;
import com.view.WorldView;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WorldModel worldModel = new WorldModel();
            WorldView worldView = new WorldView();
            WorldController worldController = new WorldController(worldModel, worldView);
            worldController.drawWorld();
            worldController.initialize();
        });
    }
}
