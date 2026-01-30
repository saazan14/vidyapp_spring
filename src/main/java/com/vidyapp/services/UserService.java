package com.vidyapp.services;

import com.vidyapp.dtos.UserDTO;
import com.vidyapp.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void updateUserRole(Long userId, String roleName);
    UserDTO getUserById(Long id);
    User findByUsername(String username);
}
