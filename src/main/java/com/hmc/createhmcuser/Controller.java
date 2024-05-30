package com.hmc.createhmcuser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Controller {

    @FXML
    private ComboBox<String> officeComboBox;

    @FXML
    private ComboBox<Integer> numberComboBox;

    private Map<String, List<Integer>> officePhoneNumbers;

    public void initialize() throws IOException {
        setOfficeComboBox();
        loadPhoneNumbers();

    }

    public void setOfficeComboBox() {
        ObservableList<String> offices = FXCollections.observableArrayList(
                "Ontario",
                "Los Angeles",
                "Sacramento",
                "San Jose",
                "San Diego",
                "San Francisco"
        );
        officeComboBox.setItems(offices);
    }

    public void loadPhoneNumbers() throws IOException {

        String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/resources/phone_numbers.json")));
        System.out.println(jsonContent);
    }

    public String getOffice(String phoneNumber) {
        String prefix = phoneNumber.substring(0, 3);
        switch (prefix) {
            case "1916":
                return "Sacramento";
            default:
                return "Unknown";
        }
    }





}

