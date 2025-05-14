package com.e_ticaret.e_commerce.service.impl;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.e_ticaret.e_commerce.dto.CartDto;
import com.e_ticaret.e_commerce.dto.CartItemDto;
import com.e_ticaret.e_commerce.entity.Cart;
import com.e_ticaret.e_commerce.entity.CartItem;
import com.e_ticaret.e_commerce.entity.Product;
import com.e_ticaret.e_commerce.entity.User;
import com.e_ticaret.e_commerce.repository.CartRepository;
import com.e_ticaret.e_commerce.repository.ProductRepository;
import com.e_ticaret.e_commerce.repository.UserRepository;
import com.e_ticaret.e_commerce.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public CartDto getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            return CartDto.builder()
                    .id(null)
                    .cartItems(new ArrayList<>())
                    .totalAmount(java.math.BigDecimal.ZERO)
                    .build();
        }

        return CartDto.builder()
                .id(cart.getId())
                .cartItems(cart.getCartItems().stream().map(item ->
                        CartItemDto.builder()
                                .productId(item.getProduct().getId())
                                .productName(item.getProduct().getName())
                                .quantity(item.getQuantity())
                                .price(item.getProduct().getPrice())
                                .build()).collect(Collectors.toList()))
                .totalAmount(cart.getCartItems().stream()
                        .map(i -> i.getProduct().getPrice().multiply(java.math.BigDecimal.valueOf(i.getQuantity())))
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add))
                .build();
    }

    @Override
    public void addToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            User user = userRepository.findById(userId).orElseThrow();
            cart = new Cart();
            cart.setUser(user);
            cart.setCartItems(new ArrayList<>());
        }

        Product product = productRepository.findById(productId).orElseThrow();

        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            if (existingItem.getQuantity() <= 0) {
                cart.getCartItems().remove(existingItem);
            }
        } else if (quantity > 0) {
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setCart(cart);
            cart.getCartItems().add(item);
        }

        cartRepository.save(cart);
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) return;

        cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);
        cart.setCartItems(cart.getCartItems());
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) return;

        // HER BİR CART ITEM'IN PARENT'INI SIFIRLA
        for (CartItem item : cart.getCartItems()) {
            item.setCart(null); // ❗ orphanRemoval düzgün çalışsın diye
        }

        cart.getCartItems().clear(); // Koleksiyondan sil
        cartRepository.save(cart);   // Güncelle ve silinsin
    }

}
