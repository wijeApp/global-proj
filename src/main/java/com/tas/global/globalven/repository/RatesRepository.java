package com.tas.global.globalven.repository;

import com.tas.global.globalven.Rates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RatesRepository extends JpaRepository<Rates, Long> {
      // Find rates by employee category
    List<Rates> findByEmpCategory(String empCategory);
    
    // Find rates by rate name
    List<Rates> findByRateName(String rateName);
    
    // Find rates by rate name containing the given string (case-insensitive)
    @Query("SELECT r FROM Rates r WHERE LOWER(r.rateName) LIKE LOWER(CONCAT('%', :rateName, '%'))")
    List<Rates> findByRateNameContainingIgnoreCase(@Param("rateName") String rateName);
    
    // Find rates by active status
    List<Rates> findByIsActive(Boolean isActive);
    
    // Find rates by currency
    List<Rates> findByCurrency(String currency);
    
    // Find rates by rate type
    List<Rates> findByRateType(String rateType);
    
    // Find rates by employee category containing the given string (case-insensitive)
    @Query("SELECT r FROM Rates r WHERE LOWER(r.empCategory) LIKE LOWER(CONCAT('%', :category, '%'))")
    List<Rates> findByEmpCategoryContainingIgnoreCase(@Param("category") String category);
    
    // Find rates by amount range
    @Query("SELECT r FROM Rates r WHERE r.rateAmount BETWEEN :minAmount AND :maxAmount")
    List<Rates> findByRateAmountBetween(@Param("minAmount") Double minAmount, @Param("maxAmount") Double maxAmount);
    
    // Find rates that are currently effective (within effective and expiry date)
    @Query("SELECT r FROM Rates r WHERE r.isActive = true AND " +
           "(r.effectiveDate IS NULL OR r.effectiveDate <= :currentDate) AND " +
           "(r.expiryDate IS NULL OR r.expiryDate >= :currentDate)")
    List<Rates> findCurrentlyEffectiveRates(@Param("currentDate") LocalDateTime currentDate);
    
    // Count rates by employee category
    long countByEmpCategory(String empCategory);
    
    // Count active rates
    long countByIsActive(Boolean isActive);
    
    // Get highest rate amount
    @Query("SELECT MAX(r.rateAmount) FROM Rates r")
    Double findHighestRateAmount();
    
    // Get lowest rate amount
    @Query("SELECT MIN(r.rateAmount) FROM Rates r")
    Double findLowestRateAmount();
    
    // Get average rate amount
    @Query("SELECT AVG(r.rateAmount) FROM Rates r")
    Double findAverageRateAmount();
      // Check if rate exists by employee category
    boolean existsByEmpCategory(String empCategory);
    
    // Check if rate exists by rate name
    boolean existsByRateName(String rateName);
}
