package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;

    private LocalDateTime dateTime;

    private double amount;

    private String description;

    private final TransactionType type;

    public TransactionDTO(Transaction transaction){
        id = transaction.getId();
        dateTime = transaction.getDateTime();
        amount = transaction.getAmount();
        type = transaction.getType();
        description = transaction.getDescription();
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
} // finaliza TransactionDTO
