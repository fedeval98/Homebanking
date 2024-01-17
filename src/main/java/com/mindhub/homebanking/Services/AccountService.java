package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAllAccounts();

    AccountDTO getAccountById(Long id);

    List<TransactionDTO> getAccountTransactions(Long id);

    boolean existsByNumber(String number);

    void saveAccount(Account account);

    Account findByClient(Client client);

    Account findByNumber (String number);

    Account findByClientAndId(Client client, Long id);

    boolean existsByClientAndNumber(Client client, String accountNumber);

    ResponseEntity<String> findByClientEmailAndId(String email, Long id);

}