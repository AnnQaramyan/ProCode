package com.example.procodetask.controller;


import com.example.procodetask.model.Authority.AuthorityType;
import com.example.procodetask.model.User;
import com.example.procodetask.repository.AuthorityRepository;
import com.example.procodetask.repository.TaskRepository;
import com.example.procodetask.repository.UserRepository;
import com.example.procodetask.service.TaskService;
import com.example.procodetask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    TaskService taskService;

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
        Long userId = userService.getIdByAuthentication(authentication);
        User user = userRepository.getReferenceById(userId);
        Boolean isManager = user.getAuthorities().contains(authorityRepository.getByName(AuthorityType.MANAGER));
        modelAndView = taskService.getModel(isManager, user);
        modelAndView.setViewName("home");
        return modelAndView;
    }

}
