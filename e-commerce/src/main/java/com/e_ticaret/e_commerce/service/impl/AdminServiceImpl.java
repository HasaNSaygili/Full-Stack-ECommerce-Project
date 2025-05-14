package com.e_ticaret.e_commerce.service.impl;

import org.springframework.stereotype.Service;

import com.e_ticaret.e_commerce.entity.Role;
import com.e_ticaret.e_commerce.entity.User;
import com.e_ticaret.e_commerce.repository.SellerRepository;
import com.e_ticaret.e_commerce.repository.UserRepository;
import com.e_ticaret.e_commerce.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        userRepository.delete(user); // Şart fark etmeksizin doğrudan sil
    }

    @Override
    public void changeUserRoleToAdmin(Long id) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }

    @Override
    public void banUser(Long id) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        user.setEnabled(false);
        userRepository.save(user);
    }
} 
