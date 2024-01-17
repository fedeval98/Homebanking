package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.ResponseEntity;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByNumber(String number);
    Account findByNumber (String number);

    Account findByClientAndId(Client client, Long id);

    Account findByClientEmailAndId(String email, Long id);

    boolean existsByClientAndNumber(Client client, String accountNumber);

    Account findByClient(Client client);

}
