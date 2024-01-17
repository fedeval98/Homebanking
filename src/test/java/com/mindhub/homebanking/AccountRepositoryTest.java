package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Authentication authentication;
    @Test // create client and account and find in the repository
    @Transactional
    public void testExistByNumber(){

        Client client = new Client("Fede", "Perez","perezf@gmail.com",passwordEncoder.encode("12345"));
        clientRepository.save(client);

        Account account = new Account("VIN7492392", 0, LocalDate.now());
        client.addAccount(account);
        accountRepository.save(account);

        authentication = new UsernamePasswordAuthenticationToken("perezf@gmail.com", "12345");

        boolean exists = accountRepository.existsByNumber(account.getNumber());

        assertTrue(exists);
    }

    @Test
    public void testFindClientsAndAccountsNull(){
        List<Client> clients = clientRepository.findAll();
        List<Account> accounts = accountRepository.findAll();

        assertNotNull(clients);
        assertNotNull(accounts);

        assertThat(clients,is(not(empty())));
        assertThat(accounts,is(not(empty())));
    }

}
