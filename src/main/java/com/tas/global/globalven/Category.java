package com.tas.global.globalven;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "categories")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private Long catId;
    
    @Column(name = "cat_name", nullable = false, unique = true)
    private String catName;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    // Default constructor
    public Category() {
    }
    
    // Constructor with all fields
    public Category(Long catId, String catName, String description, Boolean isActive, 
                   LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.catId = catId;
        this.catName = catName;
        this.description = description;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
    
    // Constructor without id (for creating new categories)
    public Category(String catName, String description) {
        this.catName = catName;
        this.description = description;
        this.isActive = true;
        this.createdDate = LocalDateTime.now();
    }
    
    // Simple constructor with just the name
    public Category(String catName) {
        this.catName = catName;
        this.isActive = true;
        this.createdDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getCatId() {
        return catId;
    }
    
    public void setCatId(Long catId) {
        this.catId = catId;
    }
    
    public String getCatName() {
        return catName;
    }
    
    public void setCatName(String catName) {
        this.catName = catName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
    
    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "catId=" + catId +
                ", catName='" + catName + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(catId, category.catId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(catId);
    }
}
