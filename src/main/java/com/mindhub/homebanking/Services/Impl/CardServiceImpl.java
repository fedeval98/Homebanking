package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.Utils.CardUtils;
import com.mindhub.homebanking.dto.CardDTO;
import com.mindhub.homebanking.dto.CardPaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.models.enums.CardColor;
import com.mindhub.homebanking.models.enums.CardType;
import com.mindhub.homebanking.models.enums.Status;
import com.mindhub.homebanking.models.enums.TransactionType;
import com.mindhub.homebanking.repositories.CardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Override
    public List<CardDTO> getClientCards() {
        return cardRepository.findAll() // busco todos los clientes en mi repositorio
                .stream().filter(card -> card.getStatus().equals(Status.ACTIVE)) //convierto la lista en un Stream para poder usar operaciones intermedias (map, filter, sort, etc)
                 //o terminales (count, collect, forEach, etc)
                .map(CardDTO::new) // transformo cada client en un objeto DTO
                .collect(Collectors.toList()); // recopilo tod;
    }

    @Override
    public ResponseEntity<String> createCard(CardColor color, CardType type, Authentication authentication) {
        Client client = clientService.getAuthClient(authentication.getName());
        // obtengo los cards de los clientes, los transformo a stream, los filtro por color y tipo y los contabilizo.
              long colorTypeCount = client.getCards().stream()
                      .filter(card -> card.getTruDate().isAfter(card.getFromDate()))
                      .filter(card -> card.getStatus().equals(Status.ACTIVE))
                      .filter(card -> card.getColor().equals(color) && card.getType().equals(type))
                      .count();

        //  Verifico si ya se tiene un tipo del card seleccionado y si ya existen 3, devuelvo un error.
          if (colorTypeCount == 1) {
              return new ResponseEntity<>("You already have this color and this type of card " + type, HttpStatus.FORBIDDEN);
          }

        //    Verifico si ya se tiene un maximo de 6 cards y si se cumple, devuelvo un error.
          if (client.getCards().stream().filter(card -> card.getTruDate().isAfter(card.getFromDate())).collect(Collectors.toList()).size() >= 6) {
              return new ResponseEntity<>("You have reached the maximum limit of 6 cards", HttpStatus.FORBIDDEN);
  }
        // generador automatico, random, de CVV
              int cvv = CardUtils.generateRandomCVV();

              String cardNumber = CardUtils.generateRandomCardNumber();

              String cardholder = client.getFirstName() + " " + client.getLastName();

              LocalDate startDate = LocalDate.now();
              LocalDate expirationDate = startDate.plusYears(5);

              Card card = new Card(cardholder, type, color, cardNumber, cvv, startDate, expirationDate);

              client.addCard(card);
              cardRepository.save(card);

              return new ResponseEntity<>("Card created for the client", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> hideCard(Status status,String number, Authentication authentication) {
        Card card = findByNumberAndClientEmail(number, authentication.getName());

        if(card == null){
            return new ResponseEntity<>("You are not the owner of this card.",HttpStatus.FORBIDDEN);
        }

        if (card.getStatus().equals(Status.DEACTIVE)) {
            return new ResponseEntity<>("Can not find the number of the card or is already deactivated.", HttpStatus.BAD_REQUEST);
        }

        if (card.getNumber().equals(number) && card.getOwner().getEmail().equals(authentication.getName())){
            card.setStatus(Status.DEACTIVE);
            cardRepository.save(card);
        }

        return new ResponseEntity<>("Card deactivated",HttpStatus.OK);
    }

   @Override
    public Card findByNumberAndClientEmail(String number, String email) {
        return cardRepository.findByNumberAndClientEmail(number, email);
    }

    @Override
    @Transactional
    public ResponseEntity<String> cardPayment(CardPaymentDTO cardPaymentDTO){

        Card card = cardRepository.findByNumber(cardPaymentDTO.getNumber());

        Client client = card.getClient();

        List<Account> accountList = client.getAccounts().stream()
                .filter(account -> account.getBalance() >= cardPaymentDTO.getAmount())
                .toList();

        Account firstAccount = accountList.stream().findFirst().orElse(null);

        if (cardPaymentDTO.getNumber().isBlank()){
            return new ResponseEntity<>("You need to put a destination number account", HttpStatus.FORBIDDEN);
        }
        if (cardPaymentDTO.getCvv() < 100 && cardPaymentDTO.getCvv() > 999){
            return new ResponseEntity<>("cvv conteins more than 3 digits" ,HttpStatus.FORBIDDEN);
        }
        if(cardPaymentDTO.getAmount() <= 0){
            return new ResponseEntity<>("The amount has to be greater than 0" , HttpStatus.FORBIDDEN);
        }
        if (cardPaymentDTO.getDescription().isBlank()){
            return new ResponseEntity<>("Please provide a description for the current payment" , HttpStatus.FORBIDDEN);
        }
        if(card.getTruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("This card is expired, please contact with the bank to get support" , HttpStatus.FORBIDDEN);
        }
        if (firstAccount.getBalance() < cardPaymentDTO.getAmount()){
            return new ResponseEntity<>("insufficient founds" , HttpStatus.FORBIDDEN);
        }
        //Se debe crear una transacción que indique el débito a una de las cuentas con la descripción de la operación

        Account sourceAccount = accountService.findByNumber(firstAccount.getNumber());

        Transaction paymentTransaction = new Transaction(sourceAccount,LocalDateTime.now(),cardPaymentDTO.getAmount(),TransactionType.DEBIT,cardPaymentDTO.getDescription());

        double debitAccount = (firstAccount.getBalance() - cardPaymentDTO.getAmount());

        firstAccount.setBalance(debitAccount);
        firstAccount.addTransaction(paymentTransaction);
        transactionService.saveTransaction(paymentTransaction);

        return new ResponseEntity<>("Successful Payment", HttpStatus.CREATED);
    }

}
