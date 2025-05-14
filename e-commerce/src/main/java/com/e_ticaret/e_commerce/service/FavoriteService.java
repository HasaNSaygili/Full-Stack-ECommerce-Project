package com.e_ticaret.e_commerce.service;

import java.util.List;

import com.e_ticaret.e_commerce.dto.ProductDto;

public interface FavoriteService {
    void addFavorite(Long userId, Long productId);
    void removeFavorite(Long userId, Long productId);
    List<ProductDto> getFavoritesByUserId(Long userId);
}

