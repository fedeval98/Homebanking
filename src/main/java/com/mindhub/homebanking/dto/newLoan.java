package com.mindhub.homebanking.dto;

import jakarta.persistence.ElementCollection;

import java.util.List;

public class newLoan {

    private String name;

    private double maxAmount,interest_rate;

    @ElementCollection
    private List<Integer> payments;

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public double getInterest_rate() {
        return interest_rate;
    }
}
