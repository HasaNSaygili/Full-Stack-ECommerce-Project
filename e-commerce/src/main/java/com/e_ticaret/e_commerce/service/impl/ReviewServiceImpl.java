package com.e_ticaret.e_commerce.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.e_ticaret.e_commerce.dto.ReviewDto;
import com.e_ticaret.e_commerce.entity.Product;
import com.e_ticaret.e_commerce.entity.Review;
import com.e_ticaret.e_commerce.entity.User;
import com.e_ticaret.e_commerce.repository.ProductRepository;
import com.e_ticaret.e_commerce.repository.ReviewRepository;
import com.e_ticaret.e_commerce.repository.UserRepository;
import com.e_ticaret.e_commerce.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public void addReview(Long productId, Long userId, ReviewDto reviewDto) {
        Product product = productRepository.findById(productId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Review review = Review.builder()
                .rating(reviewDto.getRating())
                .comment(reviewDto.getComment())
                .user(user)
                .product(product)
                .build();

        reviewRepository.save(review);
    }

    @Override
    public List<ReviewDto> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(r -> ReviewDto.builder()
                        .rating(r.getRating())
                        .comment(r.getComment())
                        .build())
                .collect(Collectors.toList());
    }
}
