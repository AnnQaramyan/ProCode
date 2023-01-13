package com.example.procodetask.command;

import com.example.procodetask.model.User;
import org.springframework.lang.NonNull;

public class TaskCommand {
    @NonNull
    private String name;
    @NonNull
    private String description;

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
