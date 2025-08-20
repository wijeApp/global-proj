package com.tas.global.globalven;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "rates")
public class Rates {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
      @Column(name = "emp_category", nullable = false)
    private String empCategory;
    
    @Column(name = "rate_name")
    private String rateName;
    
    @Column(name = "rate_amount", nullable = false)
    private Double rateAmount;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "currency")
    private String currency = "USD";
    
    @Column(name = "rate_type")
    private String rateType = "hourly"; // hourly, daily, monthly
    
    @Column(name = "effective_date")
    private LocalDateTime effectiveDate;
    
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    // Default constructor
    public Rates() {
    }
      // Constructor with all fields
    public Rates(Long id, String empCategory, String rateName, Double rateAmount, Long userId,
                String currency, String rateType, LocalDateTime effectiveDate, 
                LocalDateTime expiryDate, Boolean isActive, 
                LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.empCategory = empCategory;
        this.rateName = rateName;
        this.rateAmount = rateAmount;
        this.userId = userId;
        this.currency = currency;
        this.rateType = rateType;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
      // Constructor without id (for creating new rates)
    public Rates(String empCategory, String rateName, Double rateAmount, String currency, String rateType) {
        this.empCategory = empCategory;
        this.rateName = rateName;
        this.rateAmount = rateAmount;
        this.currency = currency;
        this.rateType = rateType;
        this.isActive = true;
        this.effectiveDate = LocalDateTime.now();
        this.createdDate = LocalDateTime.now();
    }
      // Basic constructor
    public Rates(String empCategory, String rateName, Double rateAmount) {
        this.empCategory = empCategory;
        this.rateName = rateName;
        this.rateAmount = rateAmount;
        this.isActive = true;
        this.createdDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmpCategory() {
        return empCategory;
    }
      public void setEmpCategory(String empCategory) {
        this.empCategory = empCategory;
    }
    
    public String getRateName() {
        return rateName;
    }
    
    public void setRateName(String rateName) {
        this.rateName = rateName;
    }
    
    public Double getRateAmount() {
        return rateAmount;
    }
    
    public void setRateAmount(Double rateAmount) {
        this.rateAmount = rateAmount;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getRateType() {
        return rateType;
    }
    
    public void setRateType(String rateType) {
        this.rateType = rateType;
    }
    
    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }
    
    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
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
        return "Rates{" +
                "id=" + id +
                ", empCategory='" + empCategory + '\'' +
                ", rateName='" + rateName + '\'' +
                ", rateAmount=" + rateAmount +
                ", userId=" + userId +
                ", currency='" + currency + '\'' +
                ", rateType='" + rateType + '\'' +
                ", effectiveDate=" + effectiveDate +
                ", expiryDate=" + expiryDate +
                ", isActive=" + isActive +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rates rates = (Rates) o;
        return Objects.equals(id, rates.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
