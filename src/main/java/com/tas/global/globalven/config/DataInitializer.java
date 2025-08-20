package com.tas.global.globalven.config;

import com.tas.global.globalven.User;
import com.tas.global.globalven.UserRole;
import com.tas.global.globalven.UserStatus;
import com.tas.global.globalven.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        initializeDefaultUsers();
    }
    
    private void initializeDefaultUsers() {
        // Create Super Admin if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@globalven.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setRole(UserRole.SUPER_ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            admin.setDepartment("IT");
            admin.setCreatedBy("SYSTEM");
            admin.setCreatedDate(LocalDateTime.now());
            admin.setUpdatedDate(LocalDateTime.now());
            
            userRepository.save(admin);
            System.out.println("Created default Super Admin user: admin");
        }
        
        // Create HR Admin if not exists
        if (!userRepository.existsByUsername("hradmin")) {
            User hrAdmin = new User();
            hrAdmin.setUsername("hradmin");
            hrAdmin.setEmail("hr@globalven.com");
            hrAdmin.setPassword(passwordEncoder.encode("hr123"));
            hrAdmin.setFirstName("HR");
            hrAdmin.setLastName("Manager");
            hrAdmin.setRole(UserRole.HR);
            hrAdmin.setStatus(UserStatus.ACTIVE);
            hrAdmin.setDepartment("HR");
            hrAdmin.setCreatedBy("SYSTEM");
            hrAdmin.setCreatedDate(LocalDateTime.now());
            hrAdmin.setUpdatedDate(LocalDateTime.now());
            
            userRepository.save(hrAdmin);
            System.out.println("Created default HR Admin user: hradmin");
        }
        
        // Create Finance Admin if not exists
        if (!userRepository.existsByUsername("financeadmin")) {
            User financeAdmin = new User();
            financeAdmin.setUsername("financeadmin");
            financeAdmin.setEmail("finance@globalven.com");
            financeAdmin.setPassword(passwordEncoder.encode("finance123"));
            financeAdmin.setFirstName("Finance");
            financeAdmin.setLastName("Manager");
            financeAdmin.setRole(UserRole.FINANCE);
            financeAdmin.setStatus(UserStatus.ACTIVE);
            financeAdmin.setDepartment("Finance");
            financeAdmin.setCreatedBy("SYSTEM");
            financeAdmin.setCreatedDate(LocalDateTime.now());
            financeAdmin.setUpdatedDate(LocalDateTime.now());
            
            userRepository.save(financeAdmin);
            System.out.println("Created default Finance Admin user: financeadmin");
        }
        
        System.out.println("Default users initialization completed.");
    }
}
