package com.financialapp.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty; // <-- ADD THIS IMPORT
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PythonRecommendationRequest {
    @JsonProperty("user_id") // <-- ADD THIS ANNOTATION
    private Long userId; // This Java field will now be serialized as 'user_id' in JSON
    private List<PythonFinancialItem> items;
}