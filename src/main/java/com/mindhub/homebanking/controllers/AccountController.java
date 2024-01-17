package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.PDFService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PDFService pdfService;


    @GetMapping("/accounts")
    public List<AccountDTO> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){

        Client client = clientService.getAuthClient(authentication.getName());

        Account account = accountService.findByClientAndId(client, id);

        if (account != null) {
            AccountDTO accountDTO = accountService.getAccountById(id);
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Account not found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<TransactionDTO> getAccountTransactions(@PathVariable Long id) {
        return accountService.getAccountTransactions(id);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<String> createAccount(@RequestParam AccountType type, Authentication authentication){
        Client client = clientService.getAuthClient(authentication.getName());
        if(client.getAccounts().stream().filter(Account::isActive).collect(Collectors.toList()).size()>=3){
            return new ResponseEntity<>("You reach the maximum limit of 3 accounts per client",HttpStatus.FORBIDDEN);
        }
        String number;
        do {
            number = "VIN" + getAccountNumber(00000000,99999999);
        }while (accountService.existsByNumber(number));

        Account account = new Account(number,0, LocalDate.now(),type);
        client.addAccount(account);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Client account created", HttpStatus.CREATED);
    }

    @PostMapping("/clients/current/accounts/first")
    public ResponseEntity<String> createAccountFirst(@RequestParam AccountType type, Client client){
        if(client.getAccounts().size()>=3){
            return new ResponseEntity<>("You reach the maximum limit of 3 accounts per client",HttpStatus.FORBIDDEN);
        }
        String number;
        do {
            number = "VIN" + getAccountNumber(00000000,99999999);
        }while (accountService.existsByNumber(number));

        Account account = new Account(number,0, LocalDate.now(),type);
        client.addAccount(account);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Client account created", HttpStatus.CREATED);
    }
    // PathVariable es una notacion que nos permite variar la ruta para asi matchearla con el ID del cliente que llegue.
    public int getAccountNumber(int min, int max){
        return (int) ((Math.random())*(max-min)+min);
    }

    @PatchMapping("/clients/current/account/remove")
    public ResponseEntity<String> hideAccountAndTransactions(@RequestParam Long id, Authentication authentication){
        ResponseEntity<String> response = accountService.findByClientEmailAndId(authentication.getName(),id);
        return response;
    }

    @GetMapping("/accounts/{id}/transactions/pdf")
    public ResponseEntity<InputStreamResource> downloadTransactionsPDF(@PathVariable Long id,
                                                                       @RequestParam LocalDateTime dateTime,
                                                                       @RequestParam LocalDateTime endDate,
                                                                       Authentication authentication) throws IOException {
        AccountDTO account = accountService.getAccountById(id);
        List<Transaction> transactions = transactionService.findByAccountIdAndDateTimeBetween(id,dateTime,endDate);

        ByteArrayInputStream pdf = pdfService.generatePdf(transactions,account);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","inline; filename=transactions.pdf");

        return  ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }
}

//NOTA: forEach se puede utilizar en LIST sin necesidad de hacer un .stream primero.

