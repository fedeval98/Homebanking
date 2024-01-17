package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.AccountType;

public class newClient {

    private String firstName, lastName, email, password;
    private AccountType type;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public AccountType getType() {
        return type;
    }
}
