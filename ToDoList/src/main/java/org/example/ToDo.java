package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ToDo {
    static int nextId = 1;
    private final int id;
    private String name;
    private String description;
    private boolean completed;
    private final String initialDate;
    private String dueDate;

    @JsonCreator
    public ToDo(@JsonProperty("name") String name, @JsonProperty("description") String description, @JsonProperty("dueDate") String dueDate) {
        this.id = ToDo.nextId;
        ToDo.nextId++;
        this.name = name;
        this.description = description;
        this.completed = false;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime initialDateLocalDateTime = LocalDateTime.now();
        this.initialDate = initialDateLocalDateTime.format(dateFormat);
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
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

    public boolean isCompleted() {
        return completed;
    }

    public void toggleCompleted() {
        this.completed = !this.completed;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}