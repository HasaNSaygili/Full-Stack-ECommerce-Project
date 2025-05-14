package com.e_ticaret.e_commerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e_ticaret.e_commerce.dto.UserLoginDto;
import com.e_ticaret.e_commerce.dto.UserRegisterDto;
import com.e_ticaret.e_commerce.dto.UserResponseDto;
import com.e_ticaret.e_commerce.entity.User;
import com.e_ticaret.e_commerce.repository.UserRepository;
import com.e_ticaret.e_commerce.security.JwtUtil;
import com.e_ticaret.e_commerce.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRegisterDto dto) {
        userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginDto dto) {
        String token = userService.loginUser(dto);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    // 👤 Giriş yapan kullanıcının profil bilgisi
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);
            User user = userRepository.findByEmail(email);
            if (user != null) {
                UserResponseDto dto = new UserResponseDto();
                dto.setId(user.getId());
                dto.setEmail(user.getEmail());
                dto.setUsername(user.getUsername());
                dto.setRole(user.getRole().name());
                return ResponseEntity.ok(dto);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 🔒 Admin veya backend kullanımı için spesifik id üzerinden kullanıcı bilgisi
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> changeUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        userService.changeUserRole(id, body.get("role"));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Rol güncellendi");
        return ResponseEntity.ok(response);
    }


}
