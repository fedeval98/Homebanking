package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dto.NewTransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional
    public ResponseEntity<String> transferMoney(NewTransactionDTO newTransactionDTO,
                                                Authentication authentication){

            if(newTransactionDTO.getDescriptions().isBlank()){
        return new ResponseEntity<>("Description cannot be blank", HttpStatus.FORBIDDEN);
    }

        if(newTransactionDTO.getSourceAccountNumber().isBlank() || newTransactionDTO.getTargetAccountNumber().isBlank()){
        return new ResponseEntity<>("Source Account or Target Account cannot be blank.",HttpStatus.FORBIDDEN);
    }

        if(newTransactionDTO.getAmount() == null || newTransactionDTO.getAmount() <= 0){
        return new ResponseEntity<>("Amount cannot be blank or cero", HttpStatus.FORBIDDEN);
    }

    Account sourceAccount = accountService.findByNumber(newTransactionDTO.getSourceAccountNumber());
    Account targetAccount = accountService.findByNumber(newTransactionDTO.getTargetAccountNumber());


        if(sourceAccount == null){
        return new ResponseEntity<>("Source account does not exists.", HttpStatus.FORBIDDEN);
    }

        if(targetAccount == null){
        return new ResponseEntity<>("Target account does not exists.", HttpStatus.FORBIDDEN);
    }

        if(!sourceAccount.getClient().getEmail().equals(authentication.getName())){
        return new ResponseEntity<>("Source account does not belong to an authenticated client.", HttpStatus.FORBIDDEN);
    }

        if(sourceAccount.getBalance() < newTransactionDTO.getAmount()){
        return new ResponseEntity<>("Insufficent funds in the account",HttpStatus.BAD_REQUEST);
    }

        if(sourceAccount.equals(targetAccount)){
        return new ResponseEntity<>("Source and Target account cannot be the same", HttpStatus.FORBIDDEN);
    }

    Transaction debitTransaction = new Transaction(sourceAccount, LocalDateTime.now(),-newTransactionDTO.getAmount(), TransactionType.DEBIT, newTransactionDTO.getDescriptions());
    Transaction creditTransaction = new Transaction(targetAccount, LocalDateTime.now(), newTransactionDTO.getAmount(), TransactionType.CREDIT, newTransactionDTO.getDescriptions());

        Double sourcePreviousBalance = sourceAccount.getBalance();
        Double sourceCurrentBalance = sourcePreviousBalance - newTransactionDTO.getAmount();

        Double targetPreviousBalance = targetAccount.getBalance();
        Double targetCurrentBalance = targetPreviousBalance + newTransactionDTO.getAmount();

        debitTransaction.setPreviousBalance(sourcePreviousBalance);
        debitTransaction.setCurrentBalance(sourceCurrentBalance);

        creditTransaction.setPreviousBalance(targetPreviousBalance);
        creditTransaction.setCurrentBalance(targetCurrentBalance);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        sourceAccount.setBalance(sourceCurrentBalance);
        targetAccount.setBalance(targetCurrentBalance);

        accountService.saveAccount(sourceAccount);
        accountService.saveAccount(targetAccount);

        return new ResponseEntity<>("Transfer successful",HttpStatus.OK);
    }

    @Override
    public List<Transaction> findByAccountIdAndDateTimeBetween(Long id,LocalDateTime dateTime, LocalDateTime endDate) {
        return transactionRepository.findByAccountIdAndDateTimeBetween(id,dateTime,endDate);
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
