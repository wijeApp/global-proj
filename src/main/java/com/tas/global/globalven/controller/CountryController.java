package com.tas.global.globalven.controller;

import com.tas.global.globalven.Country;
import com.tas.global.globalven.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/countries")
@CrossOrigin(origins = "*")
public class CountryController {
    
    @Autowired
    private CountryService countryService;
    
    // CREATE - Add new country
    @PostMapping
    public ResponseEntity<Country> createCountry(@RequestBody Country country) {
        try {
            // Check if country name already exists
            if (countryService.existsByName(country.getCountryName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
            }
            
            Country savedCountry = countryService.saveCountry(country);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCountry);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get all countries
    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {
        try {
            List<Country> countries = countryService.getAllCountries();
            if (countries.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get country by ID
    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable Long id) {
        try {
            Optional<Country> country = countryService.getCountryById(id);
            if (country.isPresent()) {
                return ResponseEntity.ok(country.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get country by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Country> getCountryByName(@PathVariable String name) {
        try {
            Optional<Country> country = countryService.getCountryByName(name);
            if (country.isPresent()) {
                return ResponseEntity.ok(country.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // SEARCH - Search countries by name
    @GetMapping("/search")
    public ResponseEntity<List<Country>> searchCountriesByName(@RequestParam String name) {
        try {
            List<Country> countries = countryService.searchCountriesByName(name);
            if (countries.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // UPDATE - Update country by ID
    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable Long id, @RequestBody Country countryDetails) {
        try {
            Country updatedCountry = countryService.updateCountry(id, countryDetails);
            if (updatedCountry != null) {
                return ResponseEntity.ok(updatedCountry);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // DELETE - Delete country by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        try {
            boolean deleted = countryService.deleteCountry(id);
            if (deleted) {
                return ResponseEntity.noContent().build(); // 204 No Content
            } else {
                return ResponseEntity.notFound().build(); // 404 Not Found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // STATISTICS - Get country counts
    @GetMapping("/counts")
    public ResponseEntity<Map<String, Long>> getCountryCounts() {
        try {
            Map<String, Long> counts = new HashMap<>();
            counts.put("total", countryService.getTotalCountryCount());
            
            return ResponseEntity.ok(counts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // CHECK - Check if country exists by name
    @GetMapping("/exists/{name}")
    public ResponseEntity<Boolean> checkCountryExistsByName(@PathVariable String name) {
        try {
            boolean exists = countryService.existsByName(name);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // BATCH OPERATIONS - Create multiple countries
    @PostMapping("/batch")
    public ResponseEntity<List<Country>> createCountries(@RequestBody List<Country> countries) {
        try {
            List<Country> savedCountries = new ArrayList<>();
            for (Country country : countries) {
                if (!countryService.existsByName(country.getCountryName())) {
                    savedCountries.add(countryService.saveCountry(country));
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCountries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // SEARCH - Search countries with pagination support
    @GetMapping("/search/advanced")
    public ResponseEntity<List<Country>> advancedSearchCountries(
            @RequestParam String name,
            @RequestParam(defaultValue = "false") boolean exactMatch) {
        try {
            List<Country> countries;
            if (exactMatch) {
                Optional<Country> country = countryService.getCountryByName(name);
                countries = country.map(List::of).orElse(List.of());
            } else {
                countries = countryService.searchCountriesByName(name);
            }
            
            if (countries.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // UTILITY - Get countries by name pattern
    @GetMapping("/pattern/{pattern}")
    public ResponseEntity<List<Country>> getCountriesByPattern(@PathVariable String pattern) {
        try {
            List<Country> countries = countryService.searchCountriesByName(pattern);
            if (countries.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // VALIDATION - Validate country data
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateCountry(@RequestBody Country country) {
        try {
            Map<String, Object> validation = new HashMap<>();
            List<String> errors = new ArrayList<>();
            
            if (country.getCountryName() == null || country.getCountryName().trim().isEmpty()) {
                errors.add("Country name is required");
            } else if (country.getCountryName().length() < 2) {
                errors.add("Country name must be at least 2 characters long");
            } else if (countryService.existsByName(country.getCountryName())) {
                errors.add("Country name already exists");
            }
            
            validation.put("valid", errors.isEmpty());
            validation.put("errors", errors);
            
            return ResponseEntity.ok(validation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
