package com.e_ticaret.e_commerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e_ticaret.e_commerce.service.AdminService;
import com.e_ticaret.e_commerce.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Admin paneline erişim başarılı.");
    }

    @DeleteMapping("/delete-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteUserByAdmin(@PathVariable Long id) {
        try {
            adminService.deleteUserById(id);
            return ResponseEntity.ok("Kullanıcı kalıcı olarak silindi.");
        } catch (RuntimeException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "Bilinmeyen bir hata oluştu.";
            return ResponseEntity.badRequest().body(msg);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/make-admin/{id}")
    public ResponseEntity<String> makeAdmin(@PathVariable Long id) {
        adminService.changeUserRoleToAdmin(id);
        return ResponseEntity.ok("Kullanıcı admin yapıldı.");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/ban-user/{id}")
    public ResponseEntity<String> banUser(@PathVariable Long id) {
        adminService.banUser(id);
        return ResponseEntity.ok("Kullanıcı banlandı.");
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("HATA: " + e.getMessage());
        }
    }
} 
