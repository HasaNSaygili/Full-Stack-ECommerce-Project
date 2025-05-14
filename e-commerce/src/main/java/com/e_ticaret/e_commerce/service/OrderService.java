package com.e_ticaret.e_commerce.service;

import java.util.List;

import com.e_ticaret.e_commerce.dto.OrderRequestDto;
import com.e_ticaret.e_commerce.dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto placeOrder(OrderRequestDto orderRequestDto, Long userId);
    List<OrderResponseDto> getOrdersByUser(Long userId);
    

}
