package com.hmc.createhmcuser;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Delete_Controller {

    @FXML private Button backButton;
    @FXML private Button deleteButton;

    public void initialize(Stage stage, Scene previousScene) {

        backButton.setOnAction(event -> {
            stage.setScene(previousScene);
        });
    }

}
