package com.e_ticaret.e_commerce.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentDto {
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
}
