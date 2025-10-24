package com.example.api.controller;

import com.example.api.entity.User;
import com.example.api.entity.Category;
import com.example.api.repository.UserRepository;
import com.example.api.repository.CategoryRepository;
import com.example.api.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private CourseRepository courseRepo;

    @GetMapping("/database")
    public Map<String, Object> checkDatabase() {
        Map<String, Object> result = new HashMap<>();

        try {
            long userCount = userRepo.count();
            long categoryCount = categoryRepo.count();
            long courseCount = courseRepo.count();

            result.put("status", "success");
            result.put("users_count", userCount);
            result.put("categories_count", categoryCount);
            result.put("courses_count", courseCount);

            // Listar usuarios
            Iterable<User> users = userRepo.findAll();
            Map<Long, String> userList = new HashMap<>();
            users.forEach(user -> userList.put(user.getId(), user.getUsername() + " - " + user.getEmail()));
            result.put("users", userList);

            // Listar categorías
            Iterable<Category> categories = categoryRepo.findAll();
            Map<Long, String> categoryList = new HashMap<>();
            categories.forEach(cat -> categoryList.put(cat.getId(), cat.getName()));
            result.put("categories", categoryList);

        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }

        return result;
    }
}