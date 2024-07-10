package com.hmc.boardingtool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;


public class Offboard_Controller {

    @FXML private Button backButton;
    @FXML private Button submitButton;
    @FXML private ListView usersListView;
    @FXML private TextField searchField;

    private String fullName;
    private ObservableList<String> userNamesList;

    public void initialize(Stage stage, Scene previousScene) {

        backButton.setOnAction(event -> {
            stage.setScene(previousScene);
        });

        setListView();
        searchListener();
        submit();

    }

    public void setListView() {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("./classes/users.json")));
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONArray userNamesArray = jsonObject.getJSONArray("UserNames");

            userNamesList = FXCollections.observableArrayList();
            for (int i = 0; i < userNamesArray.length(); i++) {
                userNamesList.add(userNamesArray.getString(i));
            }

            usersListView.setItems(userNamesList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void searchListener() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<String> filteredList = new FilteredList<>(userNamesList, s -> s.toLowerCase().contains(newValue.toLowerCase()));
            usersListView.setItems(filteredList);
        });
    }

    public void submit() {
        submitButton.setOnAction(event -> {
            fullName = (String) usersListView.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to offboard " + fullName + "?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                String[] command = {"powershell.exe", "-ExecutionPolicy", "Bypass", "-Command", ". './classes/script.ps1'; offboardUser '" + fullName + "'"};
                ProcessBuilder pb = new ProcessBuilder(command);
                try {
                    Process process = pb.start();

                    // Read the output
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    // Read the errors
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    while ((line = errorReader.readLine()) != null) {
                        System.err.println(line);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


}
