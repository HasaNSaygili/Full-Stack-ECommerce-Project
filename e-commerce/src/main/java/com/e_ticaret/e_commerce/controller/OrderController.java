package com.e_ticaret.e_commerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e_ticaret.e_commerce.dto.OrderRequestDto;
import com.e_ticaret.e_commerce.dto.OrderResponseDto;
import com.e_ticaret.e_commerce.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponseDto> placeOrder(@Valid @RequestBody OrderRequestDto dto, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(dto, userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    
}
