package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Account {

    @Id // clave principal para la BD
    @GeneratedValue(strategy = GenerationType.IDENTITY) // designacion del tipo generacion a usar en este caso identity
    private long id;

    private double balance;
    private String number;
    private LocalDate creationDate;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client owner;

    public Account() {
    }

    public Account(String number, double balance, LocalDate creationDate) {
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
    }

    public long getId() {
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

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }
} // Account class ends

