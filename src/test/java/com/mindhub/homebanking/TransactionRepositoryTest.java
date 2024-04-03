package com.mindhub.homebanking;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dto.NewTransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.enums.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;



    private Account sourceAccount;
    private Account targetAccount;
    private Authentication authentication;


    @Test
    @Transactional
    public void testTransferMoney() {
        Client sourceClient = new Client("Fede", "Perez", "fedep@gmail.com", passwordEncoder.encode("12345"));
        Client targetClient = new Client("Gede", "Pipo", "fede3.14po@gmail.com", passwordEncoder.encode("12345"));

        clientService.saveClient(sourceClient);
        clientService.saveClient(targetClient);

        sourceAccount = new Account("VIN23451255", 5000, LocalDate.now(), AccountType.CHECKING);
        targetAccount = new Account("VIN23451256", 7500, LocalDate.now().plusDays(1),AccountType.SAVINGS);

        sourceClient.addAccount(sourceAccount);
        targetClient.addAccount(targetAccount);

        accountService.saveAccount(sourceAccount);
        accountService.saveAccount(targetAccount);

        authentication = new UsernamePasswordAuthenticationToken("fedep@gmail.com", "12345");

        NewTransactionDTO newTransactionDTO = new NewTransactionDTO(
                200.00,
                "TransactionRepositoryTestTransactional",
                "VIN23451255",
                "VIN23451256");

        ResponseEntity<String> responseEntity = transactionService.transferMoney(newTransactionDTO, authentication);

        assertEquals("Transfer successful", responseEntity.getBody());
        System.out.println("hola, termine el test");
    }

    @Test
    public void testNullTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();

        assertNotNull(transactions);
        assertThat(transactions, is(not(empty())));
    }
}
