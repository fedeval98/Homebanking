package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;

public class AccountDTO {
    private long id;

    private double balance;
    private String number;
    private LocalDate creationDate;

    public AccountDTO(Account account){
        id = account.getId();
        balance = account.getBalance();
        number = account.getNumber();
        creationDate = account.getCreationDate();
    }

    public long getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

}
