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
            List<String> lines = Files.readAllLines(Paths.get("output.csv"));

            StringBuilder sb = new StringBuilder();
            sb.append(firstName);
            sb.append(',');
            sb.append(lastName);
            sb.append(',');
            sb.append(firstName + ' ' + lastName);
            sb.append(',');
            sb.append(firstName.charAt(0) + lastName);
            sb.append(',');
            sb.append(password);
            sb.append(',');
            sb.append(title);
            sb.append(',');
            sb.append(firstName + '.' + lastName + "@hmcarchitects.com");
            sb.append(',');
            sb.append(firstName + '.' + lastName + "@hmcarchitects.com");
            sb.append(',');
            sb.append(number);

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
