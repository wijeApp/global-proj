package com.tas.global.globalven;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "transfers_main")
public class TransfersMain {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
      @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rate_id", nullable = false)
    private Rates rate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "txn_type", nullable = false)
    private TransactionType transactionType;
    
    @Column(name = "txn_date", nullable = false)
    private LocalDateTime transactionDate;
    
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "hours_worked")
    private Double hoursWorked;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "reference_no")
    private String referenceNumber;
    
    @Column(name = "glrefcode")
    private String glrefcode;
    
    @Column(name = "status", nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, PROCESSED
    
    @Column(name = "currency", nullable = false)
    private String currency = "USD";
    
    @Column(name = "exchange_rate", precision = 10, scale = 4)
    private BigDecimal exchangeRate;
    
    @Column(name = "amount_base_currency", precision = 10, scale = 2)
    private BigDecimal amountInBaseCurrency;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @Column(name = "approved_by")
    private String approvedBy;
    
    @Column(name = "approved_date")
    private LocalDateTime approvedDate;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Default constructor
    public TransfersMain() {
        this.createdDate = LocalDateTime.now();
        this.transactionDate = LocalDateTime.now();
        this.status = "PENDING";
        this.isActive = true;
    }    // Constructor with required fields
    public TransfersMain(Employee employee, Rates rate, 
                        TransactionType transactionType, BigDecimal amount) {
        this();
        this.employee = employee;
        this.rate = rate;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Rates getRate() {
        return rate;
    }    public void setRate(Rates rate) {
        this.rate = rate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate != null ? transactionDate : LocalDateTime.now();
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

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getGlrefcode() {
        return glrefcode;
    }

    public void setGlrefcode(String glrefcode) {
        this.glrefcode = glrefcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getAmountInBaseCurrency() {
        return amountInBaseCurrency;
    }

    public void setAmountInBaseCurrency(BigDecimal amountInBaseCurrency) {
        this.amountInBaseCurrency = amountInBaseCurrency;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // Calculate amount in base currency
    public void calculateAmountInBaseCurrency() {
        if (this.amount != null && this.exchangeRate != null) {
            this.amountInBaseCurrency = this.amount.multiply(this.exchangeRate);
        }
    }

    // Business methods
    public void approve(String approver) {
        this.status = "APPROVED";
        this.approvedBy = approver;
        this.approvedDate = LocalDateTime.now();
        this.updatedBy = approver;
        this.updatedDate = LocalDateTime.now();
    }

    public void reject(String rejector) {
        this.status = "REJECTED";
        this.updatedBy = rejector;
        this.updatedDate = LocalDateTime.now();
    }

    public void process(String processor) {
        this.status = "PROCESSED";
        this.updatedBy = processor;
        this.updatedDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransfersMain that = (TransfersMain) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(referenceNumber, that.referenceNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, referenceNumber);
    }

    @Override
    public String toString() {        return "TransfersMain{" +
                "id=" + id +
                ", employee=" + (employee != null ? employee.getId() : null) +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", status='" + status + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
