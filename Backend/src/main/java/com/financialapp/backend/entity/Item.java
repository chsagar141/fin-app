package com.financialapp.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate; // Use LocalDate for DATE type

@Entity
@Table(name = "items") // Explicitly map to 'items' table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Many items can belong to one user
    @JoinColumn(name = "user_id", nullable = false) // This is the foreign key column
    private User user; // Reference to the User entity

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2) // Matches DECIMAL(10, 2)
    private BigDecimal price; // Use BigDecimal for monetary values

    @Column(length = 50) // No nullable=false as it's NULL in DB
    private String category;

    @Column(name = "date_added") // Matches column name in DB
    private LocalDate dateAdded; // Use LocalDate for DATE type

    @Column(columnDefinition = "TEXT") // For TEXT type in MySQL
    private String description;

    // PrePersist method to set dateAdded before an entity is first saved
    @PrePersist
    protected void onCreate() {
        if (dateAdded == null) {
            dateAdded = LocalDate.now();
        }
    }
}