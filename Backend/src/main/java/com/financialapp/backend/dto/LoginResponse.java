package com.financialapp.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long userId; // The ID of the authenticated user
    private String username;
    private String message; // E.g., "Login successful"
}