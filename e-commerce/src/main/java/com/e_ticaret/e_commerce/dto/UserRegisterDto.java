package com.e_ticaret.e_commerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterDto {

    @NotBlank(message = "Rol boş olamaz.")
    private String role;

    @NotBlank(message = "Kullanıcı adı boş olamaz.")
    private String username;

    @NotBlank(message = "Email boş olamaz.")
    @Email(message = "Geçerli bir email adresi girin.")
    private String email;

    @NotBlank(message = "Şifre boş olamaz.")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır.")
    private String password;

    private String storeName; 
}
