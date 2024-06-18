package com.hmc.createhmcuser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("input_view.fxml"));
        Parent root = fxmlLoader.load();

        Controller controller = fxmlLoader.getController();
        controller.initialize();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        InputStream scriptStream = App.class.getResourceAsStream("/script.ps1");

        // Create a temporary file
        File tempScript = File.createTempFile("script", ".ps1");
        tempScript.deleteOnExit();

        Files.copy(scriptStream, tempScript.toPath(), StandardCopyOption.REPLACE_EXISTING);

        String[] command = {"powershell.exe", "-ExecutionPolicy", "Bypass", "-Command", ". '" + tempScript.getAbsolutePath() + "'; getPhoneNumbers"};
        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = pb.start();
        process.waitFor();

        launch();
    }
}