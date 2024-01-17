package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dto.NewTransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    ResponseEntity<String> transferMoney(NewTransactionDTO newTransactionDTO, Authentication authentication);

    List<Transaction> findByAccountIdAndDateTimeBetween(Long id,LocalDateTime dateTime, LocalDateTime endDate);

    List<Transaction> getTransactionsByAccount(Account account, LocalDateTime startDate, LocalDateTime endDate, Authentication authentication);

    void saveTransaction(Transaction transaction);
}
