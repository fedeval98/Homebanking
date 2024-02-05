package com.mindhub.homebanking.models;

import com.mindhub.homebanking.models.enums.CardColor;
import com.mindhub.homebanking.models.enums.CardType;
import com.mindhub.homebanking.models.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    private String cardholder;

    @Enumerated(EnumType.STRING)
    private CardType type;

    @Enumerated(EnumType.STRING)
    private CardColor color;

    private String number;

    private int cvv;

    private LocalDate fromDate;

    private LocalDate truDate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private boolean expire = false;

    public Card() {
    }

    public Card(String cardholder, CardType type, CardColor color, String number, int cvv, LocalDate fromDate, LocalDate thruDate) {
        this.cardholder = cardholder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.truDate = thruDate;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public Card(CardColor color){
        this.color = color;
    }

    public Client getClient() {
        return client;
    }

    public Card(CardType type){
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public Client getOwner() {
        return client;
    }

    public void setOwner(Client client) {
        this.client = client;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getTruDate() {
        return truDate;
    }

    public void setTruDate(LocalDate truDate) {
        this.truDate = truDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }
}
