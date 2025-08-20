package com.tas.global.globalven.repository;

import com.tas.global.globalven.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    
    // Find country by name
    Optional<Country> findByCountryName(String countryName);
    
    // Find countries by name containing the given string (case-insensitive)
    @Query("SELECT c FROM Country c WHERE LOWER(c.countryName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Country> findByCountryNameContainingIgnoreCase(@Param("name") String name);
    
    // Check if a country exists by name
    boolean existsByCountryName(String countryName);
    
    // Get all countries ordered by name
    @Query("SELECT c FROM Country c ORDER BY c.countryName ASC")
    List<Country> findAllOrderByCountryName();
}
