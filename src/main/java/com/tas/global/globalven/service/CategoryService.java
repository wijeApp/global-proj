package com.tas.global.globalven.service;

import com.tas.global.globalven.Category;
import com.tas.global.globalven.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    // Create or Update Category
    public Category saveCategory(Category category) {
        if (category.getCreatedDate() == null) {
            category.setCreatedDate(LocalDateTime.now());
        }
        category.setUpdatedDate(LocalDateTime.now());
        
        return categoryRepository.save(category);
    }
    
    // Get all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    // Get active categories
    public List<Category> getActiveCategories() {
        return categoryRepository.findByIsActive(true);
    }
    
    // Get inactive categories
    public List<Category> getInactiveCategories() {
        return categoryRepository.findByIsActive(false);
    }
    
    // Get category by ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
    // Get category by name
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByCatName(name);
    }
    
    // Search categories by name
    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByCatNameContainingIgnoreCase(name);
    }
    
    // Update category
    public Category updateCategory(Long id, Category categoryDetails) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            
            // Update fields
            category.setCatName(categoryDetails.getCatName());
            category.setDescription(categoryDetails.getDescription());
            category.setIsActive(categoryDetails.getIsActive());
            category.setUpdatedDate(LocalDateTime.now());
            
            return categoryRepository.save(category);
        }
        
        return null; // Category not found
    }
    
    // Activate/Deactivate category
    public Category toggleCategoryStatus(Long id, boolean isActive) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setIsActive(isActive);
            category.setUpdatedDate(LocalDateTime.now());
            
            return categoryRepository.save(category);
        }
        
        return null; // Category not found
    }
    
    // Delete category by ID
    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Check if category exists by name
    public boolean existsByName(String name) {
        return categoryRepository.existsByCatName(name);
    }
    
    // Get count of active categories
    public long getActiveCategoryCount() {
        return categoryRepository.countByIsActive(true);
    }
    
    // Get count of inactive categories
    public long getInactiveCategoryCount() {
        return categoryRepository.countByIsActive(false);
    }
    
    // Get total category count
    public long getTotalCategoryCount() {
        return categoryRepository.count();
    }
}
