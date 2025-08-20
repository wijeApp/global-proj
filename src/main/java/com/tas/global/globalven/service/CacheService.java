package com.tas.global.globalven.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {
    
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final int DEFAULT_TTL_MINUTES = 15; // 15 minutes default TTL
    
    static class CacheEntry {
        private final Object data;
        private final LocalDateTime expiry;
        
        public CacheEntry(Object data, int ttlMinutes) {
            this.data = data;
            this.expiry = LocalDateTime.now().plusMinutes(ttlMinutes);
        }
        
        public Object getData() {
            return data;
        }
        
        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiry);
        }
    }
    
    public void put(String key, Object data) {
        put(key, data, DEFAULT_TTL_MINUTES);
    }
    
    public void put(String key, Object data, int ttlMinutes) {
        cache.put(key, new CacheEntry(data, ttlMinutes));
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return (T) entry.getData();
        }
        
        // Remove expired entry
        if (entry != null) {
            cache.remove(key);
        }
        
        return null;
    }
    
    public boolean exists(String key) {
        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return true;
        }
        
        // Remove expired entry
        if (entry != null) {
            cache.remove(key);
        }
        
        return false;
    }
    
    public void remove(String key) {
        cache.remove(key);
    }
    
    public void clear() {
        cache.clear();
    }
    
    public void clearExpired() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    
    public int size() {
        clearExpired(); // Clean up first
        return cache.size();
    }
    
    // Cache keys constants
    public static final String DEPARTMENT_STATS = "department_stats";
    public static final String COUNTRY_STATS = "country_stats";
    public static final String SALARY_ANALYTICS = "salary_analytics";
    public static final String HIRING_TRENDS = "hiring_trends";
    public static final String POSITION_STATS = "position_stats";
    public static final String ANALYTICS_SUMMARY = "analytics_summary";
    public static final String ALL_EMPLOYEES = "all_employees";
}
