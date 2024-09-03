package com.mindhub.homebanking.Utils;

import java.security.SecureRandom;

public class TokenGenerator {
    private static final String CHARACTERS =    "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+
                                                "abcdefghijklmnopqrstuvwxyz"+
                                                "0123456789";

    private static final int SEGMENT_LENGTH = 4;
    private static final int TOTAL_LENGTH = 3;

    private static SecureRandom random = new SecureRandom();

    public static String generateUniqueToken(){
        StringBuilder uniqueToken = new StringBuilder();

        for (int i = 0; i < TOTAL_LENGTH; i++){
            for (int j = 0; j < SEGMENT_LENGTH;j++){
                uniqueToken.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }

            if(i < TOTAL_LENGTH - 1){
                uniqueToken.append("-");
            }
        }
        return uniqueToken.toString();
    }
}
