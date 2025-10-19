package com.financialapp.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// This DTO mirrors the FinancialItem Pydantic model in Python
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PythonFinancialItem {
    private String name;
    private BigDecimal price; // Use BigDecimal to match Python's float for precision
    private String category;
    private LocalDate dateAdded; // Matches Python's date_added (we'll send as string from Spring)
    private String description;
}