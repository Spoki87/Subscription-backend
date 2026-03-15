package com.pawlak.subscription.subscription.controller;

import com.pawlak.subscription.response.ApiResponse;
import com.pawlak.subscription.subscription.dto.request.CreateSubscriptionRequest;
import com.pawlak.subscription.subscription.dto.request.UpdateSubscriptionRequest;
import com.pawlak.subscription.subscription.dto.response.SubscriptionByCurrencyResponse;
import com.pawlak.subscription.subscription.dto.response.SubscriptionByModelResponse;
import com.pawlak.subscription.subscription.dto.response.SubscriptionResponse;
import com.pawlak.subscription.subscription.dto.response.SubscriptionSummaryResponse;
import com.pawlak.subscription.subscription.service.SubscriptionReportService;
import com.pawlak.subscription.subscription.service.SubscriptionService;
import com.pawlak.subscription.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionReportService subscriptionReportService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<SubscriptionResponse>>> getSubscriptionsByUser(@AuthenticationPrincipal User user, @PageableDefault(page = 0, size = 20) Pageable pageable) {
        Page<SubscriptionResponse> response = subscriptionService.getSubscriptionsByUser(user, pageable);
        return ResponseEntity.ok(ApiResponse.success("Subscriptions retrieved", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> getSubscriptionById(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        SubscriptionResponse response = subscriptionService.getSubscriptionById(user, id);
        return ResponseEntity.ok(ApiResponse.success("Subscription found",response));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<SubscriptionResponse>> createSubscription(@AuthenticationPrincipal User user, @Valid @RequestBody CreateSubscriptionRequest request) {
        SubscriptionResponse response = subscriptionService.createSubscription(user,request);
        return ResponseEntity.ok(ApiResponse.success("Subscription created",response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> updateSubscription(@AuthenticationPrincipal User user, @PathVariable UUID id, @Valid @RequestBody UpdateSubscriptionRequest request) {
        SubscriptionResponse response = subscriptionService.updateSubscriptionById(user, id, request);
        return ResponseEntity.ok(ApiResponse.success("Subscription updated",response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubscription(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        subscriptionService.deleteSubscriptionById(user, id);
        return ResponseEntity.ok(ApiResponse.success("Subscription deleted"));
    }

    @GetMapping("/report/summary")
    public ResponseEntity<ApiResponse<SubscriptionSummaryResponse>> getSummary(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success("Summary retrieved", subscriptionReportService.getSummary(user)));
    }

    @GetMapping("/report/by-model")
    public ResponseEntity<ApiResponse<List<SubscriptionByModelResponse>>> getByModel(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success("Breakdown by model retrieved", subscriptionReportService.getByModel(user)));
    }

    @GetMapping("/report/by-currency")
    public ResponseEntity<ApiResponse<List<SubscriptionByCurrencyResponse>>> getByCurrency(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success("Breakdown by currency retrieved", subscriptionReportService.getByCurrency(user)));
    }

}
