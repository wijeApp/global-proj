package com.tas.global.globalven.service;

import com.tas.global.globalven.Country;
import com.tas.global.globalven.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    
    @Autowired
    private CountryRepository countryRepository;
    
    // CREATE - Save a new country
    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }
    
    // READ - Get all countries
    public List<Country> getAllCountries() {
        return countryRepository.findAllOrderByCountryName();
    }
    
    // READ - Get country by ID
    public Optional<Country> getCountryById(Long id) {
        return countryRepository.findById(id);
    }
    
    // READ - Get country by name
    public Optional<Country> getCountryByName(String countryName) {
        return countryRepository.findByCountryName(countryName);
    }
    
    // SEARCH - Search countries by name
    public List<Country> searchCountriesByName(String name) {
        return countryRepository.findByCountryNameContainingIgnoreCase(name);
    }
    
    // UPDATE - Update country by ID
    public Country updateCountry(Long id, Country countryDetails) {
        Optional<Country> countryOpt = countryRepository.findById(id);
        if (countryOpt.isPresent()) {
            Country country = countryOpt.get();
            country.setCountryName(countryDetails.getCountryName());
            return countryRepository.save(country);
        }
        return null;
    }
    
    // DELETE - Delete country by ID
    public boolean deleteCountry(Long id) {
        if (countryRepository.existsById(id)) {
            countryRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // UTILITY - Check if country exists by name
    public boolean existsByName(String countryName) {
        return countryRepository.existsByCountryName(countryName);
    }
    
    // STATISTICS - Get total country count
    public long getTotalCountryCount() {
        return countryRepository.count();
    }
}
