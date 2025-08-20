package com.tas.global.globalven.config;

import com.tas.global.globalven.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity in this example, consider enabling for production
            .authorizeHttpRequests(auth -> auth                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll() // Public access
                .requestMatchers("/api/glref-codes/test-connection").permitAll() // Public test endpoint for GL codes
                .requestMatchers("/api/glref-codes/sample-data").permitAll() // Public sample data endpoint for testing
                .requestMatchers("/api/glref-codes/active-for-dropdown").authenticated() // Allow authenticated users to load GL codes for dropdowns
                .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "SUPER_ADMIN") // Admin access for user API
                .requestMatchers("/users").hasAnyRole("ADMIN", "SUPER_ADMIN") // Admin access for users page
                .requestMatchers("/api/employees/**").hasAnyRole("HR", "ADMIN", "SUPER_ADMIN", "MANAGER") // Employee API access
                .requestMatchers("/employees").hasAnyRole("HR", "ADMIN", "SUPER_ADMIN", "MANAGER") // Employees page access
                .requestMatchers("/api/transfers/**").hasAnyRole("FINANCE", "ADMIN", "SUPER_ADMIN", "MANAGER") // Transfers API access
                .requestMatchers("/transactions", "/expenses").hasAnyRole("FINANCE", "ADMIN", "SUPER_ADMIN", "MANAGER") // Transfers pages access
                .requestMatchers("/api/rates/**").hasAnyRole("FINANCE", "ADMIN", "SUPER_ADMIN", "MANAGER") // Rates API access
                .requestMatchers("/rates").hasAnyRole("FINANCE", "ADMIN", "SUPER_ADMIN", "MANAGER") // Rates page access
                .requestMatchers("/api/countries/**").hasAnyRole("HR", "ADMIN", "SUPER_ADMIN", "MANAGER") // Countries API access
                .requestMatchers("/country").hasAnyRole("HR", "ADMIN", "SUPER_ADMIN", "MANAGER") // Countries page access
                .anyRequest().authenticated() // All other requests require authentication
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/spa", true) // Redirect to SPA after successful login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}
