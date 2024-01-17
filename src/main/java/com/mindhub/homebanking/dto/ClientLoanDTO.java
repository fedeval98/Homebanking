package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {

    private Long id;

    private Long loanId;

    private String name;

    private double amount;

    private int payments;

    private double interest_rate;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.interest_rate = clientLoan.getLoan().getinterest_rate();
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public Long getLoanId() {
        return loanId;
    }

    public double getInterest_rate() {
        return interest_rate;
    }
}
