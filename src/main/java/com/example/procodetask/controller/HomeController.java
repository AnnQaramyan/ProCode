package com.example.procodetask.controller;


import com.example.procodetask.model.Authority.AuthorityType;
import com.example.procodetask.model.User;
import com.example.procodetask.model.enums.TaskStatus;
import com.example.procodetask.repository.AuthorityRepository;
import com.example.procodetask.repository.TaskRepository;
import com.example.procodetask.repository.UserRepository;
import com.example.procodetask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    AuthorityRepository authorityRepository;

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView loginEndpoint() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/employer")
    public String employer() {
        return "Employerrr!";
    }

    @GetMapping("/home")
    public ModelAndView home(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        Long userId = userService.getIdByAuthentication(authentication);
        User user = userRepository.getReferenceById(userId);
        Boolean isManager = user.getAuthorities().contains(authorityRepository.getByName(AuthorityType.MANAGER));
        if (isManager) {
            modelAndView.getModel().put("tasks", taskRepository.findAll());
            modelAndView.getModel().put("users", userRepository.findAll());
            modelAndView.getModel().put("isManager", isManager);
        } else {
            modelAndView.getModel().put("tasks", taskRepository.findAllByUser(user));
            modelAndView.getModel().put("users", user);
            modelAndView.getModel().put("isManager", false);
        }

        return modelAndView;
    }

}
