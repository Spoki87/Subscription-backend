package com.pawlak.subscription.subscription.service;

import com.pawlak.subscription.currency.Currency;
import com.pawlak.subscription.currency.ExchangeRateService;
import com.pawlak.subscription.subscription.dto.response.SubscriptionByCurrencyResponse;
import com.pawlak.subscription.subscription.dto.response.SubscriptionByModelResponse;
import com.pawlak.subscription.subscription.dto.response.SubscriptionSummaryResponse;
import com.pawlak.subscription.subscription.model.Subscription;
import com.pawlak.subscription.subscription.model.SubscriptionModel;
import com.pawlak.subscription.subscription.repository.SubscriptionRepository;
import com.pawlak.subscription.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionReportService {

    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);

    private final SubscriptionRepository subscriptionRepository;
    private final ExchangeRateService exchangeRateService;

    public SubscriptionSummaryResponse getSummary(User user) {
        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        Currency target = user.getCurrency();

        BigDecimal totalMonthly = subscriptions.stream()
                .map(sub -> toMonthlyInTargetCurrency(sub, target))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalYearly = totalMonthly
                .multiply(MONTHS_IN_YEAR)
                .setScale(2, RoundingMode.HALF_UP);

        return new SubscriptionSummaryResponse(
                subscriptions.size(),
                totalMonthly.setScale(2, RoundingMode.HALF_UP),
                totalYearly,
                target
        );
    }

    public List<SubscriptionByModelResponse> getByModel(User user) {
        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        Currency target = user.getCurrency();

        Map<SubscriptionModel, List<Subscription>> byModel = subscriptions.stream()
                .collect(Collectors.groupingBy(Subscription::getSubscriptionModel));

        return byModel.entrySet().stream()
                .map(entry -> {
                    BigDecimal monthlyCost = entry.getValue().stream()
                            .map(sub -> toMonthlyInTargetCurrency(sub, target))
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .setScale(2, RoundingMode.HALF_UP);

                    return new SubscriptionByModelResponse(
                            entry.getKey(),
                            entry.getValue().size(),
                            monthlyCost,
                            target
                    );
                })
                .sorted((a, b) -> a.getModel().compareTo(b.getModel()))
                .collect(Collectors.toList());
    }

    public List<SubscriptionByCurrencyResponse> getByCurrency(User user) {
        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        Currency target = user.getCurrency();

        Map<Currency, List<Subscription>> byCurrency = subscriptions.stream()
                .collect(Collectors.groupingBy(Subscription::getCurrency));

        return byCurrency.entrySet().stream()
                .map(entry -> {
                    BigDecimal monthlyCost = entry.getValue().stream()
                            .map(sub -> toMonthlyInTargetCurrency(sub, target))
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .setScale(2, RoundingMode.HALF_UP);

                    return new SubscriptionByCurrencyResponse(
                            entry.getKey(),
                            entry.getValue().size(),
                            monthlyCost,
                            target
                    );
                })
                .sorted((a, b) -> a.getCurrency().compareTo(b.getCurrency()))
                .collect(Collectors.toList());
    }

    private BigDecimal toMonthlyInTargetCurrency(Subscription sub, Currency target) {
        BigDecimal converted = exchangeRateService.convert(sub.getPrice(), sub.getCurrency(), target);
        if (sub.getSubscriptionModel() == SubscriptionModel.YEARLY) {
            return converted.divide(MONTHS_IN_YEAR, 2, RoundingMode.HALF_UP);
        }
        return converted;
    }
}
