package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Entity // crea una tabla en nuestra BD para la clase Client
public class Client {

    @Id // clave principal para la BD
    @GeneratedValue(strategy = GenerationType.IDENTITY) // designacion del tipo generacion a usar en este caso identity
    private long id;

    private String firstName, lastName, email;

    public Client() {

    }

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    } // solamente genero 1 getter porque no quiero que nadie lo pueda modificar.


    public void addAccount (Account account){
        account.setOwner(this);
        this.accounts.add(account);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
} // cierre Client
