package com.project.demo.logic.entity.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE %?1%")
    List<Product> findProductsByCharacterInName(String character);

    @Query("SELECT p FROM Product p WHERE p.name = ?1")
    Optional<Product> findByName(String name);
}