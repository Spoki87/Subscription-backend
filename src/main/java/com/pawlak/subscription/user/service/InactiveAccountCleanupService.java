package com.pawlak.subscription.user.service;

import com.pawlak.subscription.token.registrationtoken.model.RegistrationToken;
import com.pawlak.subscription.token.registrationtoken.repository.RegistrationTokenRepository;
import com.pawlak.subscription.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InactiveAccountCleanupService {

    private final RegistrationTokenRepository registrationTokenRepository;
    private final UserRepository userRepository;

    @Scheduled(fixedRateString = "${cleanup.inactive-account.interval-ms:3600000}")
    @Transactional
    public void removeInactiveAccountsWithExpiredTokens() {
        List<RegistrationToken> expiredTokens = registrationTokenRepository
                .findAllByExpiredTimeBefore(LocalDateTime.now());

        List<RegistrationToken> tokensOfInactiveUsers = expiredTokens.stream()
                .filter(t -> !t.getUser().isEnabled())
                .toList();

        if (tokensOfInactiveUsers.isEmpty()) {
            return;
        }

        List<UUID> inactiveUserIds = tokensOfInactiveUsers.stream()
                .map(t -> t.getUser().getId())
                .toList();

        registrationTokenRepository.deleteAllByUserIds(inactiveUserIds);
        userRepository.deleteAllByIds(inactiveUserIds);
    }
}
