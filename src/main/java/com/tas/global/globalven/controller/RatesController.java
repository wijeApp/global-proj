package com.tas.global.globalven.controller;

import com.tas.global.globalven.Rates;
import com.tas.global.globalven.service.RatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/rates")
@CrossOrigin(origins = "*")
public class RatesController {
    
    @Autowired
    private RatesService ratesService;
    
    // CREATE - Add new rate
    @PostMapping
    public ResponseEntity<Rates> createRate(@RequestBody Rates rate) {
        try {
            Rates savedRate = ratesService.saveRate(rate);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRate);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get all rates
    @GetMapping
    public ResponseEntity<List<Rates>> getAllRates() {
        try {
            List<Rates> rates = ratesService.getAllRates();
            if (rates.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get active rates
    @GetMapping("/active")
    public ResponseEntity<List<Rates>> getActiveRates() {
        try {
            List<Rates> rates = ratesService.getActiveRates();
            if (rates.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get inactive rates
    @GetMapping("/inactive")
    public ResponseEntity<List<Rates>> getInactiveRates() {
        try {
            List<Rates> rates = ratesService.getInactiveRates();
            if (rates.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get rate by ID
    @GetMapping("/{id}")
    public ResponseEntity<Rates> getRateById(@PathVariable Long id) {
        try {
            Optional<Rates> rate = ratesService.getRateById(id);
            if (rate.isPresent()) {
                return ResponseEntity.ok(rate.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
      // READ - Get rates by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Rates>> getRatesByCategory(@PathVariable String category) {
        try {
            List<Rates> rates = ratesService.getRatesByCategory(category);
            if (rates.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get rates by rate name
    @GetMapping("/name/{rateName}")
    public ResponseEntity<List<Rates>> getRatesByName(@PathVariable String rateName) {
        try {
            List<Rates> rates = ratesService.getRatesByName(rateName);
            if (rates.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
      // SEARCH - Search rates by category
    @GetMapping("/search")
    public ResponseEntity<List<Rates>> searchRatesByCategory(@RequestParam String category) {
        try {
            List<Rates> rates = ratesService.searchRatesByCategory(category);
            if (rates.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // SEARCH - Search rates by rate name
    @GetMapping("/search-name")
    public ResponseEntity<List<Rates>> searchRatesByName(@RequestParam String rateName) {
        try {
            List<Rates> rates = ratesService.searchRatesByName(rateName);
            if (rates.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get rates by currency
    @GetMapping("/currency/{currency}")
    public ResponseEntity<List<Rates>> getRatesByCurrency(@PathVariable String currency) {
        try {
            List<Rates> rates = ratesService.getRatesByCurrency(currency);
            if (rates.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get rates by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Rates>> getRatesByType(@PathVariable String type) {
        try {
            List<Rates> rates = ratesService.getRatesByType(type);
            if (rates.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get rates by amount range
    @GetMapping("/amount-range")
    public ResponseEntity<List<Rates>> getRatesByAmountRange(
            @RequestParam Double minAmount, 
            @RequestParam Double maxAmount) {
        try {
            List<Rates> rates = ratesService.getRatesByAmountRange(minAmount, maxAmount);
            if (rates.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get currently effective rates
    @GetMapping("/effective")
    public ResponseEntity<List<Rates>> getCurrentlyEffectiveRates() {
        try {
            List<Rates> rates = ratesService.getCurrentlyEffectiveRates();
            if (rates.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // UPDATE - Update rate by ID
    @PutMapping("/{id}")
    public ResponseEntity<Rates> updateRate(@PathVariable Long id, @RequestBody Rates rateDetails) {
        try {
            Rates updatedRate = ratesService.updateRate(id, rateDetails);
            if (updatedRate != null) {
                return ResponseEntity.ok(updatedRate);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // UPDATE - Toggle rate active status
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Rates> toggleRateStatus(@PathVariable Long id, @RequestParam boolean active) {
        try {
            Rates updatedRate = ratesService.toggleRateStatus(id, active);
            if (updatedRate != null) {
                return ResponseEntity.ok(updatedRate);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // DELETE - Delete rate by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
        try {
            boolean deleted = ratesService.deleteRate(id);
            if (deleted) {
                return ResponseEntity.noContent().build(); // 204 No Content
            } else {
                return ResponseEntity.notFound().build(); // 404 Not Found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // STATISTICS - Get rate counts
    @GetMapping("/counts")
    public ResponseEntity<Map<String, Long>> getRateCounts() {
        try {
            Map<String, Long> counts = Map.of(
                "total", ratesService.getTotalRateCount(),
                "active", ratesService.getActiveRateCount(),
                "inactive", ratesService.getInactiveRateCount()
            );
            
            return ResponseEntity.ok(counts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // STATISTICS - Get rate statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Double>> getRateStatistics() {
        try {
            Map<String, Double> stats = ratesService.getRateStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // CHECK - Check if rate exists by category
    @GetMapping("/exists/{category}")
    public ResponseEntity<Boolean> checkRateExistsByCategory(@PathVariable String category) {
        try {
            boolean exists = ratesService.existsByCategory(category);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
