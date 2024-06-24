package com.hmc.createhmcuser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Delete_Controller {

    @FXML private Button backButton;
    @FXML private Button deleteButton;
    @FXML private ListView usersListView;
    @FXML private TextField searchField;

    private ObservableList<String> userNamesList;

    public void initialize(Stage stage, Scene previousScene) {

        backButton.setOnAction(event -> {
            stage.setScene(previousScene);
        });

        setListView();
        searchListener();

    }
    
    public void setListView() {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/resources/users.json")));
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


}
