package com.pawlak.subscription.token.registrationtoken.repository;

import com.pawlak.subscription.token.registrationtoken.model.RegistrationToken;
import com.pawlak.subscription.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, UUID> {
    Optional<RegistrationToken> findByToken(String token);
    Optional<RegistrationToken> findByUser(User user);
    List<RegistrationToken> findAllByExpiredTimeBefore(LocalDateTime now);

    @Modifying
    @Query("DELETE FROM RegistrationToken t WHERE t.user.id IN :userIds")
    void deleteAllByUserIds(@Param("userIds") List<UUID> userIds);
}
