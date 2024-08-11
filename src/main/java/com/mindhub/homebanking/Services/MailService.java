package com.mindhub.homebanking.Services;

public interface MailService {

    void sendRecoveryMail (String to, String subject, String text);
}
