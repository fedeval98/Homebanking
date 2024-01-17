package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.models.*;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardService {

    List<CardDTO> getClientCards ();

    ResponseEntity<String> createCard(CardColor color, CardType type, Authentication authentication);

    ResponseEntity<?> hideCard(Status status, String number, Authentication authentication);

    Card findByNumber(String number);

    Card findByNumberAndClientEmail(String number, String email);
}
