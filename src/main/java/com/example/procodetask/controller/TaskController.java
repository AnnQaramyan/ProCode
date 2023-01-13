package com.example.procodetask.controller;

import com.example.procodetask.command.FilterCommand;
import com.example.procodetask.command.TaskCommand;
import com.example.procodetask.model.Authority.AuthorityType;
import com.example.procodetask.model.Task;
import com.example.procodetask.model.User;
import com.example.procodetask.model.enums.TaskStatus;
import com.example.procodetask.repository.AuthorityRepository;
import com.example.procodetask.repository.TaskRepository;
import com.example.procodetask.repository.UserRepository;
import com.example.procodetask.service.TaskService;
import com.example.procodetask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@Controller
public class TaskController {
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @PostMapping("/createTask")
    public ModelAndView add(@ModelAttribute TaskCommand taskCommand, Authentication authentication) throws ChangeSetPersister.NotFoundException {
        Long userId = userService.getIdByAuthentication(authentication);
        taskService.save(taskCommand, userId);
        ModelMap model =  new ModelMap();
        ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.getById(userId);
        Boolean isManager = user.getAuthorities().contains(authorityRepository.getByName(AuthorityType.MANAGER));
        modelAndView = taskService.getModel(isManager, user);
        model.addAttribute(modelAndView);
        return new ModelAndView("redirect:/home", model);
    }

    @GetMapping("/newTask")
    public ModelAndView newTask(Authentication authentication) {
        Long userId = userService.getIdByAuthentication(authentication);
        ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.getById(userId);
        Boolean isManager = user.getAuthorities().contains(authorityRepository.getByName(AuthorityType.MANAGER));
        modelAndView = taskService.getModel(isManager, user);
        modelAndView.setViewName("newTask");
        return modelAndView;
    }

    @PostMapping("/updateTaskStatus")
    public ResponseEntity<Task> updateTaskStatus(@RequestParam String taskStatus, @RequestParam Long taskId, Authentication authentication) {
        Long userId = userService.getIdByAuthentication(authentication);
        User user = userRepository.getReferenceById(userId);
        Boolean isManager = user.getAuthorities().contains(authorityRepository.getByName(AuthorityType.MANAGER));
        if (!isManager && (taskStatus == TaskStatus.DONE.toString() || taskStatus == TaskStatus.REOPEN.toString())){
            throw  new AccessDeniedException("Access Denied");
        }
        Task task = taskService.updateTaskStatus(TaskStatus.valueOf(taskStatus), taskId);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/deleteTask")
    public void deleteTask(Long taskId) {
        taskService.deleteTask(taskId);
    }


    @GetMapping("/filter")
    public ModelAndView filter(@ModelAttribute FilterCommand filterCommand, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();
        Long userId = userService.getIdByAuthentication(authentication);
        User user = userRepository.getReferenceById(userId);
        Boolean isManager = user.getAuthorities().contains(authorityRepository.getByName(AuthorityType.MANAGER));
        List<Task> tasks = taskService.filter(filterCommand, isManager, user);
        modelAndView.getModel().put("tasks", tasks);
        modelAndView.getModel().put("users", userRepository.findAll());
        modelAndView.setViewName("task_table");
        modelAndView.getModel().put("isManager", isManager);
        return modelAndView;
    }
}
