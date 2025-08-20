package com.tas.global.globalven.service;

import com.tas.global.globalven.Rates;
import com.tas.global.globalven.repository.RatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
public class RatesService {
    
    @Autowired
    private RatesRepository ratesRepository;
    
    // Create or Update Rate
    public Rates saveRate(Rates rate) {
        if (rate.getCreatedDate() == null) {
            rate.setCreatedDate(LocalDateTime.now());
        }
        rate.setUpdatedDate(LocalDateTime.now());
        
        return ratesRepository.save(rate);
    }
    
    // Get all rates
    public List<Rates> getAllRates() {
        return ratesRepository.findAll();
    }
    
    // Get active rates
    public List<Rates> getActiveRates() {
        return ratesRepository.findByIsActive(true);
    }
    
    // Get inactive rates
    public List<Rates> getInactiveRates() {
        return ratesRepository.findByIsActive(false);
    }
    
    // Get rate by ID
    public Optional<Rates> getRateById(Long id) {
        return ratesRepository.findById(id);
    }
      // Get rates by employee category
    public List<Rates> getRatesByCategory(String category) {
        return ratesRepository.findByEmpCategory(category);
    }
    
    // Get rates by rate name
    public List<Rates> getRatesByName(String rateName) {
        return ratesRepository.findByRateName(rateName);
    }
    
    // Search rates by rate name
    public List<Rates> searchRatesByName(String rateName) {
        return ratesRepository.findByRateNameContainingIgnoreCase(rateName);
    }
    
    // Search rates by category
    public List<Rates> searchRatesByCategory(String category) {
        return ratesRepository.findByEmpCategoryContainingIgnoreCase(category);
    }
    
    // Get rates by currency
    public List<Rates> getRatesByCurrency(String currency) {
        return ratesRepository.findByCurrency(currency);
    }
    
    // Get rates by rate type
    public List<Rates> getRatesByType(String rateType) {
        return ratesRepository.findByRateType(rateType);
    }
    
    // Get rates by amount range
    public List<Rates> getRatesByAmountRange(Double minAmount, Double maxAmount) {
        return ratesRepository.findByRateAmountBetween(minAmount, maxAmount);
    }
    
    // Get currently effective rates
    public List<Rates> getCurrentlyEffectiveRates() {
        return ratesRepository.findCurrentlyEffectiveRates(LocalDateTime.now());
    }
      // Update rate
    public Rates updateRate(Long id, Rates rateDetails) {
        Optional<Rates> rateOptional = ratesRepository.findById(id);
        
        if (rateOptional.isPresent()) {
            Rates rate = rateOptional.get();
            
            // Update fields
            rate.setEmpCategory(rateDetails.getEmpCategory());
            rate.setRateName(rateDetails.getRateName());
            rate.setRateAmount(rateDetails.getRateAmount());
            rate.setCurrency(rateDetails.getCurrency());
            rate.setRateType(rateDetails.getRateType());
            rate.setEffectiveDate(rateDetails.getEffectiveDate());
            rate.setExpiryDate(rateDetails.getExpiryDate());
            rate.setIsActive(rateDetails.getIsActive());
            rate.setUpdatedDate(LocalDateTime.now());
            
            return ratesRepository.save(rate);
        }
        
        return null; // Rate not found
    }
    
    // Activate/Deactivate rate
    public Rates toggleRateStatus(Long id, boolean isActive) {
        Optional<Rates> rateOptional = ratesRepository.findById(id);
        
        if (rateOptional.isPresent()) {
            Rates rate = rateOptional.get();
            rate.setIsActive(isActive);
            rate.setUpdatedDate(LocalDateTime.now());
            
            return ratesRepository.save(rate);
        }
        
        return null; // Rate not found
    }
    
    // Delete rate by ID
    public boolean deleteRate(Long id) {
        if (ratesRepository.existsById(id)) {
            ratesRepository.deleteById(id);
            return true;
        }
        return false;
    }
      // Check if rate exists by employee category
    public boolean existsByCategory(String category) {
        return ratesRepository.existsByEmpCategory(category);
    }
    
    // Check if rate exists by rate name
    public boolean existsByRateName(String rateName) {
        return ratesRepository.existsByRateName(rateName);
    }
    
    // Get count of active rates
    public long getActiveRateCount() {
        return ratesRepository.countByIsActive(true);
    }
    
    // Get count of inactive rates
    public long getInactiveRateCount() {
        return ratesRepository.countByIsActive(false);
    }
    
    // Get total rate count
    public long getTotalRateCount() {
        return ratesRepository.count();
    }
    
    // Get rate statistics
    public Map<String, Double> getRateStatistics() {
        Map<String, Double> stats = new HashMap<>();
        
        Double highest = ratesRepository.findHighestRateAmount();
        Double lowest = ratesRepository.findLowestRateAmount();
        Double average = ratesRepository.findAverageRateAmount();
        
        stats.put("highest", highest != null ? highest : 0.0);
        stats.put("lowest", lowest != null ? lowest : 0.0);
        stats.put("average", average != null ? average : 0.0);
        
        return stats;
    }
}
