package com.e_ticaret.e_commerce.service.impl;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.e_ticaret.e_commerce.dto.UserLoginDto;
import com.e_ticaret.e_commerce.dto.UserRegisterDto;
import com.e_ticaret.e_commerce.dto.UserResponseDto;
import com.e_ticaret.e_commerce.entity.Role;
import com.e_ticaret.e_commerce.entity.Seller;
import com.e_ticaret.e_commerce.entity.User;
import com.e_ticaret.e_commerce.repository.SellerRepository;
import com.e_ticaret.e_commerce.repository.UserRepository;
import com.e_ticaret.e_commerce.security.JwtUtil;
import com.e_ticaret.e_commerce.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SellerRepository sellerRepository;

    @Override
    public void registerUser(UserRegisterDto dto) {
    if (userRepository.findByEmail(dto.getEmail()) != null) {
        throw new RuntimeException("Bu e-posta ile zaten bir kullanıcı var.");
    }

    Role role;
    try {
        role = Role.valueOf(dto.getRole());
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Geçersiz rol türü.");
    }

    User user = User.builder()
            .username(dto.getUsername())
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .role(role)
            .enabled(true)
            .build();

    userRepository.save(user);

    if (role == Role.ROLE_SELLER) {
        if (dto.getStoreName() == null || dto.getStoreName().isBlank()) {
            throw new RuntimeException("Satıcı kaydı için mağaza adı gereklidir.");
        }

        Seller seller = Seller.builder()
                .user(user)
                .storeName(dto.getStoreName())
                .build();

        sellerRepository.save(seller);
    }
    }


    @Override
    public String loginUser(UserLoginDto dto) {
        User user = userRepository.findByEmail(dto.getEmail());

        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Geçersiz e-posta veya şifre.");
        }
        String role = user.getRole().name();
        return jwtUtil.generateToken(user.getEmail(), role ,user.getId());

    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        return response;
    }

    @Override
public List<UserResponseDto> getAllUsers() {
    return userRepository.findAll().stream().map(user -> {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole() != null ? user.getRole().name() : "ROLE_USER"); // default
        return dto;
    }).toList();
}

@Override
public void deleteUser(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

    if (user.getRole() == Role.ROLE_ADMIN) {
        throw new AccessDeniedException("Admin kullanıcılar silinemez.");
    }

    userRepository.deleteById(id);
}


    @Override
    public void changeUserRole(Long id, String role) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            if (user.getRole() == Role.ROLE_ADMIN) {
                throw new AccessDeniedException("Admin kullanıcıların rolü değiştirilemez.");
            } 

        user.setRole(Role.valueOf(role)); // örnek: "ROLE_ADMIN"
        userRepository.save(user);
    }
}
