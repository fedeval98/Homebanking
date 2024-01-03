package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/transactions")
    @Transactional
    public ResponseEntity<String> transferMoney(
            @RequestParam Double amount,
            @RequestParam String descriptions,
            @RequestParam String sourceAccountNumber,
            @RequestParam String targetAccountNumber,
            Authentication authentication){

        if(descriptions.isBlank()){
            return new ResponseEntity<>("Description cannot be blank",HttpStatus.FORBIDDEN);
        }

        if(sourceAccountNumber.isBlank() || targetAccountNumber.isBlank()){
            return new ResponseEntity<>("Source Account or Target Account cannot be blank.",HttpStatus.FORBIDDEN);
        }

        if(amount == null || amount <= 0){
            return new ResponseEntity<>("Amount cannot be blank or cero", HttpStatus.FORBIDDEN);
        }

        Account sourceAccount = accountRepository.findByNumber(sourceAccountNumber);
        Account targetAccount = accountRepository.findByNumber(targetAccountNumber);


        if(sourceAccount == null){
            return new ResponseEntity<>("Source account does not exists.", HttpStatus.FORBIDDEN);
        }

        if(targetAccount == null){
            return new ResponseEntity<>("Target account does not exists.", HttpStatus.FORBIDDEN);
        }

        if(!sourceAccount.getOwner().getEmail().equals(authentication.getName())){
            return new ResponseEntity<>("Source account does not belong to an authenticated client.", HttpStatus.FORBIDDEN);
        }

        if(sourceAccount.getBalance() < amount){
            return new ResponseEntity<>("Insufficent funds in the account",HttpStatus.BAD_REQUEST);
        }

        if(sourceAccount.equals(targetAccount)){
            return new ResponseEntity<>("Source and Target account cannot be the same", HttpStatus.FORBIDDEN);
        }

        Transaction debitTransaction = new Transaction(sourceAccount, LocalDateTime.now(),-amount, TransactionType.DEBIT,descriptions);
        Transaction creditTransaction = new Transaction(targetAccount, LocalDateTime.now(),amount, TransactionType.CREDIT,descriptions);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        sourceAccount.setBalance(sourceAccount.getBalance()- amount);
        targetAccount.setBalance(targetAccount.getBalance()+ amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        return new ResponseEntity<>("Transfer succesful",HttpStatus.OK);
    }
}
