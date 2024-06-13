package com.hmc.createhmcuser;


import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CSV_Writer {

    private String firstName;
    private String lastName;
    private String password;
    private String title;
    private String office;
    private String number;
    private Stage loadingStage;


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
            alert.showAndWait();
        } else if (password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*\\W.*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Password must be at least 8 characters with at least 1 number and 1 symbol.");
            alert.showAndWait();
        } else {
            // Hide the current view
            currentStage.close();
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

        // create list of content in the csv file
        List<String> lines = Files.readAllLines(Paths.get("output.csv"));
        if (lines.size() > 1) { lines.set(1, sb.toString());
        } else { lines.add(sb.toString()); }

        Files.write(Paths.get("output.csv"), lines);

    }

    public void callScript() throws IOException, InterruptedException {
        String[] command = {"cmd.exe", "/k", "start", "powershell.exe", "-ExecutionPolicy", "Bypass", "-File", "src/main/powershell/script.ps1"};

        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = pb.start();

        process.waitFor();
    }


}

