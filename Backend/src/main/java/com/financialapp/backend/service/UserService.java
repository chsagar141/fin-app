package com.financialapp.backend.service;

import com.financialapp.backend.dto.LoginRequest;
import com.financialapp.backend.dto.LoginResponse;
import com.financialapp.backend.dto.SignupRequest;
import com.financialapp.backend.dto.SignupResponse;
import com.financialapp.backend.entity.User;
import com.financialapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service // Marks this class as a Spring service
public class UserService {

    @Autowired // Injects UserRepository and PasswordEncoder
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public SignupResponse registerUser(SignupRequest signupRequest) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return new SignupResponse(null, null, "Username already taken!");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new SignupResponse(null, null, "Email already in use!");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword())); // Hash the password
        user.setCreatedAt(LocalDateTime.now()); // Set creation timestamp

        User savedUser = userRepository.save(user); // Save the new user

        return new SignupResponse(savedUser.getId(), savedUser.getUsername(), "User registered successfully!");
    }

    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return new LoginResponse(null, null, "Invalid username or password!");
        }

        User user = userOptional.get();

        // Compare the provided password with the hashed password in the DB
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new LoginResponse(user.getId(), user.getUsername(), "Login successful!");
        } else {
            return new LoginResponse(null, null, "Invalid username or password!");
        }
    }
}