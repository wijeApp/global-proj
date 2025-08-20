package com.tas.global.globalven;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "country_name", nullable = false, unique = true)
    private String countryName;

    // Default constructor
    public Country() {}

    // Constructor with all fields
    public Country(Long id, String countryName) {
        this.id = id;
        this.countryName = countryName;
    }

    // Constructor without id
    public Country(String countryName) {
        this.countryName = countryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }    public String getCountryName() {
        return countryName;
    }    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", countryName='" + countryName + '\'' +
                '}';
    }    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(id, country.id) &&
               Objects.equals(countryName, country.countryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, countryName);
    }
}
