package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.dto.CardPaymentDTO;
import com.mindhub.homebanking.models.enums.CardColor;
import com.mindhub.homebanking.models.enums.CardType;
import com.mindhub.homebanking.models.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // notacion para escuchar y responde peticiones bajo los lineamietos REST.
// Que es escuchar peticiones HTTP usando metodos HTTP (GET, PUT, PATCH, POST, ETC)
// y responder en formato JSON/XML
@RequestMapping("/api") // ruta a escuchar / responder
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/clients/current/cards")
    public ResponseEntity<List<CardDTO>> getClientCards(){
        List<CardDTO> cardDTOList = cardService.getClientCards();
        return ResponseEntity.ok(cardDTOList);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<String> createCard(
            @RequestParam CardColor color,
            @RequestParam CardType type,
            Authentication authentication){

        ResponseEntity<String> response = cardService.createCard(color,type,authentication);
        return response;
    }

    @PatchMapping("/clients/current/cards/remove")
    public ResponseEntity<?> hideCard(@RequestParam Status status, @RequestParam String number, Authentication authentication){
        ResponseEntity<?> response = cardService.hideCard(status, number,authentication);
        return response;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/cards/payments")
    public ResponseEntity<String> cardPayment(@RequestBody CardPaymentDTO cardPaymentDTO){
        ResponseEntity<String> response = cardService.cardPayment(cardPaymentDTO);
        return response;
    }
}



