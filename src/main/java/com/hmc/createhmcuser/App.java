package com.hmc.createhmcuser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


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
        String[] command = {"powershell.exe", "-ExecutionPolicy", "Bypass", "-Command", ". 'src/main/powershell/script.ps1'; getPhoneNumbers"};
        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = pb.start();
        process.waitFor();

        launch();

    }
}