package com.example.lab6.services;

import com.example.lab6.models.Category;
import com.example.lab6.models.Task;
import com.example.lab6.models.UserModel;
import com.example.lab6.repositories.CategoryRepository;
import com.example.lab6.repositories.TaskRepository;
import com.example.lab6.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Task> getTasksByUser(String username, Pageable pageable) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return taskRepository.findByUser(user, pageable);
    }

    public Task addTask(String username, Task task, Long categoryId) {
        UserModel user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
            task.setCategory(category);
        }

        return taskRepository.save(task);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Task> searchTasks(String username, String keyword) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return taskRepository.searchTasksByKeyword(keyword, user);
    }

    public List<Task> filterTasks(String username, Long categoryId) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return taskRepository.filterTasksByCategory(categoryId, user);
    }
}