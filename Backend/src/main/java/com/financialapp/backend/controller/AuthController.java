package com.financialapp.backend.controller;

import com.financialapp.backend.dto.LoginRequest;
import com.financialapp.backend.dto.LoginResponse;
import com.financialapp.backend.dto.SignupRequest;
import com.financialapp.backend.dto.SignupResponse;
import com.financialapp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Marks this class as a REST controller
@RequestMapping("/api/auth") // Base path for authentication endpoints
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup") // Handles POST requests to /api/auth/signup
    public ResponseEntity<SignupResponse> registerUser(@RequestBody SignupRequest signupRequest) {
        SignupResponse response = userService.registerUser(signupRequest);
        if (response.getUserId() != null) {
            return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created on success
        } else {
            // Handle cases where username/email is already taken
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/login") // Handles POST requests to /api/auth/login
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.authenticateUser(loginRequest);
        if (response.getUserId() != null) {
            return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK on success
        } else {
            // Handle invalid credentials
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }
    }
}