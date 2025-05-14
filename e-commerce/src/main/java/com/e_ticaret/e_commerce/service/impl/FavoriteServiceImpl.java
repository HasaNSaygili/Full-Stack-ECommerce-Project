package com.e_ticaret.e_commerce.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.e_ticaret.e_commerce.dto.ProductDto;
import com.e_ticaret.e_commerce.entity.Favorite;
import com.e_ticaret.e_commerce.entity.Product;
import com.e_ticaret.e_commerce.entity.User;
import com.e_ticaret.e_commerce.repository.FavoriteRepository;
import com.e_ticaret.e_commerce.repository.ProductRepository;
import com.e_ticaret.e_commerce.repository.UserRepository;
import com.e_ticaret.e_commerce.service.FavoriteService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public void addFavorite(Long userId, Long productId) {
        if (!favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            User user = userRepository.findById(userId).orElseThrow();
            Product product = productRepository.findById(productId).orElseThrow();
            Favorite favorite = Favorite.builder().user(user).product(product).build();
            favoriteRepository.save(favorite);
        }
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long productId) {
        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public List<ProductDto> getFavoritesByUserId(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return favorites.stream().map(fav -> {
            Product p = fav.getProduct();
            return ProductDto.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .price(p.getPrice())
                    .stock(p.getStock())
                    .description(p.getDescription())
                    .imageUrl(p.getImageUrl())
                    .sellerId(p.getSeller().getId())
                    .categoryId(p.getCategory().getId())
                    .categoryName(p.getCategory().getName())
                    .build();
        }).collect(Collectors.toList());
    }
}
