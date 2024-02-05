package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.enums.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;

    private LocalDateTime dateTime;

    private double amount,previousAmount,currentAmount;
    private String description;

    private final TransactionType type;


    public TransactionDTO(Transaction transaction){
        id = transaction.getId();
        dateTime = transaction.getDateTime();
        amount = transaction.getAmount();
        type = transaction.getType();
        description = transaction.getDescription();
        previousAmount = transaction.getPreviousBalance();
        currentAmount = previousAmount+ transaction.getAmount();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPreviousAmount() {
        return previousAmount;
    }

    public void setPreviousAmount(double previousAmount) {
        this.previousAmount = previousAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
} // finaliza TransactionDTO
