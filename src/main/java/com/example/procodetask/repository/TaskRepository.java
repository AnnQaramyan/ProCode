package com.example.procodetask.repository;

import com.example.procodetask.model.Task;
import com.example.procodetask.model.User;
import com.example.procodetask.model.enums.TaskStatus;
import org.springframework.core.task.TaskDecorator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);

    List<Task> findByTaskStatus(TaskStatus status);

    List<Task> findByTaskStatusAndUser(TaskStatus status, User user);

    Task findByUserId(Long userId);
}
