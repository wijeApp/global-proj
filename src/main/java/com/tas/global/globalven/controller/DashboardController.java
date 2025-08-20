package com.tas.global.globalven.controller;

import com.tas.global.globalven.service.CategoryService;
import com.tas.global.globalven.service.CountryService;
import com.tas.global.globalven.service.EmployeeService;
import com.tas.global.globalven.service.RatesService;
import com.tas.global.globalven.service.TransfersMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private RatesService ratesService;
    
    @Autowired
    private CountryService countryService;
    
    @Autowired
    private TransfersMainService transfersMainService;
    
    // Get all statistics for dashboard
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Employee stats
        Map<String, Object> employeeStats = new HashMap<>();
        employeeStats.put("total", employeeService.getTotalEmployeeCount());
        stats.put("employees", employeeStats);
        
        // Category stats
        Map<String, Object> categoryStats = new HashMap<>();
        categoryStats.put("total", categoryService.getTotalCategoryCount());
        categoryStats.put("active", categoryService.getActiveCategoryCount());
        categoryStats.put("inactive", categoryService.getInactiveCategoryCount());
        stats.put("categories", categoryStats);
        
        // Rates stats
        Map<String, Object> ratesStats = new HashMap<>();
        ratesStats.put("total", ratesService.getTotalRateCount());
        ratesStats.put("active", ratesService.getActiveRateCount());
        ratesStats.put("inactive", ratesService.getInactiveRateCount());
        stats.put("rates", ratesStats);
        
        // Country stats
        Map<String, Object> countryStats = new HashMap<>();
        countryStats.put("total", countryService.getTotalCountryCount());
        stats.put("countries", countryStats);
        
        // Transactions stats
        Map<String, Object> transactionStats = new HashMap<>();
        transactionStats.put("total", transfersMainService.getTotalTransactionCount());
        transactionStats.put("active", transfersMainService.getActiveTransactionCount());
        transactionStats.put("pending", transfersMainService.getPendingTransactionCount());
        transactionStats.put("processed", transfersMainService.getProcessedTransactionCount());
        stats.put("transactions", transactionStats);
        
        return ResponseEntity.ok(stats);
    }
}
