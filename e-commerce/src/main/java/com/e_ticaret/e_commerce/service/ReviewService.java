package com.e_ticaret.e_commerce.service;

import java.util.List;

import com.e_ticaret.e_commerce.dto.ReviewDto;

public interface ReviewService {
    void addReview(Long productId, Long userId, ReviewDto reviewDto);
    List<ReviewDto> getReviewsForProduct(Long productId);
}
