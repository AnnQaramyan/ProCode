package com.example.procodetask.service;

import com.example.procodetask.command.FilterCommand;
import com.example.procodetask.command.TaskCommand;
import com.example.procodetask.model.Task;
import com.example.procodetask.model.User;
import com.example.procodetask.model.enums.TaskStatus;
import com.example.procodetask.repository.TaskRepository;
import com.example.procodetask.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public List<Task> findAllByUser(User user) {
        return taskRepository.findAllByUser(user);
    }

    @Override
    public Task findBy(Long id) {
        return taskRepository.getById(id);
    }

    @Override
    public Task save(TaskCommand taskCommand, Long userId) {
        Task task = new Task();
        task.setDateCreated(LocalDate.now());
        task.setLastUpdated(LocalDate.now());
        task.setDescription(taskCommand.getDescription());
        task.setName(taskCommand.getName());
        User manager = userRepository.getById(userId);
        task.setManager(manager);
        task.setTaskStatus(TaskStatus.NEW_TASK);
        User user = userRepository.getReferenceById(taskCommand.getUserId());
        task.setUser(user);
        return taskRepository.save(task);

    }

    @Override
    public Task updateTaskStatus(TaskStatus taskStatus, Long taskId) {
        Task task = taskRepository.getById(taskId);
        task.setTaskStatus(taskStatus);
        task.setLastUpdated(LocalDate.now());
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = taskRepository.getById(taskId);
        taskRepository.deleteById(taskId);
    }

    @Override
    public List<Task> filter(FilterCommand filterCommand, Boolean isManager, User user) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> cq = criteriaBuilder.createQuery(Task.class);
        List<Predicate> predicates = new ArrayList<>();
        Root<Task> task = cq.from(Task.class);
        if (filterCommand.getUserId() != null) {
            predicates.add(criteriaBuilder.equal(task.get("user"), userRepository.getById(filterCommand.getUserId())));
        }
        if (filterCommand.getTaskStatus() != null) {
            predicates.add(criteriaBuilder.equal(task.get("taskStatus"), filterCommand.getTaskStatus()));
        }

        if (filterCommand.getDateCreated() != null){
            predicates.add(criteriaBuilder.equal(task.get("dateCreated"), filterCommand.getDateCreated()));
        }

        if (!isManager) {
            predicates.add(criteriaBuilder.equal(task.get("user"), user));
        }

        cq.where(criteriaBuilder.and(predicates.toArray(Predicate[]::new)));
        TypedQuery<Task> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public ModelAndView getModel(Boolean isManager, User user) {
        ModelAndView modelAndView = new ModelAndView();
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

    @Override
    public Task findByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }


}
