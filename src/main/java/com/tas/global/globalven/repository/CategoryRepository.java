package com.tas.global.globalven.repository;

import com.tas.global.globalven.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Find category by name
    Optional<Category> findByCatName(String catName);
    
    // Find categories by active status
    List<Category> findByIsActive(Boolean isActive);
    
    // Find categories by name containing the given string (case-insensitive)
    @Query("SELECT c FROM Category c WHERE LOWER(c.catName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Category> findByCatNameContainingIgnoreCase(@Param("name") String name);
    
    // Check if a category exists by name
    boolean existsByCatName(String catName);
    
    // Count active categories
    long countByIsActive(Boolean isActive);
}
