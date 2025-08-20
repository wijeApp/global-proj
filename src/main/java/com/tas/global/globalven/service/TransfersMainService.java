package com.tas.global.globalven.service;

import com.tas.global.globalven.TransfersMain;
import com.tas.global.globalven.Employee;
import com.tas.global.globalven.Rates;
import com.tas.global.globalven.TransactionType;
import com.tas.global.globalven.repository.TransfersMainRepository;
import com.tas.global.globalven.repository.EmployeeRepository;
import com.tas.global.globalven.repository.RatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransfersMainService {
      @Autowired
    private TransfersMainRepository transfersMainRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private RatesRepository ratesRepository;// CREATE
    public TransfersMain saveTransfer(TransfersMain transfer) {
        System.out.println("TransfersMainService.saveTransfer called with: " + transfer);
        
        // Set default values if not provided
        if (transfer.getCreatedDate() == null) {
            transfer.setCreatedDate(LocalDateTime.now());
        }
        
        if (transfer.getTransactionDate() == null) {
            transfer.setTransactionDate(LocalDateTime.now());
        }
        
        if (transfer.getStatus() == null || transfer.getStatus().isEmpty()) {
            transfer.setStatus("PENDING");
        }
        
        if (transfer.getIsActive() == null) {
            transfer.setIsActive(true);
        }
          // Check for required relationships and fetch from database if only IDs are provided
        if (transfer.getEmployee() == null) {
            throw new IllegalArgumentException("Employee is required");
        }
        
        if (transfer.getRate() == null) {
            throw new IllegalArgumentException("Rate is required");
        }
          // Fetch and attach full entities if only IDs are provided
        Employee employee = transfer.getEmployee();
        if (employee.getId() != null) {
            // Always fetch the full employee entity from database to ensure it's managed by Hibernate
            Optional<Employee> fullEmployee = employeeRepository.findById(employee.getId());
            if (fullEmployee.isPresent()) {
                transfer.setEmployee(fullEmployee.get());
            } else {
                throw new IllegalArgumentException("Employee with ID " + employee.getId() + " not found");
            }
        }
          Rates rate = transfer.getRate();
        if (rate.getId() != null) {
            // Always fetch the full rate entity from database to ensure it's managed by Hibernate
            Optional<Rates> fullRate = ratesRepository.findById(rate.getId());
            if (fullRate.isPresent()) {
                transfer.setRate(fullRate.get());
            } else {
                throw new IllegalArgumentException("Rate with ID " + rate.getId() + " not found");
            }
        }
        
        try {
            TransfersMain savedTransfer = transfersMainRepository.save(transfer);
            System.out.println("Transfer saved successfully: " + savedTransfer);
            return savedTransfer;
        } catch (Exception e) {
            System.err.println("Error saving transfer: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // READ
    public List<TransfersMain> getAllTransfers() {
        return transfersMainRepository.findAll();
    }
    
    public Optional<TransfersMain> getTransferById(Long id) {
        return transfersMainRepository.findById(id);
    }
      public List<TransfersMain> getTransfersByEmployee(Employee employee) {
        return transfersMainRepository.findByEmployeeAndIsActiveTrue(employee);
    }
    
    public List<TransfersMain> getTransfersByType(TransactionType type) {
        return transfersMainRepository.findByTransactionTypeAndIsActiveTrue(type);
    }
    
    public List<TransfersMain> getTransfersByStatus(String status) {
        return transfersMainRepository.findByStatusAndIsActiveTrue(status);
    }
    
    public List<TransfersMain> getTransfersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transfersMainRepository.findByTransactionDateBetweenAndIsActiveTrue(startDate, endDate);
    }
    
    public List<TransfersMain> getTransfersByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        return transfersMainRepository.findByAmountBetweenAndIsActiveTrue(minAmount, maxAmount);
    }
    
    public List<TransfersMain> getTransfersByCurrency(String currency) {
        return transfersMainRepository.findByCurrencyAndIsActiveTrue(currency);
    }
    
    // UPDATE
    public TransfersMain updateTransfer(Long id, TransfersMain transferDetails) {
        TransfersMain transfer = transfersMainRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transfer not found"));
              transfer.setEmployee(transferDetails.getEmployee());
        transfer.setRate(transferDetails.getRate());
        transfer.setTransactionType(transferDetails.getTransactionType());
        transfer.setAmount(transferDetails.getAmount());
        transfer.setHoursWorked(transferDetails.getHoursWorked());
        transfer.setDescription(transferDetails.getDescription());
        transfer.setCurrency(transferDetails.getCurrency());
        transfer.setExchangeRate(transferDetails.getExchangeRate());
        transfer.setNotes(transferDetails.getNotes());
        transfer.setGlrefcode(transferDetails.getGlrefcode());
        transfer.setUpdatedDate(LocalDateTime.now());
        
        return transfersMainRepository.save(transfer);
    }
    
    // DELETE (Soft delete)
    public boolean deleteTransfer(Long id) {
        Optional<TransfersMain> transfer = transfersMainRepository.findById(id);
        if (transfer.isPresent()) {
            transfer.get().setIsActive(false);
            transfer.get().setUpdatedDate(LocalDateTime.now());
            transfersMainRepository.save(transfer.get());
            return true;
        }
        return false;
    }
    
    // Business Methods
    public TransfersMain approveTransfer(Long id, String approver) {
        TransfersMain transfer = transfersMainRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transfer not found"));
        transfer.approve(approver);
        return transfersMainRepository.save(transfer);
    }
    
    public TransfersMain rejectTransfer(Long id, String rejector) {
        TransfersMain transfer = transfersMainRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transfer not found"));
        transfer.reject(rejector);
        return transfersMainRepository.save(transfer);
    }
    
    public TransfersMain processTransfer(Long id, String processor) {
        TransfersMain transfer = transfersMainRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transfer not found"));
        transfer.process(processor);
        return transfersMainRepository.save(transfer);
    }
    
    // Reporting Methods
    public List<TransfersMain> getEmployeeTransactionsByDateRange(Employee employee, LocalDateTime startDate, LocalDateTime endDate) {
        return transfersMainRepository.findEmployeeTransactionsByDateRange(employee, startDate, endDate);
    }
    
    public BigDecimal getTotalProcessedAmountByEmployeeAndType(Employee employee, TransactionType type) {
        return transfersMainRepository.sumProcessedAmountByEmployeeAndType(employee, type);
    }
    
    public long getCountByStatus(String status) {
        return transfersMainRepository.countByStatusAndActive(status);
    }
    
    public List<TransfersMain> getPendingTransactionsByEmployee(Employee employee) {
        return transfersMainRepository.findPendingTransactionsByEmployee(employee);
    }
      public List<TransfersMain> getAllPendingTransactions() {
        return transfersMainRepository.findAllPendingTransactions();
    }
    
    public List<TransfersMain> searchTransactions(String keyword) {
        return transfersMainRepository.searchTransactions(keyword);
    }
      public List<TransfersMain> searchTransactionsByDescription(String description) {
        return transfersMainRepository.searchTransactionsByDescription(description);
    }
    
    public List<TransfersMain> searchTransactionsByReferenceNumber(String referenceNumber) {
        return transfersMainRepository.searchTransactionsByReferenceNumber(referenceNumber);
    }
    
    public List<TransfersMain> getTransfersByGlrefcode(String glrefcode) {
        return transfersMainRepository.findByGlrefcodeAndIsActiveTrue(glrefcode);
    }
    
    public List<TransfersMain> searchTransactionsByGlrefcode(String glrefcode) {
        return transfersMainRepository.searchTransactionsByGlrefcode(glrefcode);
    }
    
    // Statistics Methods for Dashboard
    public long getTotalTransactionCount() {
        return transfersMainRepository.count();
    }
    
    public long getActiveTransactionCount() {
        return transfersMainRepository.findAll().stream()
            .filter(t -> t.getIsActive() != null && t.getIsActive())
            .count();
    }
    
    public long getPendingTransactionCount() {
        return getCountByStatus("PENDING");
    }
    
    public long getProcessedTransactionCount() {
        return getCountByStatus("PROCESSED");
    }
}
