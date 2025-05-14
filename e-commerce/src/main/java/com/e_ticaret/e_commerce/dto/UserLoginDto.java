package com.e_ticaret.e_commerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginDto {

    @NotBlank(message = "Email boş olamaz.")
    @Email(message = "Geçerli bir email adresi girin.")
    private String email;

    @NotBlank(message = "Şifre boş olamaz.")
    private String password;
}
