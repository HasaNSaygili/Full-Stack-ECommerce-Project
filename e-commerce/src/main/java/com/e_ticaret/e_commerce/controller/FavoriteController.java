package com.e_ticaret.e_commerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e_ticaret.e_commerce.dto.ProductDto;
import com.e_ticaret.e_commerce.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<Void> add(@PathVariable Long userId, @PathVariable Long productId) {
        favoriteService.addFavorite(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Void> remove(@PathVariable Long userId, @PathVariable Long productId) {
        favoriteService.removeFavorite(userId, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ProductDto>> getFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUserId(userId));
    }
}
