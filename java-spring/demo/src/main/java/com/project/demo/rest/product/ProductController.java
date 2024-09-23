package com.project.demo.rest.product;

import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import com.project.demo.logic.entity.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROL')")
    public Product addProduct(@RequestBody Product product) {
        Optional<Category> optionalCategory = categoryRepository.findById(product.getCategory().getId());

        if (optionalCategory.isEmpty()) {

            return null;
        }

        product.setCategory(optionalCategory.get());
        Product newProduct = productRepository.save(product);
        return ResponseEntity.ok(newProduct).getBody();
    }

    @PutMapping ("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROL')")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setStock(product.getStock());
                    return productRepository.save(existingProduct);
                })
                .orElseGet(() -> {
                    product.setId(id);
                    return productRepository.save(product);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROL')")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    @GetMapping("/me") // Get the authenticated user
    @PreAuthorize("isAuthenticated()")   // Check if the user is authenticated
    public User authenticatedUser() {  // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Get the current user's authentication
        return (User) authentication.getPrincipal(); // Return the current user
    }
}
