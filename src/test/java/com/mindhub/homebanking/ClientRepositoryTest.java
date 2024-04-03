package com.mindhub.homebanking;

import org.junit.jupiter.api.Test;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {
//    @Autowired
//    private ClientRepository clientRepository;
//
//    @Test // test to find clients by email
//    public void testFindClientByEmail() {
//        List<Client> existingClients = clientRepository.findAll();
//
//        if(existingClients.isEmpty()){
//            throw new RuntimeException("No clients found in the Data base to realize this test");
//        }
//
//        Client foundClient = clientRepository.findByEmail("melba@mindhub.com");
//
//        assertNotNull(foundClient);
//    }
//
//    @Test
//    public void testFindNullClients(){
//        List<Client> clients = clientRepository.findAll();
//
//        assertNotNull(clients);
//        assertThat(clients,is(not(empty())));
//    }
}
