package com.tas.global.globalven.controller;

import com.tas.global.globalven.dto.GlRefCodeDto;
import com.tas.global.globalven.service.GlRefCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/glref-codes")
@CrossOrigin(origins = "*")
public class GlRefCodeController {
    
    @Autowired
    private GlRefCodeService glRefCodeService;
    
    /**
     * Get all GL reference codes from TasGlobalDB using GET_GLREF_CODES stored procedure
     * 
     * @return ResponseEntity with list of GlRefCodeDto objects
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HR_ADMIN', 'FINANCE_ADMIN')")
    public ResponseEntity<List<GlRefCodeDto>> getAllGlRefCodes() {
        try {
            List<GlRefCodeDto> glRefCodes = glRefCodeService.getAllGlRefCodes();
            
            if (glRefCodes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(glRefCodes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get active GL reference codes only
     * 
     * @return ResponseEntity with list of active GlRefCodeDto objects
     */
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HR_ADMIN', 'FINANCE_ADMIN', 'USER')")
    public ResponseEntity<List<GlRefCodeDto>> getActiveGlRefCodes() {
        try {
            List<GlRefCodeDto> glRefCodes = glRefCodeService.getActiveGlRefCodes();
            
            if (glRefCodes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(glRefCodes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get active GL reference codes for frontend dropdowns (Public access for authenticated users)
     * This endpoint is used by transactions.html and other frontend forms
     */
    @GetMapping("/active-for-dropdown")
    public ResponseEntity<List<GlRefCodeDto>> getActiveGlRefCodesForDropdown() {
        try {
            List<GlRefCodeDto> glRefCodes = glRefCodeService.getActiveGlRefCodes();
            
            if (glRefCodes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(glRefCodes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Search GL reference codes by term
     * 
     * @param searchTerm The search term to look for
     * @return ResponseEntity with list of matching GlRefCodeDto objects
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HR_ADMIN', 'FINANCE_ADMIN', 'USER')")
    public ResponseEntity<List<GlRefCodeDto>> searchGlRefCodes(@RequestParam String searchTerm) {
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            List<GlRefCodeDto> glRefCodes = glRefCodeService.searchGlRefCodes(searchTerm);
            
            if (glRefCodes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(glRefCodes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get GL reference code by specific code
     * 
     * @param code The specific code to find
     * @return ResponseEntity with GlRefCodeDto object if found
     */
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HR_ADMIN', 'FINANCE_ADMIN', 'USER')")
    public ResponseEntity<GlRefCodeDto> getGlRefCodeByCode(@PathVariable String code) {
        try {
            GlRefCodeDto glRefCode = glRefCodeService.getGlRefCodeByCode(code);
            
            if (glRefCode == null) {
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok(glRefCode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get all unique categories
     * 
     * @return ResponseEntity with list of category names
     */
    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HR_ADMIN', 'FINANCE_ADMIN', 'USER')")
    public ResponseEntity<List<String>> getAllCategories() {
        try {
            List<String> categories = glRefCodeService.getAllCategories();
            
            if (categories.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get GL reference codes by category
     * 
     * @param category The category to filter by
     * @return ResponseEntity with list of GlRefCodeDto objects
     */
    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HR_ADMIN', 'FINANCE_ADMIN', 'USER')")
    public ResponseEntity<List<GlRefCodeDto>> getGlRefCodesByCategory(@PathVariable String category) {
        try {
            List<GlRefCodeDto> glRefCodes = glRefCodeService.getGlRefCodesByCategory(category);
            
            if (glRefCodes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(glRefCodes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get statistics about GL reference codes
     * 
     * @return ResponseEntity with statistics object
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'HR_ADMIN', 'FINANCE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getGlRefCodeStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            long totalCount = glRefCodeService.getTotalGlRefCodeCount();
            long activeCount = glRefCodeService.getActiveGlRefCodeCount();
            long inactiveCount = totalCount - activeCount;
            
            statistics.put("total", totalCount);
            statistics.put("active", activeCount);
            statistics.put("inactive", inactiveCount);
            statistics.put("categories", glRefCodeService.getAllCategories().size());
            
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Execute stored procedure with custom parameters (for future extensibility)
     * 
     * @param parameters Map of parameter names and values
     * @return ResponseEntity with list of GlRefCodeDto objects
     */
    @PostMapping("/execute-with-params")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<GlRefCodeDto>> executeStoredProcedureWithParams(@RequestBody Map<String, Object> parameters) {
        try {
            List<GlRefCodeDto> glRefCodes = glRefCodeService.getGlRefCodesWithParams(parameters);
            
            if (glRefCodes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(glRefCodes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Health check endpoint to test SQL Server connection
     * 
     * @return ResponseEntity with connection status
     */
    @GetMapping("/health")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, String>> healthCheck() {
        try {
            // Try to fetch a small amount of data to test connection
            glRefCodeService.getTotalGlRefCodeCount();
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "UP");
            response.put("database", "TasGlobalDB");
            response.put("connection", "ACTIVE");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "DOWN");
            response.put("database", "TasGlobalDB");
            response.put("connection", "FAILED");
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }
    
    /**
     * Test SQL Server connection for GL Reference Codes (No authentication required for testing)
     */
    @GetMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testSqlServerConnection() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Try to get a simple count or execute a basic query
            List<GlRefCodeDto> testData = glRefCodeService.getAllGlRefCodes();
            
            result.put("status", "SUCCESS");
            result.put("connected", true);
            result.put("message", "SQL Server connection successful for GL Reference Codes");
            result.put("recordCount", testData.size());
            result.put("database", "TasGlobalDB (SQL Server)");
            result.put("storedProcedure", "GET_GLREF_CODES");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("connected", false);
            result.put("error", e.getMessage());
            result.put("database", "TasGlobalDB (SQL Server)");
            result.put("storedProcedure", "GET_GLREF_CODES");
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
        }
    }
    
    /**
     * Get sample GL reference codes for testing and preview (No authentication required)
     */
    @GetMapping("/sample-data")
    public ResponseEntity<Map<String, Object>> getSampleGlRefCodeData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<GlRefCodeDto> allCodes = glRefCodeService.getAllGlRefCodes();
            
            // Get first 5 records as sample
            List<GlRefCodeDto> sampleCodes = allCodes.stream()
                .limit(5)
                .collect(java.util.stream.Collectors.toList());
            
            result.put("status", "SUCCESS");
            result.put("totalRecords", allCodes.size());
            result.put("sampleSize", sampleCodes.size());
            result.put("sampleData", sampleCodes);
            result.put("database", "TasGlobalDB (SQL Server)");
            result.put("storedProcedure", "GET_GLREF_CODES");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("error", e.getMessage());
            result.put("database", "TasGlobalDB (SQL Server)");
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
        }
    }

    /**
     * Debug endpoint to check actual column names returned by stored procedure
     */
    @GetMapping("/debug-columns")
    public ResponseEntity<Map<String, Object>> debugStoredProcedureColumns() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Get raw data from repository to inspect column names
            List<GlRefCodeDto> testData = glRefCodeService.getAllGlRefCodes();
            
            result.put("status", "SUCCESS");
            result.put("recordCount", testData.size());
            result.put("message", "Check application logs for actual column names");
            result.put("note", "This endpoint helps debug column mapping issues");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("status", "FAILED");
            result.put("error", e.getMessage());
            result.put("stackTrace", e.getStackTrace());
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
        }
    }
}
