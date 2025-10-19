package com.financialapp.backend.controller;

import com.financialapp.backend.dto.ItemDto;
import com.financialapp.backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.financialapp.backend.dto.PythonRecommendationResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items") // Base path for item management endpoints
public class ItemController {
	// ... inside ItemController class ...

	// GET AI recommendation for a user
	@GetMapping("/recommendation") // Handles GET requests to /api/items/recommendation
	public ResponseEntity<PythonRecommendationResponse> getRecommendation(@RequestHeader("X-User-ID") String userIdHeader) {
	    Long userId = getUserIdFromHeader(userIdHeader);
	    Optional<PythonRecommendationResponse> recommendation = itemService.getAiRecommendation(userId);

	    return recommendation.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
	            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 if no items or AI service error
	}
    @Autowired
    private ItemService itemService;

    // --- Helper for simplified User ID management (for local-only minimal security) ---
    // In a real app, this would come from a JWT token or session.
    // For this local demo, we'll expect the userId to be passed as a header or param
    // Or in the request body for POST/PUT. For simplicity, we'll use a header.
    // Ensure Angular sends X-User-ID header with the logged-in user's ID.
    private Long getUserIdFromHeader(String userIdHeader) {
        try {
            return Long.parseLong(userIdHeader);
        } catch (NumberFormatException e) {
            // Handle invalid User ID header, maybe return 400 or 401
            throw new IllegalArgumentException("Invalid X-User-ID header format");
        }
    }

    // GET all items for a user
    @GetMapping // Handles GET requests to /api/items
    public ResponseEntity<List<ItemDto>> getAllItems(@RequestHeader("X-User-ID") String userIdHeader) {
        Long userId = getUserIdFromHeader(userIdHeader);
        List<ItemDto> items = itemService.getAllItemsByUserId(userId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // GET a single item by ID for a user
    @GetMapping("/{id}") // Handles GET requests to /api/items/{id}
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id, @RequestHeader("X-User-ID") String userIdHeader) {
        Long userId = getUserIdFromHeader(userIdHeader);
        Optional<ItemDto> item = itemService.getItemById(id, userId);
        return item.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found if not found or not owned
    }

    // POST to add a new item for a user
    @PostMapping // Handles POST requests to /api/items
    public ResponseEntity<ItemDto> addItem(@RequestBody ItemDto itemDto, @RequestHeader("X-User-ID") String userIdHeader) {
        Long userId = getUserIdFromHeader(userIdHeader);
        Optional<ItemDto> newItem = itemService.addItem(itemDto, userId);
        return newItem.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST)); // 400 Bad Request if user ID somehow invalid
    }

    // PUT to update an existing item for a user
    @PutMapping("/{id}") // Handles PUT requests to /api/items/{id}
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto, @RequestHeader("X-User-ID") String userIdHeader) {
        Long userId = getUserIdFromHeader(userIdHeader);
        Optional<ItemDto> updatedItem = itemService.updateItem(id, itemDto, userId);
        return updatedItem.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found if not found or not owned
    }

    // DELETE an item by ID for a user
    @DeleteMapping("/{id}") // Handles DELETE requests to /api/items/{id}
    public ResponseEntity<Void> deleteItem(@PathVariable Long id, @RequestHeader("X-User-ID") String userIdHeader) {
        Long userId = getUserIdFromHeader(userIdHeader);
        boolean deleted = itemService.deleteItem(id, userId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content on successful deletion
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if not found or not owned
        }
    }
}