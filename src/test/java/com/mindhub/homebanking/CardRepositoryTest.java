package com.mindhub.homebanking;

import com.mindhub.homebanking.Utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardRepositoryTest {

//    @Test
//    public void testCardNumberCreated(){
//        String cardNumber = CardUtils.generateRandomCardNumber();
//
//        assertThat(cardNumber,is(not(emptyOrNullString())));
//    }
//
//    @Test
//    public void testCVVCreated(){
//        int cvv = CardUtils.generateRandomCVV();
//
//        assertThat(cvv,is(not(equalTo(000))));
//    }
}
