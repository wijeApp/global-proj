package com.tas.global.globalven.service;

import com.tas.global.globalven.dto.GlRefCodeDto;
import com.tas.global.globalven.repository.GlRefCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GlRefCodeService {
    
    @Autowired
    private GlRefCodeRepository glRefCodeRepository;
    
    /**
     * Get all GL reference codes from TasGlobalDB using stored procedure
     * @return List of GlRefCodeDto objects
     */
    public List<GlRefCodeDto> getAllGlRefCodes() {
        try {
            return glRefCodeRepository.getGlRefCodes();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching GL reference codes: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get GL reference codes with parameters (for future use)
     * @param parameters Map of parameter names and values
     * @return List of GlRefCodeDto objects
     */
    public List<GlRefCodeDto> getGlRefCodesWithParams(Map<String, Object> parameters) {
        try {
            return glRefCodeRepository.getGlRefCodesWithParams(parameters);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching GL reference codes with parameters: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get active GL reference codes only
     * @return List of active GlRefCodeDto objects
     */
    public List<GlRefCodeDto> getActiveGlRefCodes() {
        try {
            return getAllGlRefCodes().stream()
                    .filter(code -> code.getIsActive() != null && code.getIsActive())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching active GL reference codes: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search GL reference codes by term (code or description)
     * @param searchTerm The search term
     * @return List of matching GlRefCodeDto objects
     */
    public List<GlRefCodeDto> searchGlRefCodes(String searchTerm) {
        try {
            String lowerSearchTerm = searchTerm.toLowerCase();
            return getAllGlRefCodes().stream()
                    .filter(code -> 
                        (code.getCode() != null && code.getCode().toLowerCase().contains(lowerSearchTerm)) ||
                        (code.getDescription() != null && code.getDescription().toLowerCase().contains(lowerSearchTerm))
                    )
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching GL reference codes: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get GL reference code by specific code
     * @param code The specific code to find
     * @return GlRefCodeDto object if found, null otherwise
     */
    public GlRefCodeDto getGlRefCodeByCode(String code) {
        try {
            List<GlRefCodeDto> allCodes = getAllGlRefCodes();
            return allCodes.stream()
                    .filter(refCode -> refCode.getCode() != null && 
                                      refCode.getCode().equalsIgnoreCase(code))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching GL reference code by code: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get count of total GL reference codes
     * @return Total count of GL reference codes
     */
    public long getTotalGlRefCodeCount() {
        try {
            return getAllGlRefCodes().size();
        } catch (Exception e) {
            throw new RuntimeException("Error getting total GL reference code count: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get count of active GL reference codes
     * @return Count of active GL reference codes
     */
    public long getActiveGlRefCodeCount() {
        try {
            return getActiveGlRefCodes().size();
        } catch (Exception e) {
            throw new RuntimeException("Error getting active GL reference code count: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all unique categories from GL reference codes
     * @return List of unique categories
     */
    public List<String> getAllCategories() {
        try {
            return getAllGlRefCodes().stream()
                    .map(GlRefCodeDto::getCategory)
                    .filter(category -> category != null && !category.trim().isEmpty())
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error getting GL reference code categories: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get GL reference codes by category
     * @param category The category to filter by
     * @return List of GlRefCodeDto objects in the specified category
     */
    public List<GlRefCodeDto> getGlRefCodesByCategory(String category) {
        try {
            return getAllGlRefCodes().stream()
                    .filter(code -> code.getCategory() != null && 
                                   code.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching GL reference codes by category: " + e.getMessage(), e);
        }
    }
}
