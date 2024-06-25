package com.vs.runner.auth.repositories;


import com.vs.runner.auth.entities.ConfirmRegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmRegistrationTokenRepository extends JpaRepository<ConfirmRegistrationToken, String> {
    Optional<ConfirmRegistrationToken> findByToken(String token);
}
