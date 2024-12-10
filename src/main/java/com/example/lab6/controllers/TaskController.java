package com.example.lab6.controllers;

import com.example.lab6.models.Task;
import com.example.lab6.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/paginated")
    @ResponseBody
    public Page<Task> getPaginatedTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String username = userDetails.getUsername();
        return taskService.getTasksByUser(username, PageRequest.of(page, size));
    }

    @GetMapping("/add")
    public String showAddTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("categories", taskService.getAllCategories());
        return "add_task";
    }

    @PostMapping("/add")
    public String addTask(@AuthenticationPrincipal UserDetails userDetails,
                          @ModelAttribute Task task,
                          @RequestParam(required = false) Long categoryId) {
        taskService.addTask(userDetails.getUsername(), task, categoryId);
        return "redirect:/home";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Task> searchTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String keyword) {
        String username = userDetails.getUsername();
        return taskService.searchTasks(username, keyword);
    }

    @GetMapping("/filter")
    @ResponseBody
    public List<Task> filterTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long categoryId) {
        String username = userDetails.getUsername();
        return taskService.filterTasks(username, categoryId);
    }
}