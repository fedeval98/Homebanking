package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity // crea una tabla en nuestra BD para la clase Client
public class Client {

    @Id // clave principal para la BD
    @GeneratedValue(strategy = GenerationType.IDENTITY) // designacion del tipo generacion a usar en este caso identity
    private long id; //aca uso long en vez de Long(wrapper) porque necesito que el cliente SIEMPRE tenga un id, no puede ser NULL.

    private String firstName, lastName, email;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set <ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client() {

    }

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void addAccount (Account account){
        account.setOwner(this);
        this.accounts.add(account);
    }

    public void addClientLoan (ClientLoan clientLoan){
        clientLoan.setClient(this);
        this.clientLoans.add(clientLoan);
    }

    public void addCard (Card card){
        card.setOwner(this);
        this.cards.add(card);
    }

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

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
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
