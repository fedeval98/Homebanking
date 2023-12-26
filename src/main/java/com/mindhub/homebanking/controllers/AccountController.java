package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@RestController // notacion para escuchar y responde peticiones bajo los lineamietos REST.
// Que es escuchar peticiones HTTP usando metodos HTTP (GET, PUT, PATCH, POST, ETC)
// y responder en formato JSON/XML
@RequestMapping("/api") // ruta a escuchar / responder
public class AccountController {

    // Inyeccion de dependencia

    @Autowired //Se encarga de realizar la inyeccion por construccion pero de forma automatica
    // hace algo SIMILAR a instanciar la clase del objeto que ejecuta
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

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

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<String> createAccount(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        if(client.getAccounts().size()>=3){
            return new ResponseEntity<>("You reach the maximum limit of 3 accounts per client",HttpStatus.FORBIDDEN);
        }
        String number;
        do {
            number = "VIN" + getAccountNumber(00000000,99999999);
        }while (accountRepository.existsByNumber(number));

        Account account = new Account(number,0, LocalDate.now());
        client.addAccount(account);
        accountRepository.save(account);

        return new ResponseEntity<>("Client account created", HttpStatus.CREATED);
    }
    // PathVariable es una notacion que nos permite variar la ruta para asi matchearla con el ID del cliente que llegue.
    public int getAccountNumber(int min, int max){
        return (int) ((Math.random())*(max-min)+min);
    }
}

//NOTA: forEach se puede utilizar en LIST sin necesidad de hacer un .stream primero.

