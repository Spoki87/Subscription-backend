package com.pawlak.subscription.subscription.dto.response;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class SubscriptionResponse {
    UUID id;
    String name;
    String description;
    BigDecimal price;
}
