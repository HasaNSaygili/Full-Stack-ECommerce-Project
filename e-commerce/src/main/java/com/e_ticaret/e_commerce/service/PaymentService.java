package com.e_ticaret.e_commerce.service;

import com.e_ticaret.e_commerce.dto.PaymentDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface PaymentService {
    void processPayment(Long orderId, PaymentDto paymentDto);
    
     PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException;
}
