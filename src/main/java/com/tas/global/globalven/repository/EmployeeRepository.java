package com.tas.global.globalven.repository;

import com.tas.global.globalven.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Find employee by email
    Optional<Employee> findByEmail(String email);
    
    // Find employees by department
    List<Employee> findByDepartment(String department);
    
    // Find employees by position
    List<Employee> findByPosition(String position);
    
    // Find employees by rate category
    List<Employee> findByRateCategory(String rateCategory);
    
    // Find employees by country
    List<Employee> findByCountry(String country);
    
    // Custom query to find employees by full name
    @Query("SELECT e FROM Employee e WHERE CONCAT(e.firstName, ' ', e.lastName) LIKE %:name%")
    List<Employee> findByFullNameContaining(@Param("name") String name);
    
    // Find employees by first name or last name containing
    @Query("SELECT e FROM Employee e WHERE e.firstName LIKE %:keyword% OR e.lastName LIKE %:keyword%")
    List<Employee> findByFirstNameOrLastNameContaining(@Param("keyword") String keyword);
    
    // Find employees by salary range
    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :minSalary AND :maxSalary")
    List<Employee> findBySalaryBetween(@Param("minSalary") Double minSalary, @Param("maxSalary") Double maxSalary);
    
    // Count employees by department
    long countByDepartment(String department);
    
    // Check if employee exists by email
    boolean existsByEmail(String email);
}
