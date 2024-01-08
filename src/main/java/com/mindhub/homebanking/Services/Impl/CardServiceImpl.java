package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientService clientService;

    @Override
    public List<CardDTO> getClientCards() {
        return cardRepository.findAll() // busco todos los clientes en mi repositorio
                .stream() //convierto la lista en un Stream para poder usar operaciones intermedias (map, filter, sort, etc)
                 //o terminales (count, collect, forEach, etc)
                .map(CardDTO::new) // transformo cada client en un objeto DTO
                .collect(Collectors.toList()); // recopilo tod;
    }

    @Override
    public ResponseEntity<String> createCard(CardColor color, CardType type, Authentication authentication) {
        Client client = clientService.getAuthClient(authentication.getName());
        // obtengo los cards de los clientes, los transformo a stream, los filtro por color y tipo y los contabilizo.
              long colorTypeCount = client.getCards().stream()
                  .filter(card -> card.getColor() == color && card.getType() == type)
                  .count();

        //  obtengo los cards de los clientes, los transformo a stream, los filtro por color y los contabilizo.
              long colorCount = client.getCards().stream()
                  .filter(card -> card.getColor() == color)
                  .count();

        //  obtengo los cards de los clientes, los transformo a stream, los filtro por tipo y los contabilizo.
              long typeCount = client.getCards().stream()
                  .filter(card -> card.getType() == type)
                  .count();

        //  Verifico si ya se tiene un tipo del card seleccionado y si ya existen 3, devuelvo un error.
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

        //    Verifico si ya se tiene un maximo de 6 cards y si se cumple, devuelvo un error.
          if (client.getCards().size() >= 6) {
              return new ResponseEntity<>("You have reached the maximum limit of 6 cards", HttpStatus.FORBIDDEN);
  }
        // generador automatico, random, de CVV
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
