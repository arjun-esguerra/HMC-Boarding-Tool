package com.hmc.boardingtool;


import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSV_Writer {

    private String firstName;
    private String lastName;
    private String password;
    private String title;
    private String office;
    private String number;

    public void submit(String firstName, String lastName, String password, String title, String office, String number, Stage currentStage) throws IOException, InterruptedException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.title = title;
        this.office = office;
        this.number = number;

        // check if any field is empty
        if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || title.isEmpty() || office == null|| number == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields must be filled out!");
            alert.getDialogPane().setStyle("-fx-font-size: 20px;");
            alert.showAndWait();
        } else if (password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*\\W.*") || !password.matches(".*[A-Z].*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Password must be at least 8 characters with at least 1 uppercase letter, 1 number, and 1 symbol.");
            alert.getDialogPane().setStyle("-fx-font-size: 20px;");
            alert.showAndWait();
        } else {
            // Hide the current view
            writeCSV();
            callScript();
        }
    }

    public void writeCSV() throws IOException {
        // build string for content in csv file
        StringBuilder sb = new StringBuilder()
                .append(firstName).append(',')
                .append(lastName).append(',')
                .append(firstName + ' ' + lastName).append(',')
                .append(firstName.charAt(0) + lastName).append(',')
                .append(password).append(',')
                .append(title).append(',')
                .append(firstName + '.' + lastName + "@hmcarchitects.com").append(',')
                .append(office).append(',')
                .append(number);

        // Create a path to the file
        Path filePath = Paths.get("./classes/output.csv");

        // create list of content in the csv file
        List<String> lines = Files.exists(filePath) ? Files.readAllLines(filePath) : new ArrayList<>();
        if (lines.size() > 1) {
            lines.set(1, sb.toString());
        } else {
            lines.add(sb.toString());
        }
        Files.write(filePath, lines);
    }

    public void callScript() throws IOException {
        String[] command = {"cmd.exe", "/k", "start", "cmd.exe", "/k", "powershell.exe", "-ExecutionPolicy", "Bypass", "-Command", ". './classes/script.ps1'; onboardUser"};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.start();
    }

}

