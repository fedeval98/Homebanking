package com.mindhub.homebanking.models;

import com.mindhub.homebanking.models.enums.RoleType;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity // crea una tabla en nuestra BD para la clase Client
public class Client {

    @Id // clave principal para la BD =/= a clave foranea (cuando esta en otra tabla, para relaciones)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // designacion del tipo generacion a usar en este caso identity
    private Long id; //aca uso long en vez de Long(wrapper) porque necesito que el cliente SIEMPRE tenga un id, no puede ser NULL.

    private String firstName, lastName, email,password;

    @Enumerated(EnumType.STRING)
    private RoleType role = RoleType.CLIENT;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER) // mapped by indica el dueño
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set <ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client() {

    }

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void addAccount (Account account){
        account.setClient(this);
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

    public Long getId() {
        return id;
    } // solamente genero 1 getter porque no quiero que nadie lo pueda modificar.

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
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
