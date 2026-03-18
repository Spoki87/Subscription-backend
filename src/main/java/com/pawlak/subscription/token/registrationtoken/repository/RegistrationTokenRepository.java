package com.pawlak.subscription.token.registrationtoken.repository;

import com.pawlak.subscription.token.registrationtoken.model.RegistrationToken;
import com.pawlak.subscription.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, UUID> {
    Optional<RegistrationToken> findByToken(String token);
    Optional<RegistrationToken> findByUser(User user);
}
