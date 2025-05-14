package com.e_ticaret.e_commerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseDto {

    private Long orderId;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private List<OrderItemResponseDto> items;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private String cardNumber;
    
}
