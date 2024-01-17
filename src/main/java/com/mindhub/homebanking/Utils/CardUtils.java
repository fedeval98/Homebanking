package com.mindhub.homebanking.Utils;

public class CardUtils {

    public static String generateRandomCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int section = (int) (Math.random() * 9000 + 1000);
            cardNumber.append(section).append("-");
        }
        return cardNumber.substring(0, cardNumber.length() - 1);
    }

    public static int generateRandomCVV(){
       int cvv = (int) (Math.random() * 900 + 100);
       return cvv;
    }
}
