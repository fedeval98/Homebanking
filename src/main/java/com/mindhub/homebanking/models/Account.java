package com.mindhub.homebanking.models;

import com.mindhub.homebanking.models.enums.AccountType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id // clave principal para la BD
    @GeneratedValue(strategy = GenerationType.IDENTITY) // designacion del tipo generacion a usar en este caso identity
    private Long id;

    private double balance;
    private String number;
    private LocalDate creationDate;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    public Account() {
    }

    public Account(String number, double balance, LocalDate creationDate, AccountType type) {
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
        this.type = type;
    }

    public void addTransaction (Transaction transaction){
        transaction.setAccount(this);
        this.transactions.add(transaction);
    }

    public Long getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }
} // Account class ends

