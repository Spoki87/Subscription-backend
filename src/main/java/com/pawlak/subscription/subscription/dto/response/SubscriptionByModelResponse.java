package com.pawlak.subscription.subscription.dto.response;

import com.pawlak.subscription.currency.Currency;
import com.pawlak.subscription.subscription.model.SubscriptionModel;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class SubscriptionByModelResponse {
    SubscriptionModel model;
    int count;
    BigDecimal monthlyCost;
    Currency currency;
}
