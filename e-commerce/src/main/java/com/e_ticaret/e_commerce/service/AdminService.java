package com.e_ticaret.e_commerce.service;

public interface AdminService {
    void deleteUserById(Long id);
    void changeUserRoleToAdmin(Long id);
    void banUser(Long id);

}
