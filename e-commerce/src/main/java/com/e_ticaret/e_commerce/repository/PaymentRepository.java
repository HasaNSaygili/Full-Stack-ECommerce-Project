package com.e_ticaret.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_ticaret.e_commerce.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}