package com.e_ticaret.e_commerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.e_ticaret.e_commerce.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
