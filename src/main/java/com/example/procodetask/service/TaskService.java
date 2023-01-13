package com.example.procodetask.service;

import com.example.procodetask.command.FilterCommand;
import com.example.procodetask.command.TaskCommand;
import com.example.procodetask.model.Task;
import com.example.procodetask.model.User;
import com.example.procodetask.model.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    List<Task> findAllByUser(User user);

    Task findBy(Long id);

    Task save(TaskCommand taskCommand, Long userId);

    Task updateTaskStatus(TaskStatus taskStatus, Long taskId);

    void deleteTask(Long taskId);

    List<Task> filter(FilterCommand filterCommand, Boolean isManager, User user);
}
