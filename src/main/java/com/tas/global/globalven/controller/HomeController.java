package com.tas.global.globalven.controller;

import com.tas.global.globalven.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    
    @Autowired
    private AuthorizationService authorizationService;
    
    @GetMapping("/")
    public String home() {
        return "redirect:/spa";
    }
    
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Login");
        return "login";
    }
    
    @GetMapping("/spa")
    public String spa(Model model) {
        model.addAttribute("pageTitle", "GlobalVen - Single Page App");
        return "spa";
    }
    
    @GetMapping("/employees")
    public String employees(Model model) {
        model.addAttribute("pageTitle", "Employees");
        return "employees";
    }
    
    @GetMapping("/users")
    public String users(Model model) {
        // Authorization is handled by Spring Security in SecurityConfig
        // Users with ADMIN or SUPER_ADMIN roles can access this page
        model.addAttribute("pageTitle", "User Management");
        return "users";
    }
    
    @GetMapping("/glrefcodes")
    public String glRefCodes(Model model) {
        model.addAttribute("pageTitle", "GL Reference Codes");
        return "glrefcodes";
    }

    @GetMapping("/unauthorized")
    public String unauthorized(Model model, 
                             @RequestParam(value = "resource", required = false) String resource,
                             @RequestParam(value = "role", required = false) String userRole) {
        model.addAttribute("pageTitle", "Access Denied");
        model.addAttribute("resource", resource != null ? resource : "system");
        model.addAttribute("userRole", userRole != null ? userRole : "GUEST");
        
        // Add authorization information
        if (resource != null) {
            try {
                model.addAttribute("requiredRoles", authorizationService.getRequiredRoles(resource));
                model.addAttribute("errorMessage", authorizationService.getUnauthorizedMessage(resource, 
                    userRole != null ? com.tas.global.globalven.UserRole.valueOf(userRole.toUpperCase()) : null));
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", "Invalid user role or resource specified");
            }
        }
        
        return "unauthorized";
    }
    
    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("pageTitle", "Categories");
        return "categories";
    }
    
    @GetMapping("/rates")
    public String rates(Model model) {
        model.addAttribute("pageTitle", "Rates");
        return "rates";
    }
    
    @GetMapping("/transactions")
    public String transactions(Model model) {
        model.addAttribute("pageTitle", "Transactions");
        return "transactions";
    }
    
    @GetMapping("/expenses")
    public String expenses(Model model) {
        model.addAttribute("pageTitle", "Manage Expenses");
       return "transactions";
    }
    
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "About");
        return "about";
    }
    
    @GetMapping("/departments")
    public String departments(Model model) {
        model.addAttribute("pageTitle", "Departments");
        return "departments";
    }
    
    @GetMapping("/positions")
    public String positions(Model model) {
        model.addAttribute("pageTitle", "Positions");
        return "positions";
    }
    
    @GetMapping("/system-settings")
    public String systemSettings(Model model) {
        model.addAttribute("pageTitle", "System Settings");
        return "system-settings";
    }

    @GetMapping("/country")
    public String country(Model model) {
        model.addAttribute("pageTitle", "Country");
        return "country";
    }
    
    @GetMapping("/auth-demo")
    public String authDemo(Model model) {
        model.addAttribute("pageTitle", "Authorization Demo");
        return "auth-demo";
    }
    
    @GetMapping("/database-test")
    public String databaseTest(Model model) {
        model.addAttribute("pageTitle", "Database Connection Test");
        return "database-test";
    }
}
