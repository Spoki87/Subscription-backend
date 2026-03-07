package com.pawlak.subscription.subscription.service;

import com.pawlak.subscription.exception.domain.InvalidPermissionAccessException;
import com.pawlak.subscription.exception.domain.RecordNotFoundException;
import com.pawlak.subscription.subscription.Repository.SubscriptionRepository;
import com.pawlak.subscription.subscription.dto.response.SubscriptionResponse;
import com.pawlak.subscription.subscription.mapper.SubscriptionMapper;
import com.pawlak.subscription.subscription.model.Subscription;
import com.pawlak.subscription.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    public Page<SubscriptionResponse> getSubscriptionsByUser(User user, Pageable pageable) {
        Page<Subscription> subscriptions = subscriptionRepository.findAllByUser(user, pageable);
        return subscriptions.map(subscription -> new SubscriptionResponse());
    }

    public SubscriptionResponse getSubscriptionById(User user, UUID id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);
        if (!subscription.getUser().getId().equals(user.getId())) {
            throw new InvalidPermissionAccessException();
        }

        return subscriptionMapper.toResponse(subscription);
    }

    public SubscriptionResponse createSubscription(User user) {
        return new SubscriptionResponse();
    }

    public SubscriptionResponse updateSubscriptionById(User user, UUID id) {
        return new SubscriptionResponse();
    }

    public void deleteSubscriptionById(User user, UUID id) {
    }
}
