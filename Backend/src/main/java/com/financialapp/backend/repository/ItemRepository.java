package com.financialapp.backend.repository;

import com.financialapp.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    // Custom method to find all items belonging to a specific user
    List<Item> findByUserId(Long userId);

    // Custom method to find a specific item by its ID and the user's ID
    Optional<Item> findByIdAndUserId(Long id, Long userId);

    // Custom method to delete a specific item by its ID and the user's ID
    void deleteByIdAndUserId(Long id, Long userId);
}