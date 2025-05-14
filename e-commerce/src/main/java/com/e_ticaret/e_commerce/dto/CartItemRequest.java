package com.e_ticaret.e_commerce.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private int quantity;
}
