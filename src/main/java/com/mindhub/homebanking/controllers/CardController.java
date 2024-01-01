package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController // notacion para escuchar y responde peticiones bajo los lineamietos REST.
// Que es escuchar peticiones HTTP usando metodos HTTP (GET, PUT, PATCH, POST, ETC)
// y responder en formato JSON/XML
@RequestMapping("/api") // ruta a escuchar / responder
public class CardController {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<String> createCard(
            @RequestParam CardColor color,
            @RequestParam CardType type,
            Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        // obtengo los cards de los clientes, los transformo a stream, los filtro por color y tipo y los contabilizo.
        long colorTypeCount = client.getCards().stream()
                .filter(card -> card.getColor() == color && card.getType() == type)
                .count();

        // obtengo los cards de los clientes, los transformo a stream, los filtro por color y los contabilizo.
        long colorCount = client.getCards().stream()
                .filter(card -> card.getColor() == color)
                .count();

        // obtengo los cards de los clientes, los transformo a stream, los filtro por tipo y los contabilizo.
        long typeCount = client.getCards().stream()
                .filter(card -> card.getType() == type)
                .count();

        // Verifico si ya se tiene un tipo del card seleccionado y si ya existen 3, devuelvo un error.
        if (typeCount >= 3) {
            return new ResponseEntity<>("You already have 3 cards of type " + type, HttpStatus.FORBIDDEN);
        }
        // Verifico si ya se tiene un color y un tipo del card seleccionado y si ya existe 1, devuelvo un error.
        if (colorTypeCount >= 1) {
            return new ResponseEntity<>("You already have a card of color " + color + " and type " + type, HttpStatus.FORBIDDEN);
        }
        // Verifico si ya se tiene un color del card seleccionado y si ya existen 2, devuelvo un error.
        if (colorCount >= 2) {
            return new ResponseEntity<>("You already have a card of color " + color, HttpStatus.FORBIDDEN);
        }

        // Verifico si ya se tiene un maximo de 6 cards y si se cumple, devuelvo un error.
        if (client.getCards().size() >= 6) {
            return new ResponseEntity<>("You have reached the maximum limit of 6 cards", HttpStatus.FORBIDDEN);
        }
        //generador automatico, random, de CVV
        int cvv = (int) (Math.random() * 900 + 100);

        String cardNumber = generateRandomCardNumber();

        String cardholder = client.getFirstName() + " " + client.getLastName();

        LocalDate startDate = LocalDate.now();
        LocalDate expirationDate = startDate.plusYears(5);

        Card card = new Card(cardholder, type, color, cardNumber, cvv, startDate, expirationDate);

        client.addCard(card);
        cardRepository.save(card);

        return new ResponseEntity<>("Card created for the client", HttpStatus.CREATED);
    }

    private String generateRandomCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int section = (int) (Math.random() * 9000 + 1000);
            cardNumber.append(section).append("-");
        }
        return cardNumber.substring(0, cardNumber.length() - 1);
    }
}



