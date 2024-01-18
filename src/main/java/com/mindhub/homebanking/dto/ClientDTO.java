package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Status;

import java.util.List;
import java.util.stream.Collectors;

public class ClientDTO {

    //Con esto creo los DATA para transferir mis Objects, para lo cual creo un constructor que obtenga los datos
    // De mi objecto Client para poder enviarlo a mi ClientController y poder visualizar mi objeto.

    private Long id;
    private String firstName, lastName, email;

    private List<AccountDTO> accounts;

    private List<ClientLoanDTO> loans;

    private List<CardDTO> cards;

    private List<String> loanNames;

    public ClientDTO(Client client) {
        id = client.getId();
        firstName = client.getFirstName();
        lastName = client.getLastName();
        email = client.getEmail();
        accounts = client.getAccounts()
                .stream()// convierto la lista en un Stream para poder usar operaciones intermedias (map, filter, sort, etc)
                // o terminales (count, collect, forEach, etc)
                .filter(Account::isActive)
                .map(AccountDTO::new) // transformo cada account en un objeto DTO
                .collect(Collectors.toList()); //recopilo todos los objetos DTO y los transforma a una lista.
        loans = client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toList());
        cards = client.getCards().stream().filter(card -> card.getStatus().equals(Status.ACTIVE)).map(CardDTO::new).collect(Collectors.toList());
        loanNames = client.getClientLoans().stream().map(clientLoan -> clientLoan.getLoan().getName()).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<AccountDTO> getAccounts() {
        return accounts;
    }

    public List<ClientLoanDTO> getLoans() {
        return loans;
    }

    public List<CardDTO> getCards() {
        return cards;
    }

}
