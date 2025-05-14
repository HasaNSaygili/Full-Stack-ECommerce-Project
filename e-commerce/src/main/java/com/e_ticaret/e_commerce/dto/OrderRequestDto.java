package com.e_ticaret.e_commerce.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRequestDto {

    private List<OrderItemRequestDto> items;
    private Long addressId;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private String cardNumber;

    // Stripe özel alanları
    private String transactionId;
    private BigDecimal amount;
}
