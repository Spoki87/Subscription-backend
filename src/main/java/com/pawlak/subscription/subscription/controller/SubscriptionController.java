package com.pawlak.subscription.subscription.controller;

import com.pawlak.subscription.response.ApiResponse;
import com.pawlak.subscription.subscription.dto.response.SubscriptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    @GetMapping()
    ResponseEntity<ApiResponse<List<SubscriptionResponse>>> getSubscriptions() {
        return null;
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<SubscriptionResponse>> getSubscriptionById() {
        return null;
    }

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<SubscriptionResponse>> updateSubscription() {
        return null;
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> deleteSubscription() {
        return null;
    }

}
