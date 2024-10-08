package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double maxAmount;

    @ElementCollection
    private List<Integer> payments; // aca quiero que mis prestamos tengan multiples valores en formato int.

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    private double interest_rate;

    public Loan() {
    }

    public Loan(String name, double maxAmount, List<Integer> payments, double interest_rate) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interest_rate = interest_rate;
    }

    public void ClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        this.clientLoans.add(clientLoan);
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

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public double getinterest_rate() {
        return interest_rate;
    }

    public void setinterest_rate(double interest_rate) {
        this.interest_rate = interest_rate;
    }
}
