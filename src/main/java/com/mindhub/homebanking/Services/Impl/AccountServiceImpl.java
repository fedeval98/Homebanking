package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {


    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll() //busco todos los clientes en mi repositorio
                .stream()// convierto la lista en un Stream para poder usar operaciones intermedias (map, filter, sort, etc)
                // o terminales (count, collect, forEach, etc)
                .filter(Account::isActive)
                .map(AccountDTO::new) // transformo cada client en un objeto DTO
                .collect(Collectors.toList()); //recopilo todos los objetos DTO y los transforma a una lista.
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null); // aca vamos a buscar por ID pero nos devuelve o un cliente o NULL
    }

    @Override
    public List<TransactionDTO> getAccountTransactions(Long id) {
        return accountRepository.findById(id)
                    .filter(Account::isActive)
                    .map(account -> account.getTransactions().stream()
                    .map(TransactionDTO::new)
                    .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
    }

    @Override
    public boolean existsByNumber(String number) {
        return accountRepository.existsByNumber(number);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account findByClient(Client client) {
        return accountRepository.findByClient(client);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public Account findByClientAndId(Client client, Long id){
        return accountRepository.findByClientAndId(client,id);

    }

    @Override
    public boolean existsByClientAndNumber(Client client, String accountNumber) {
        return accountRepository.existsByClientAndNumber(client,accountNumber);
    }

    @Override
    public ResponseEntity<String> findByClientEmailAndId(String email, Long id) {
       Account account = accountRepository.findByClientEmailAndId(email,id);

       if (account == null){
           return new ResponseEntity<String>("Client or Account not found or you are not the owner", HttpStatus.BAD_REQUEST);
       }
       if(account.getBalance() != 0){
           return new ResponseEntity<>("You can not delete accounts with balance.",HttpStatus.FORBIDDEN);
       }

       if(!account.isActive()){
           return new ResponseEntity<>("Account is already inactive", HttpStatus.OK);
       }

       if(account.getClient().getAccounts().size() == 1){
           return new ResponseEntity<>("Cannot delete sole account. Please set up an alternative account before proceeding.",HttpStatus.FORBIDDEN);
       }
       account.setActive(false);
       accountRepository.save(account);

       return new ResponseEntity<>("Account remove successfully", HttpStatus.OK);
    }

}
