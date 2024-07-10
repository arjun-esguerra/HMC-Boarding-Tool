package com.hmc.boardingtool;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Home_Controller {

    @FXML private Button onboardButton;
    @FXML private Button offboardButton;

    private Scene onboardScene;
    private Scene offboardScene;

    public void initialize(Stage stage, Scene homeScene) throws IOException {
        // Load onboard user view in the background
        FXMLLoader onboardLoader = new FXMLLoader(App.class.getResource("onboard_user_view.fxml"));
        Parent onboardRoot = onboardLoader.load();
        Onboard_Controller onboardController = onboardLoader.getController();

        onboardController.initialize(stage, homeScene);

        if (onboardScene == null) {
            onboardScene = new Scene(onboardRoot);
        }

        // Go to onboard scene
        onboardButton.setOnAction(event -> {
            stage.setScene(onboardScene);
            stage.setResizable(false);
            stage.show();
        });

        // Load delete user view in the background
        FXMLLoader offboardLoader = new FXMLLoader(App.class.getResource("offboard_user_view.fxml"));
        Parent offboardRoot = offboardLoader.load();
        Offboard_Controller offboardController = offboardLoader.getController();

        offboardController.initialize(stage, homeScene);

        if (offboardScene == null) {
            offboardScene = new Scene(offboardRoot);
        }

        // Go to delete scene
        offboardButton.setOnAction(event -> {
            stage.setScene(offboardScene);
            stage.setResizable(false);
            stage.show();
        });
    }


}
