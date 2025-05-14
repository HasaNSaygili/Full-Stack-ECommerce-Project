package com.e_ticaret.e_commerce.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.e_ticaret.e_commerce.dto.PaymentDto;
import com.e_ticaret.e_commerce.entity.Order;
import com.e_ticaret.e_commerce.entity.Payment;
import com.e_ticaret.e_commerce.repository.OrderRepository;
import com.e_ticaret.e_commerce.repository.PaymentRepository;
import com.e_ticaret.e_commerce.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    public void processPayment(Long orderId, PaymentDto dto) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        Payment payment = Payment.builder()
                .amount(dto.getAmount())
                .paymentMethod(dto.getPaymentMethod())
                .transactionId(dto.getTransactionId())
                .order(order)
                .paymentDate(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
    }

    public PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount); // Örnek: 1000 => 10.00 TL
        params.put("currency", currency);
        params.put("automatic_payment_methods", Map.of("enabled", true));

        return PaymentIntent.create(params);
    }
}

