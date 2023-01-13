package com.example.procodetask.config;

import com.example.procodetask.model.User;
import com.example.procodetask.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        Set<String> authoritiesSet = user.getAuthorities().stream().map(authority -> authority.getName().name()).collect(Collectors.toSet());
        return new CustomUserDetail(user.getId(), user.getUsername(), user.getPassword(), authoritiesSet);

    }
}
