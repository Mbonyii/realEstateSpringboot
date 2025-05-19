package com.example.realestate.controller;

import com.example.realestate.model.Amenity;
import com.example.realestate.service.AmenityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
public class AmenityController {

    private final AmenityService amenityService;

    @GetMapping
    public ResponseEntity<List<Amenity>> getAllAmenities() {
        List<Amenity> amenities = amenityService.findAll();
        return ResponseEntity.ok(amenities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Amenity> getAmenity(@PathVariable Long id) {
        Amenity amenity = amenityService.findById(id);
        return ResponseEntity.ok(amenity);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Amenity> createAmenity(@Valid @RequestBody Amenity amenity) {
        Amenity createdAmenity = amenityService.save(amenity);
        return new ResponseEntity<>(createdAmenity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Amenity> updateAmenity(
            @PathVariable Long id, 
            @Valid @RequestBody Amenity amenity) {
        amenity.setId(id);
        Amenity updatedAmenity = amenityService.update(amenity);
        return ResponseEntity.ok(updatedAmenity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        amenityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}