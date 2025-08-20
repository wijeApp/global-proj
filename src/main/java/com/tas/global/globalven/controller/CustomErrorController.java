package com.tas.global.globalven.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get error status code
        Object status = request.getAttribute("javax.servlet.error.status_code");
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            
            switch (statusCode) {
                case 403:
                    model.addAttribute("pageTitle", "Access Denied");
                    model.addAttribute("errorMessage", "You are not Allow Access This Page");
                    model.addAttribute("statusCode", 403);
                    return "error";
                case 404:
                    model.addAttribute("pageTitle", "Page Not Found");
                    model.addAttribute("errorMessage", "You are not Allow Access This Page");
                    model.addAttribute("statusCode", 404);
                    return "error";
                case 500:
                    model.addAttribute("pageTitle", "Internal Server Error");
                    model.addAttribute("errorMessage", "You are not Allow Access This Page");
                    model.addAttribute("statusCode", 500);
                    return "error";
                default:
                    model.addAttribute("pageTitle", "Error");
                    model.addAttribute("errorMessage", "You are not Allow Access This Page");
                    model.addAttribute("statusCode", statusCode);
                    return "error";
            }
        }
        
        // Default error
        model.addAttribute("pageTitle", "Error");
        model.addAttribute("errorMessage", "You are not Allow Access This Page");
        model.addAttribute("statusCode", "Unknown");
        return "error";
    }
}

