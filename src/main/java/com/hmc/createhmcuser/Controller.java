package com.hmc.createhmcuser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class Controller {

    @FXML
    private ComboBox<String> officeComboBox;

    public void start() {
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


}

