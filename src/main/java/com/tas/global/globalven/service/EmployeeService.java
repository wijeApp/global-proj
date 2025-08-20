package com.tas.global.globalven.service;

import com.tas.global.globalven.Employee;
import com.tas.global.globalven.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    // Create or Update Employee
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    // Get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    // Get employee by ID
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }
    
    // Get employee by email
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
    
    // Get employees by department
    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department);
    }
    
    // Get employees by position
    public List<Employee> getEmployeesByPosition(String position) {
        return employeeRepository.findByPosition(position);
    }
    
    // Get employees by rate category
    public List<Employee> getEmployeesByRateCategory(String rateCategory) {
        return employeeRepository.findByRateCategory(rateCategory);
    }
    
    // Get employees by country
    public List<Employee> getEmployeesByCountry(String country) {
        return employeeRepository.findByCountry(country);
    }
    
    // Search employees by name
    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.findByFullNameContaining(name);
    }
    
    // Search employees by keyword
    public List<Employee> searchEmployeesByKeyword(String keyword) {
        return employeeRepository.findByFirstNameOrLastNameContaining(keyword);
    }
    
    // Get employees by salary range
    public List<Employee> getEmployeesBySalaryRange(Double minSalary, Double maxSalary) {
        return employeeRepository.findBySalaryBetween(minSalary, maxSalary);
    }
    
    // Update employee
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            
            // Update fields
            employee.setFirstName(employeeDetails.getFirstName());
            employee.setLastName(employeeDetails.getLastName());
            employee.setEmail(employeeDetails.getEmail());
            employee.setDepartment(employeeDetails.getDepartment());
            employee.setSalary(employeeDetails.getSalary());
            employee.setHireDate(employeeDetails.getHireDate());
            employee.setJoinDate(employeeDetails.getJoinDate());
            employee.setPosition(employeeDetails.getPosition());
            employee.setPhoneNumber(employeeDetails.getPhoneNumber());
            employee.setEmployeeImage(employeeDetails.getEmployeeImage());
            employee.setImage(employeeDetails.getImage());
            employee.setRateCategory(employeeDetails.getRateCategory());
            employee.setCountry(employeeDetails.getCountry());
            
            return employeeRepository.save(employee);
        }
        
        return null; // Employee not found
    }
    
    // Delete employee by ID
    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Check if employee exists by email
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }
    
    // Get count of employees by department
    public long getEmployeeCountByDepartment(String department) {
        return employeeRepository.countByDepartment(department);
    }
    
    // Get total employee count
    public long getTotalEmployeeCount() {
        return employeeRepository.count();
    }
    
    // Analytics methods for reports
    public Map<String, Object> getAnalyticsSummary() {
        List<Employee> employees = employeeRepository.findAll();
        long totalEmployees = employees.size();
        long uniqueDepartments = employees.stream()
            .filter(e -> e.getDepartment() != null)
            .map(Employee::getDepartment)
            .distinct()
            .count();
        long uniqueCountries = employees.stream()
            .filter(e -> e.getCountry() != null)
            .map(Employee::getCountry)
            .distinct()
            .count();
        double avgSalary = employees.stream()
            .filter(e -> e.getSalary() != null)
            .mapToDouble(Employee::getSalary)
            .average()
            .orElse(0.0);
        
        return Map.of(
            "totalEmployees", totalEmployees,
            "uniqueDepartments", uniqueDepartments,
            "uniqueCountries", uniqueCountries,
            "averageSalary", avgSalary
        );
    }
    
    public Map<String, Long> getDepartmentStatistics() {
        return employeeRepository.findAll().stream()
            .filter(e -> e.getDepartment() != null)
            .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
    }
    
    public Map<String, Long> getCountryStatistics() {
        return employeeRepository.findAll().stream()
            .filter(e -> e.getCountry() != null)
            .collect(Collectors.groupingBy(Employee::getCountry, Collectors.counting()));
    }
    
    public Map<String, Long> getPositionStatistics() {
        return employeeRepository.findAll().stream()
            .filter(e -> e.getPosition() != null)
            .collect(Collectors.groupingBy(Employee::getPosition, Collectors.counting()));
    }
    
    public Map<String, Object> getSalaryAnalytics() {
        List<Employee> employees = employeeRepository.findAll();
        List<Double> salaries = employees.stream()
            .filter(e -> e.getSalary() != null)
            .map(Employee::getSalary)
            .sorted()
            .collect(Collectors.toList());
        
        if (salaries.isEmpty()) {
            return Map.of("min", 0.0, "max", 0.0, "average", 0.0, "median", 0.0);
        }
        
        double min = salaries.get(0);
        double max = salaries.get(salaries.size() - 1);
        double average = salaries.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double median = salaries.size() % 2 == 0 
            ? (salaries.get(salaries.size() / 2 - 1) + salaries.get(salaries.size() / 2)) / 2.0
            : salaries.get(salaries.size() / 2);
        
        return Map.of(
            "min", min,
            "max", max,
            "average", average,
            "median", median
        );
    }
    
    public Map<String, Long> getHiringTrends() {
        return employeeRepository.findAll().stream()
            .filter(e -> e.getHireDate() != null)
            .collect(Collectors.groupingBy(
                e -> String.valueOf(e.getHireDate().getYear()),
                Collectors.counting()
            ));
    }
    
    // Get employees hired between two dates
    public List<Employee> getEmployeesHiredBetween(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        
        return employeeRepository.findAll().stream()
            .filter(e -> e.getHireDate() != null)
            .filter(e -> !e.getHireDate().isBefore(start) && !e.getHireDate().isAfter(end))
            .collect(Collectors.toList());
    }
}
