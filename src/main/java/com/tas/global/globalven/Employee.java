package com.tas.global.globalven;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(unique = true)
    private String email;
    
    private String department;
    private Double salary;
    
    @Column(name = "hire_date")
    private LocalDate hireDate;
    
    @Column(name = "join_date")
    private LocalDate joinDate;
    
    private String position;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "employee_image")
    private String employeeImage;
    
    @Column(name = "image")
    private String image;
    
    @Column(name = "rate_category")
    private String rateCategory;
    
    private String country;
    
    // Default constructor
    public Employee() {
    }
    
    // Constructor with all fields including joinDate
    public Employee(Long id, String firstName, String lastName, String email, 
                   String department, Double salary, LocalDate hireDate, LocalDate joinDate,
                   String position, String phoneNumber, String employeeImage, String image,
                   String rateCategory, String country) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.hireDate = hireDate;
        this.joinDate = joinDate;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.employeeImage = employeeImage;
        this.image = image;
        this.rateCategory = rateCategory;
        this.country = country;
    }
    
    // Constructor without joinDate (for backward compatibility)
    public Employee(Long id, String firstName, String lastName, String email, 
                   String department, Double salary, LocalDate hireDate, 
                   String position, String phoneNumber, String employeeImage, String image,
                   String rateCategory, String country) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.hireDate = hireDate;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.employeeImage = employeeImage;
        this.image = image;
        this.rateCategory = rateCategory;
        this.country = country;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public Double getSalary() {
        return salary;
    }
    
    public void setSalary(Double salary) {
        this.salary = salary;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public LocalDate getJoinDate() {
        return joinDate;
    }
    
    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmployeeImage() {
        return employeeImage;
    }

    public void setEmployeeImage(String employeeImage) {
        this.employeeImage = employeeImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRateCategory() {
        return rateCategory;
    }

    public void setRateCategory(String rateCategory) {
        this.rateCategory = rateCategory;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                ", hireDate=" + hireDate +
                ", joinDate=" + joinDate +
                ", position='" + position + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", employeeImage='" + employeeImage + '\'' +
                ", image='" + image + '\'' +
                ", rateCategory='" + rateCategory + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 




