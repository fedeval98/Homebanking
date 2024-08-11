package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.MailService;
import com.mindhub.homebanking.Services.PasswordTokenService;
import com.mindhub.homebanking.Utils.TokenGenerator;
import com.mindhub.homebanking.dto.newPassword;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.PasswordToken;
import com.mindhub.homebanking.repositories.PasswordTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class PasswordTokenSeriveImpl implements PasswordTokenService {

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String baseURL = "http://localhost:8080";

    private static final Logger logger = Logger.getLogger(PasswordTokenSeriveImpl.class.getName());

    @Override
    public void passwordDelete(PasswordToken passwordToken) {
        passwordTokenRepository.delete(passwordToken);
    }

    @Override
    @Transactional
    public ResponseEntity<String> emailSend (String email){
        Client client = clientService.getAuthClient(email);



        if (client == null){
            return new ResponseEntity<>("Email not Found", HttpStatus.BAD_REQUEST);
        } else {
            String newToken = TokenGenerator.generateUniqueToken();

            PasswordToken token = new PasswordToken(newToken, client, LocalDateTime.now().plusMinutes(10));

            passwordTokenRepository.save(token);

            client.addToken(token);
            clientService.saveClient(client);

            String to = email;
            String subject = "Password recovery link";
            String text = "Your link to recover your password is: "+baseURL+"/api/clients/passwordRecovery?token="+newToken;

            mailService.sendRecoveryMail(to, subject, text);

            return new ResponseEntity<>("Recovery link sent to email", HttpStatus.OK);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> passwordRecovery (newPassword newPassword){
        Client client = clientService.getAuthClient(newPassword.getEmail());

        if (newPassword.getToken() == null || newPassword.getToken().isBlank()){
            return new ResponseEntity<>("Token can't be blank",HttpStatus.FORBIDDEN);
        }

        if (newPassword.getEmail() == null || newPassword.getEmail().isBlank()){
            return new ResponseEntity<>("Email can't be blank",HttpStatus.FORBIDDEN);
        }

        if (newPassword.getPassword() == null|| newPassword.getPassword().isBlank()){
            return new ResponseEntity<>("Password can't be blank",HttpStatus.FORBIDDEN);
        }

        if(client == null){
            return new ResponseEntity<>("Client not found", HttpStatus.BAD_REQUEST);
        }

        if(client.getToken() == null){
            return new ResponseEntity<>("No token found for this client",HttpStatus.BAD_REQUEST);
        }

        if(client.getToken().getExpirationDate().isBefore(LocalDateTime.now())){
            return new ResponseEntity<>("Token Expired, please generate a new one",HttpStatus.BAD_REQUEST);
        }

        if(!client.getToken().getToken().equals(newPassword.getToken())){
            return new ResponseEntity<>("The client email does not match the token", HttpStatus.BAD_REQUEST);
        }

        client.setPassword(passwordEncoder.encode(newPassword.getPassword()));
        clientService.saveClient(client);

        String to = newPassword.getEmail();
        String subject = "Password Change Successfully";
        String text = "Your password has been changed successfully. If you did not initiate this change, please contact our support team immediately.";

        mailService.sendRecoveryMail(to, subject, text);

        passwordDelete(client.getToken());

        return new ResponseEntity<>("Client password updated successfully", HttpStatus.OK);

    }

}
