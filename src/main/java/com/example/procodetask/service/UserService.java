package com.example.procodetask.service;

import com.example.procodetask.model.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    public void saveUser(User user);

    User findByUsername(String email);

    Long getIdByAuthentication(Authentication authentication);

}
