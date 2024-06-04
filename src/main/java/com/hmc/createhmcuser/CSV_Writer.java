package com.hmc.createhmcuser;

import javafx.scene.control.TextField;

public class CSV_Writer {

    private String firstName;
    private String lastName;
    private String password;
    private String title;

    public void submit(String firstName, String lastName, String password, String title) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.lastName = lastName;

        System.out.println(firstName);
        System.out.println(lastName);
        System.out.println(password);
        System.out.println(title);

    }

}
