package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;

    private double balance;

    private String number;

    private LocalDate creationDate;

    private Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO(Account account){
        id = account.getId();
        balance = account.getBalance();
        number = account.getNumber();
        creationDate = account.getCreationDate();
        this.transactions = account.getTransactions().stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toSet());
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

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}
