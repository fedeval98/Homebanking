package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.dto.CardPaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.models.enums.CardColor;
import com.mindhub.homebanking.models.enums.CardType;
import com.mindhub.homebanking.models.enums.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardService {

    List<CardDTO> getClientCards ();

    ResponseEntity<String> createCard(CardColor color, CardType type, Authentication authentication);

    ResponseEntity<?> hideCard(Status status, String number, Authentication authentication);

    Card findByNumberAndClientEmail(String number, String email);

    ResponseEntity<String> cardPayment(CardPaymentDTO cardPaymentDTO);
}
