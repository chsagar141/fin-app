package com.financialapp.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// This DTO mirrors the RecommendationResponse Pydantic model in Python
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PythonRecommendationResponse {
    private String recommendation;
    private String generatedAt; // Matches Python's generated_at (string)
}