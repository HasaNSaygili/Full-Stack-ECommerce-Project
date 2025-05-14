package com.e_ticaret.e_commerce.service;

import java.util.List;

import com.e_ticaret.e_commerce.dto.UserLoginDto;
import com.e_ticaret.e_commerce.dto.UserRegisterDto;
import com.e_ticaret.e_commerce.dto.UserResponseDto;

public interface UserService {

    void registerUser(UserRegisterDto userRegisterDto);
    String loginUser(UserLoginDto userLoginDto);
    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();
    void deleteUser(Long id);
    void changeUserRole(Long id, String role);
}

