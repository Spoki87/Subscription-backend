package com.pawlak.subscription.subscription.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class CreateSubscriptionRequest {
    @NotBlank(message = "Subscription name is required")
    @Size(max = 100, message = "Name must not exceed {max} characters")
    String name;

    @Size(max = 500, message = "Description must not exceed {max} characters")
    String description;

    @Positive(message = "Price must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Price format is invalid")
    BigDecimal price;
}
