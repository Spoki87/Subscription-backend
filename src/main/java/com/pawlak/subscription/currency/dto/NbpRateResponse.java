package com.pawlak.subscription.currency.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NbpRateResponse {
    private String code;
    private List<Rate> rates;

    public BigDecimal getMidRate() {
        return rates.getFirst().getMid();
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rate {
        @JsonProperty("mid")
        private BigDecimal mid;
    }
}
