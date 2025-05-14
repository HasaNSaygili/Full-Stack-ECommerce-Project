package com.e_ticaret.e_commerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.e_ticaret.e_commerce.dto.PaymentDto;
import com.e_ticaret.e_commerce.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    public ResponseEntity<Void> processPayment(@Valid @RequestBody PaymentDto dto, @PathVariable Long orderId) {
        paymentService.processPayment(orderId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPayment(@RequestParam Long amount) {
        try {
            PaymentIntent intent = paymentService.createPaymentIntent(amount, "try");
            return ResponseEntity.ok(intent.getClientSecret());
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body("Ödeme oluşturulamadı: " + e.getMessage());
        }
    }
}
