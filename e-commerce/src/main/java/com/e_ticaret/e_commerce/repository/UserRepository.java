package com.e_ticaret.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_ticaret.e_commerce.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
