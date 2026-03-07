package com.pawlak.subscription.subscription.mapper;

import com.pawlak.subscription.subscription.dto.response.SubscriptionResponse;
import com.pawlak.subscription.subscription.model.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionResponse toResponse(Subscription subscription);
}
