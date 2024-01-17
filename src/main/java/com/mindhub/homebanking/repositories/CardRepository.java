package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card,Long> {
    Card findByNumber(String number);

    @Query(
            "SELECT card FROM Card card " +
                    "WHERE card.number = :number AND " +
                    "card.client.id IN ( " +
                    "     SELECT client.id FROM Client client "+
                    "     WHERE client.email LIKE :email)")
    Card findByNumberAndClientEmail(@Param("number")String number, @Param("email")String email);

}
