package com.project.demo.rest.category;

import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROL')")
    public Category addCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping ("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROL')")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(category.getName());
                    existingCategory.setDescription(category.getDescription());
                    return categoryRepository.save(existingCategory);
                })
                .orElseGet(() -> {
                    category.setId(id);
                    return categoryRepository.save(category);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROL')")
    public void deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
    }

    @GetMapping("/me") // Get the authenticated user
    @PreAuthorize("isAuthenticated()")   // Check if the user is authenticated
    public User authenticatedUser() {  // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Get the current user's authentication
        return (User) authentication.getPrincipal(); // Return the current user
    }
}
