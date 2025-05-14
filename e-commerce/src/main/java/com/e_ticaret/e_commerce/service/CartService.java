package com.e_ticaret.e_commerce.service;

import com.e_ticaret.e_commerce.dto.CartDto;

public interface CartService {
    CartDto getCartByUserId(Long userId);
    void addToCart(Long userId, Long productId, int quantity);
    void removeFromCart(Long userId, Long productId);
    void clearCart(Long userId);
}
