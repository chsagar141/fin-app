package com.financialapp.backend.repository;

import com.financialapp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Marks this interface as a Spring Data JPA repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository provides basic CRUD methods: save, findById, findAll, delete, etc.

    // Custom method to find a User by their username
    Optional<User> findByUsername(String username);

    // Custom method to check if a user with a given username exists
    boolean existsByUsername(String username);

    // Custom method to check if a user with a given email exists
    boolean existsByEmail(String email);
}