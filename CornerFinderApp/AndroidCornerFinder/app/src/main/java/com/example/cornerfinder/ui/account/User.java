package com.example.cornerfinder.ui.account;

public class User {
    public String username, email, birthdate;

    public User(){}

    public User(String username, String email, String birthdate) {
        this.username = username;
        this.email = email;
        this.birthdate = birthdate;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthdate() {
        return birthdate;
    }
}
