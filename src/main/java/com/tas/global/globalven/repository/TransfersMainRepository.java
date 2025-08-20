package com.tas.global.globalven.repository;

import com.tas.global.globalven.TransfersMain;
import com.tas.global.globalven.Employee;
import com.tas.global.globalven.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Repository
public interface TransfersMainRepository extends JpaRepository<TransfersMain, Long> {
      // Find by employee
    List<TransfersMain> findByEmployee(Employee employee);
    List<TransfersMain> findByEmployeeAndIsActiveTrue(Employee employee);
    
    // Find by transaction type
    List<TransfersMain> findByTransactionType(TransactionType transactionType);
    List<TransfersMain> findByTransactionTypeAndIsActiveTrue(TransactionType transactionType);
    
    // Find by status
    List<TransfersMain> findByStatus(String status);
    List<TransfersMain> findByStatusAndIsActiveTrue(String status);
    
    // Find by date range
    List<TransfersMain> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<TransfersMain> findByTransactionDateBetweenAndIsActiveTrue(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find by amount range
    List<TransfersMain> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    List<TransfersMain> findByAmountBetweenAndIsActiveTrue(BigDecimal minAmount, BigDecimal maxAmount);
    
    // Find by currency
    List<TransfersMain> findByCurrency(String currency);
    List<TransfersMain> findByCurrencyAndIsActiveTrue(String currency);
    
    // Custom queries
    @Query("SELECT t FROM TransfersMain t WHERE t.employee = :employee AND t.transactionDate >= :startDate AND t.transactionDate <= :endDate AND t.isActive = true")
    List<TransfersMain> findEmployeeTransactionsByDateRange(
        @Param("employee") Employee employee,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT SUM(t.amount) FROM TransfersMain t WHERE t.employee = :employee AND t.transactionType = :txnType AND t.status = 'PROCESSED' AND t.isActive = true")
    BigDecimal sumProcessedAmountByEmployeeAndType(
        @Param("employee") Employee employee,
        @Param("txnType") TransactionType txnType
    );
    
    @Query("SELECT COUNT(t) FROM TransfersMain t WHERE t.status = :status AND t.isActive = true")
    long countByStatusAndActive(@Param("status") String status);
    
    @Query("SELECT t FROM TransfersMain t WHERE t.employee = :employee AND t.status = 'PENDING' AND t.isActive = true ORDER BY t.transactionDate DESC")
    List<TransfersMain> findPendingTransactionsByEmployee(@Param("employee") Employee employee);
      @Query("SELECT t FROM TransfersMain t WHERE t.status = 'PENDING' AND t.isActive = true ORDER BY t.transactionDate ASC")
    List<TransfersMain> findAllPendingTransactions();
    
    @Query("SELECT t FROM TransfersMain t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.referenceNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TransfersMain> searchTransactions(@Param("keyword") String keyword);
      @Query("SELECT t FROM TransfersMain t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%')) AND t.isActive = true")
    List<TransfersMain> searchTransactionsByDescription(@Param("description") String description);
    
    @Query("SELECT t FROM TransfersMain t WHERE LOWER(t.referenceNumber) LIKE LOWER(CONCAT('%', :referenceNumber, '%')) AND t.isActive = true")
    List<TransfersMain> searchTransactionsByReferenceNumber(@Param("referenceNumber") String referenceNumber);
    
    // Find by glrefcode
    List<TransfersMain> findByGlrefcode(String glrefcode);
    List<TransfersMain> findByGlrefcodeAndIsActiveTrue(String glrefcode);
    
    @Query("SELECT t FROM TransfersMain t WHERE LOWER(t.glrefcode) LIKE LOWER(CONCAT('%', :glrefcode, '%')) AND t.isActive = true")
    List<TransfersMain> searchTransactionsByGlrefcode(@Param("glrefcode") String glrefcode);
}
