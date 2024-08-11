package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.dto.newPassword;
import com.mindhub.homebanking.models.PasswordToken;
import org.springframework.http.ResponseEntity;

public interface PasswordTokenService {

    ResponseEntity<String> emailSend (String email);

    ResponseEntity<String> passwordRecovery (newPassword newPassword);

    void passwordDelete (PasswordToken passwordToken);
}
