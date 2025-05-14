package com.e_ticaret.e_commerce.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_ticaret.e_commerce.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
}
