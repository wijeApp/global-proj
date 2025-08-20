package com.tas.global.globalven.dto;

import com.tas.global.globalven.TransactionType;
import java.math.BigDecimal;

public class CreateTransferRequest {
    private Long employeeId;
    private Long rateId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private Double hoursWorked;
    private String description;
    private String currency;
    private String notes;
    private String glrefcode;
    
    // Constructors
    public CreateTransferRequest() {}
    
    // Getters and Setters
    public Long getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    
    public Long getRateId() {
        return rateId;
    }
    
    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }
    
    public TransactionType getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Double getHoursWorked() {
        return hoursWorked;
    }
    
    public void setHoursWorked(Double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getGlrefcode() {
        return glrefcode;
    }
    
    public void setGlrefcode(String glrefcode) {
        this.glrefcode = glrefcode;
    }
}
