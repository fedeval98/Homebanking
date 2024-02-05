package com.mindhub.homebanking.models;

import com.mindhub.homebanking.models.enums.TransactionType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;

    private double amount;

    @Enumerated (EnumType.STRING)
    private TransactionType type;

    private String description;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private double previousBalance, currentBalance;

    public Transaction() {
    }

    public Transaction(Account account, LocalDateTime dateTime, double amount, TransactionType type, String description) {
        this.account = account;
        this.dateTime = dateTime;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public Transaction(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(double previousBalance) {
        this.previousBalance = previousBalance;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
} // finaliza Transaction
