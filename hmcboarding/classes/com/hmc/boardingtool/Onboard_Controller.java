package com.hmc.boardingtool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Onboard_Controller {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField passwordField;
    @FXML private TextField titleField;
    @FXML private ComboBox<String> officeComboBox;
    @FXML private ComboBox<String> numberComboBox;
    @FXML private Button backButton;
    @FXML private Button submitButton;

    private Scene previousScene;
    private Map<String, List<String>> officePhoneNumbers;
    private CSV_Writer csv_writer;

    @FXML
    public void initialize(Stage stage, Scene previousScene) throws IOException {
        setOfficeComboBox();
        loadPhoneNumbers();

        // event listener for submit button
        csv_writer = new CSV_Writer();
        submitButton.setOnAction(event -> {
            try {
                csv_writer.submit(firstNameField.getText(), lastNameField.getText(),
                        passwordField.getText(), titleField.getText(), officeComboBox.getValue(), numberComboBox.getValue(),
                        (Stage) submitButton.getScene().getWindow());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        backButton.setOnAction(event -> {
            stage.setScene(previousScene);
        });
    }

    public void setOfficeComboBox() {
        ObservableList<String> offices = FXCollections.observableArrayList(
                "Ontario 01",
                "Ontario 05",
                "Los Angeles",
                "Sacramento",
                "San Jose",
                "San Diego",
                "San Francisco"
        );
        officeComboBox.setItems(offices);
    }

    public void loadPhoneNumbers() throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get("./classes/phone_numbers.json")));
        JSONObject jsonObject = new JSONObject(jsonContent);
        JSONArray jsonArray = jsonObject.getJSONArray("TelephoneNumbers");

        officePhoneNumbers = new HashMap<>();

        // loop through the jsonArray and add phone numbers
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject phoneNumberObject = jsonArray.getJSONObject(i);
            String phoneNumber = phoneNumberObject.getString("TelephoneNumber");
            String office = getOffice(phoneNumber);

            // hash phone numbers by office
            officePhoneNumbers.putIfAbsent(office, new ArrayList<>());
            officePhoneNumbers.get(office).add(phoneNumber);
        }

        // add event listener to office combo box
        officeComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            // If the selected office is ONT01 or ONT05, set "Ontario" to key. Else, use the selected office as the key
            String key = (newValue.equals("Ontario 01") || newValue.equals("Ontario 05")) ? "Ontario" : newValue;

            // if the map contains the selected office, create a list of phone numbers
            if (officePhoneNumbers.containsKey(key)) {
                List<String> phoneNumbers = officePhoneNumbers.get(key);

                // convert list to observable list
                ObservableList<String> observablePhoneNumbers = FXCollections.observableArrayList(phoneNumbers);

                numberComboBox.setItems(observablePhoneNumbers);

                // sets combobox height
                numberComboBox.show();
                numberComboBox.hide();
            }
        });
    }

    public String getOffice(String phoneNumber) {
        String prefix = phoneNumber.substring(0, 4);
        if (prefix.equals("1916") || prefix.equals("1530")) { return "Sacramento";
        } else if (prefix.equals("1213") || prefix.equals("1562")) { return "Los Angeles";
        } else if (prefix.equals("1619")) { return "San Diego";
        } else if (prefix.equals("1909") || prefix.equals("1951")) { return "Ontario";
        } else if (prefix.equals("1408")) { return "San Jose";
        } else if (prefix.equals("1628")) { return "San Francisco";
        } else {
            return "Unknown";
        }
    }



}

