package com.mindhub.homebanking.dto;


import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private Long id;
    private String cardholder;
    private int cvv;
    private String number;

    private final CardType type;
    private final CardColor color;
    private LocalDate fromDate, truDate;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardholder = card.getOwner().getFirstName()+" "+card.getOwner().getLastName();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.truDate = card.getTruDate();
    }

    public Long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public int getCvv() {
        return cvv;
    }

    public String getNumber() {
        return number;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getTruDate() {
        return truDate;
    }
}
