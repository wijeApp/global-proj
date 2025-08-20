package com.tas.global.globalven.controller;

import com.tas.global.globalven.Employee;
import com.tas.global.globalven.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportsController {
    
    @Autowired
    private EmployeeService employeeService;
    
    // Generate comprehensive employee report
    @GetMapping("/comprehensive")
    public ResponseEntity<Map<String, Object>> getComprehensiveReport() {
        try {
            Map<String, Object> report = Map.of(
                "summary", employeeService.getAnalyticsSummary(),
                "departments", employeeService.getDepartmentStatistics(),
                "countries", employeeService.getCountryStatistics(),
                "positions", employeeService.getPositionStatistics(),
                "salary", employeeService.getSalaryAnalytics(),
                "hiring", employeeService.getHiringTrends(),
                "generatedAt", LocalDate.now().toString()
            );
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Generate department-wise report
    @GetMapping("/department/{department}")
    public ResponseEntity<Map<String, Object>> getDepartmentReport(@PathVariable String department) {
        try {
            List<Employee> employees = employeeService.getEmployeesByDepartment(department);
            long totalCount = employeeService.getEmployeeCountByDepartment(department);
            
            // Calculate department-specific statistics
            double avgSalary = employees.stream()
                .filter(e -> e.getSalary() != null)
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
            
            long countriesCount = employees.stream()
                .filter(e -> e.getCountry() != null)
                .map(Employee::getCountry)
                .distinct()
                .count();
            
            Map<String, Object> report = Map.of(
                "department", department,
                "totalEmployees", totalCount,
                "averageSalary", avgSalary,
                "countriesRepresented", countriesCount,
                "employees", employees,
                "generatedAt", LocalDate.now().toString()
            );
            
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Generate country-wise report
    @GetMapping("/country/{country}")
    public ResponseEntity<Map<String, Object>> getCountryReport(@PathVariable String country) {
        try {
            List<Employee> employees = employeeService.getEmployeesByCountry(country);
            
            // Calculate country-specific statistics
            double avgSalary = employees.stream()
                .filter(e -> e.getSalary() != null)
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
            
            long departmentsCount = employees.stream()
                .filter(e -> e.getDepartment() != null)
                .map(Employee::getDepartment)
                .distinct()
                .count();
            
            Map<String, Object> report = Map.of(
                "country", country,
                "totalEmployees", employees.size(),
                "averageSalary", avgSalary,
                "departmentsRepresented", departmentsCount,
                "employees", employees,
                "generatedAt", LocalDate.now().toString()
            );
            
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Generate salary range report
    @GetMapping("/salary-range")
    public ResponseEntity<Map<String, Object>> getSalaryRangeReport(
            @RequestParam Double minSalary, 
            @RequestParam Double maxSalary) {
        try {
            List<Employee> employees = employeeService.getEmployeesBySalaryRange(minSalary, maxSalary);
            
            // Calculate statistics for this salary range
            double avgSalary = employees.stream()
                .filter(e -> e.getSalary() != null)
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
            
            long departmentsCount = employees.stream()
                .filter(e -> e.getDepartment() != null)
                .map(Employee::getDepartment)
                .distinct()
                .count();
            
            Map<String, Object> report = Map.of(
                "salaryRange", Map.of("min", minSalary, "max", maxSalary),
                "totalEmployees", employees.size(),
                "averageSalary", avgSalary,
                "departmentsRepresented", departmentsCount,
                "employees", employees,
                "generatedAt", LocalDate.now().toString()
            );
            
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Generate hiring period report
    @GetMapping("/hiring-period")
    public ResponseEntity<Map<String, Object>> getHiringPeriodReport(
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        try {
            List<Employee> employees = employeeService.getEmployeesHiredBetween(startDate, endDate);
            
            // Calculate statistics for this hiring period
            double avgSalary = employees.stream()
                .filter(e -> e.getSalary() != null)
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
            
            long departmentsCount = employees.stream()
                .filter(e -> e.getDepartment() != null)
                .map(Employee::getDepartment)
                .distinct()
                .count();
            
            Map<String, Object> report = Map.of(
                "hiringPeriod", Map.of("startDate", startDate, "endDate", endDate),
                "totalHires", employees.size(),
                "averageSalary", avgSalary,
                "departmentsRepresented", departmentsCount,
                "newHires", employees,
                "generatedAt", LocalDate.now().toString()
            );
            
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Export employee data as CSV
    @GetMapping("/export/csv")
    public ResponseEntity<String> exportEmployeesToCSV(@RequestParam(required = false) String department) {
        try {
            List<Employee> employees;
            if (department != null && !department.isEmpty()) {
                employees = employeeService.getEmployeesByDepartment(department);
            } else {
                employees = employeeService.getAllEmployees();
            }
            
            StringBuilder csv = new StringBuilder();
            csv.append("ID,First Name,Last Name,Email,Department,Position,Country,Salary,Hire Date\n");
            
            for (Employee emp : employees) {
                csv.append(emp.getId()).append(",")
                   .append(emp.getFirstName() != null ? emp.getFirstName() : "").append(",")
                   .append(emp.getLastName() != null ? emp.getLastName() : "").append(",")
                   .append(emp.getEmail() != null ? emp.getEmail() : "").append(",")
                   .append(emp.getDepartment() != null ? emp.getDepartment() : "").append(",")
                   .append(emp.getPosition() != null ? emp.getPosition() : "").append(",")
                   .append(emp.getCountry() != null ? emp.getCountry() : "").append(",")
                   .append(emp.getSalary() != null ? emp.getSalary() : "").append(",")
                   .append(emp.getHireDate() != null ? emp.getHireDate().toString() : "").append("\n");
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=employees_export.csv");
            headers.add("Content-Type", "text/csv");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csv.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
