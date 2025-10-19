package com.financialapp.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set; // Use Set for collections in JPA relationships

@Entity
@Table(name = "users") // Explicitly map to 'users' table
@Data // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: Generates a no-argument constructor
@AllArgsConstructor // Lombok: Generates an all-argument constructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Use Long for primary keys in Java

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 255) // Length for hashed password
    private String password;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // Use LocalDateTime for TIMESTAMP

    // One-to-Many relationship with Item
    // mappedBy refers to the field name in the Item entity that owns the relationship (user)
    // CascadeType.ALL means if a user is deleted, their items are also deleted (matches ON DELETE CASCADE in DB)
    // orphanRemoval = true is good practice when cascade is ALL
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items;

    // PrePersist method to set createdAt before an entity is first saved
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) { // Only set if not already set (e.g., during testing with specific date)
            createdAt = LocalDateTime.now();
        }
    }
}