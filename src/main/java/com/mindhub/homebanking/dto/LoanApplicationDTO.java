package com.mindhub.homebanking.dto;

public class LoanApplicationDTO {

    private Long loanId;

    private String accountNumber;

    private double amount;

    private int payments;

    public Long getLoanId() {
        return loanId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

}
