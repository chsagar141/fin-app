package com.financialapp.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Marks this class as a source of bean definitions
public class AppConfig {

    @Bean // Declares a method that instantiates, configures, and initializes a new object
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder is a strong hashing algorithm
        return new BCryptPasswordEncoder();
    }
}