package com.hmc.createhmcuser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    private Parent createRoot;
    private Create_Controller createController;

    @Override
    public void start(Stage stage) throws IOException {
        // Load and display homepage
        FXMLLoader homeLoader = new FXMLLoader(App.class.getResource("homepage_view.fxml"));
        Parent homepageRoot = homeLoader.load();
        Scene scene = new Scene(homepageRoot);
        stage.setScene(scene);

        Home_Controller homeController = homeLoader.getController();
        homeController.initialize(stage, scene); // Pass the scene here

        stage.setResizable(false);
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