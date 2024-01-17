package com.mindhub.homebanking.dto;

public class NewTransactionDTO {
    private Double amount;
    private String descriptions, sourceAccountNumber, targetAccountNumber;

    public NewTransactionDTO() {
    }

    public NewTransactionDTO(Double amount, String descriptions, String sourceAccountNumber, String targetAccountNumber) {
        this.amount = amount;
        this.descriptions = descriptions;
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }
}
