package com.e_ticaret.e_commerce.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemRequestDto {

    private Long productId;
    private int quantity;
}
