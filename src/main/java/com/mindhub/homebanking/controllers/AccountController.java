package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.TransactionDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api")
public class AccountController {

    // Inyeccion de dependencia

    @Autowired //Se encarga de realizar la inyeccion por construccion pero de forma automatica
    // hace algo SIMILAR a instanciar la clase del objeto que ejecuta
    private AccountRepository accountRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAllAccounts(){
        return accountRepository.findAll() //busco todos los clientes en mi repositorio
                .stream()// convierto la lista en un Stream para poder usar operaciones intermedias (map, filter, sort, etc)
                // o terminales (count, collect, forEach, etc)
                .map(AccountDTO::new) // transformo cada client en un objeto DTO
                .collect(Collectors.toList()); //recopilo todos los objetos DTO y los transforma a una lista.
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null); // aca vamos a buscar por ID pero nos devuelve o un cliente o NULL
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<TransactionDTO> getAccountTransactions(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(account -> account.getTransactions().stream()
                        .map(TransactionDTO::new)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
    // PathVariable es una notacion que nos permite variar la ruta para asi matchearla con el ID del cliente que llegue.
}

//NOTA: forEach se puede utilizar en LIST sin necesidad de hacer un .stream primero.

