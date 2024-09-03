package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;

@RepositoryRestResource
public interface PasswordTokenRepository extends JpaRepository <PasswordToken, Long> {
    int deleteByExpirationDateBefore(LocalDateTime now);
}

