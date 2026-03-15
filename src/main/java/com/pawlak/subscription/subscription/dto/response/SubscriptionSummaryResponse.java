package com.pawlak.subscription.subscription.dto.response;

import com.pawlak.subscription.currency.Currency;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class SubscriptionSummaryResponse {
    int totalCount;
    BigDecimal monthlyCost;
    BigDecimal yearlyCost;
    Currency currency;
}
