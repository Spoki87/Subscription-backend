package com.pawlak.subscription.subscription.dto.response;

import com.pawlak.subscription.currency.Currency;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class SubscriptionByCurrencyResponse {
    Currency currency;
    int count;
    BigDecimal monthlyCost;
    Currency displayCurrency;
}
