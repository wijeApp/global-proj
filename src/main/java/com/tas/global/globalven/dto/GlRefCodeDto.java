package com.tas.global.globalven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlRefCodeDto {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("code")
    private String code;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("isActive")
    private Boolean isActive;
    
    @JsonProperty("createdDate")
    private String createdDate;
    
    @JsonProperty("updatedDate")
    private String updatedDate;
    
    // Default constructor
    public GlRefCodeDto() {}
    
    // Constructor with all fields
    public GlRefCodeDto(Long id, String code, String description, String category, 
                       Boolean isActive, String createdDate, String updatedDate) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.category = category;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public String getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    
    public String getUpdatedDate() {
        return updatedDate;
    }
    
    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    @Override
    public String toString() {
        return "GlRefCodeDto{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", isActive=" + isActive +
                ", createdDate='" + createdDate + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                '}';
    }
}
