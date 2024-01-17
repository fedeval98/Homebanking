package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dto.NewTransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransactionController {

@Autowired
     private TransactionService transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<String> transferMoney(
            @RequestBody NewTransactionDTO newTransactionDTO,
            Authentication authentication){

        ResponseEntity<String> response = transactionService.transferMoney(newTransactionDTO, authentication);
        return  response;
    }

}
