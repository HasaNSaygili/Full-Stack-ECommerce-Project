package com.e_ticaret.e_commerce.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDto {
    private String productName;
    private int quantity;
    private BigDecimal price;
    private Long productId;
}
