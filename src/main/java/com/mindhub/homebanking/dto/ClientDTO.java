package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Client;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    //Con esto creo los DATA para transferir mis Objects, para lo cual creo un constructor que obtenga los datos
    // De mi objecto Client para poder enviarlo a mi ClientController y poder visualizar mi objeto.

    private Long id;
    private String firstName, lastName, email;

    private Set<AccountDTO> accounts;

    public ClientDTO(Client client){
        id = client.getId();
        firstName = client.getFirstName();
        lastName = client.getLastName();
        email = client.getEmail();
        accounts = client.getAccounts()
                .stream()// convierto la lista en un Stream para poder usar operaciones intermedias (map, filter, sort, etc)
                // o terminales (count, collect, forEach, etc)
                .map(account -> new AccountDTO(account)) // transformo cada account en un objeto DTO
                .collect(Collectors.toSet()); //recopilo todos los objetos DTO y los transforma a una lista.
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

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
}
