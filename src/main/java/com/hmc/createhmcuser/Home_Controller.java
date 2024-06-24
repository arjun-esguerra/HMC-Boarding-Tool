package com.hmc.createhmcuser;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Home_Controller {

    @FXML private Button createButton;
    @FXML private Button deleteButton;

    private Scene createScene;
    private Scene deleteScene;

    public void initialize(Stage stage, Scene homeScene) throws IOException {
        // Load create user view in the background
        FXMLLoader createLoader = new FXMLLoader(App.class.getResource("create_user_view.fxml"));
        Parent createRoot = createLoader.load();
        Create_Controller createController = createLoader.getController();

        createController.initialize(stage, homeScene);

        if (createScene == null) {
            createScene = new Scene(createRoot);
        }

        // Go to create scene
        createButton.setOnAction(event -> {
            stage.setScene(createScene);
            stage.setResizable(false);
            stage.show();
        });

        // Load delete user view in the background
        FXMLLoader deleteLoader = new FXMLLoader(App.class.getResource("delete_user_view.fxml"));
        Parent deleteRoot = deleteLoader.load();
        Delete_Controller deleteController = deleteLoader.getController();

        deleteController.initialize(stage, homeScene);

        if (deleteScene == null) {
            deleteScene = new Scene(deleteRoot);
        }

        // Go to delete scene
        deleteButton.setOnAction(event -> {
            stage.setScene(deleteScene);
            stage.setResizable(false);
            stage.show();
        });
    }


}
