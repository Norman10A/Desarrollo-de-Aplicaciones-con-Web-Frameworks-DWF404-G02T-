package com.example.api.controller;

import com.example.api.entity.Category;
import com.example.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryRepo.findById(id);
        return category.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        try {
            if (categoryRepo.existsByName(category.getName())) {
                return ResponseEntity.badRequest().build();
            }

            Category savedCategory = categoryRepo.save(category);
            return ResponseEntity.ok(savedCategory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        Optional<Category> categoryOpt = categoryRepo.findById(id);
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Category category = categoryOpt.get();
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        Category updatedCategory = categoryRepo.save(category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        if (categoryRepo.existsById(id)) {
            categoryRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}