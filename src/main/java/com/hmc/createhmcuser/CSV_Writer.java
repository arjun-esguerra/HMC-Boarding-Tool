package com.hmc.createhmcuser;

import java.io.FileNotFoundException;
import java.io.FileWriter;
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

    public void submit(String firstName, String lastName, String password, String title, String office, String number) throws FileNotFoundException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.title = lastName;
        this.office = office;
        this.number = number;

        try {
            // build string for content in csv file
            StringBuilder sb = new StringBuilder()
            .append(firstName).append(',')
            .append(lastName).append(',')
            .append(firstName + ' ' + lastName).append(',')
            .append(firstName.charAt(0) + lastName).append(',')
            .append(password).append(',')
            .append(title).append(',')
            .append(firstName + '.' + lastName + "@hmcarchitects.com").append(',')
            .append(firstName + '.' + lastName + "@hmcarchitects.com").append(',')
            .append(number);

            // create list of content in the csv file
            List<String> lines = Files.readAllLines(Paths.get("output.csv"));
            if (lines.size() > 1) {
                lines.set(1, sb.toString());
            } else {
                lines.add(sb.toString());
            }
            Files.write(Paths.get("output.csv"), lines);

            System.out.println(sb);

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the CSV file");
            e.printStackTrace();
        }

    }

}
