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
        // Load and display homepage
        FXMLLoader homepageLoader = new FXMLLoader(App.class.getResource("homepage_view.fxml"));
        Parent homepageRoot = homepageLoader.load();
        Homepage_Controller homepageCreateUserController = homepageLoader.getController();
        homepageCreateUserController.initialize();

        Scene scene = new Scene(homepageRoot);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


        // Load create user view in the background
        FXMLLoader createUserLoader = new FXMLLoader(App.class.getResource("create_user_view.fxml"));
        Parent createUserRoot = createUserLoader.load();
        Create_User_Controller createUserCreateUserController = createUserLoader.getController();
        createUserCreateUserController.initialize();


    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] command = {"powershell.exe", "-ExecutionPolicy", "Bypass", "-Command", ". 'src/main/powershell/script.ps1'; getPhoneNumbers"};
        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = pb.start();
        process.waitFor();

        launch();

    }
}