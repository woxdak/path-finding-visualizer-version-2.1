package com.gc;


import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;

import javax.swing.*;

public class main extends Application {

    @Override
    public void start(Stage stage) throws Exception{

        PathfindingFrame frame = new PathfindingFrame();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Pathfinding Visualiser Version 2.1");
        frame.setVisible(true);
    }

}
