package com.example.realestate.controller;

import com.example.realestate.dto.RatingDto;
import com.example.realestate.security.CurrentUser;
import com.example.realestate.security.UserPrincipal;
import com.example.realestate.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<RatingDto>> getPropertyRatings(@PathVariable Long propertyId) {
        List<RatingDto> ratings = ratingService.findByPropertyId(propertyId);
        return ResponseEntity.ok(ratings);
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<RatingDto> createRating(
            @Valid @RequestBody RatingDto ratingDto,
            @CurrentUser UserPrincipal currentUser) {
        
        RatingDto createdRating = ratingService.save(ratingDto, currentUser.getId());
        return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT') and @ratingSecurity.isRatingOwner(#id, principal)")
    public ResponseEntity<RatingDto> updateRating(
            @PathVariable Long id,
            @Valid @RequestBody RatingDto ratingDto) {
        
        ratingDto.setId(id);
        RatingDto updatedRating = ratingService.update(ratingDto);
        return ResponseEntity.ok(updatedRating);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENT') and @ratingSecurity.isRatingOwner(#id, principal))")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<RatingDto>> getUserRatings(@CurrentUser UserPrincipal currentUser) {
        List<RatingDto> ratings = ratingService.findByClientId(currentUser.getId());
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/average/{propertyId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long propertyId) {
        Double averageRating = ratingService.getAverageRatingForProperty(propertyId);
        return ResponseEntity.ok(averageRating);
    }
}