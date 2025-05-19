package com.example.realestate.controller;

import com.example.realestate.dto.PropertyDto;
import com.example.realestate.model.Property;
import com.example.realestate.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping
    public ResponseEntity<Page<PropertyDto>> getAllProperties(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) Property.Status status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) Integer bathrooms,
            @RequestParam(required = false) Long amenityId) {
        
        Page<PropertyDto> properties = propertyService.findProperties(
                status, categoryId, location, minPrice, maxPrice, 
                bedrooms, bathrooms, amenityId, pageable);
        
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDto> getProperty(@PathVariable Long id) {
        PropertyDto property = propertyService.findById(id);
        return ResponseEntity.ok(property);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<PropertyDto> createProperty(@Valid @RequestBody PropertyDto propertyDto) {
        PropertyDto createdProperty = propertyService.save(propertyDto);
        return new ResponseEntity<>(createdProperty, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<PropertyDto> updateProperty(
            @PathVariable Long id, 
            @Valid @RequestBody PropertyDto propertyDto) {
        propertyDto.setId(id);
        PropertyDto updatedProperty = propertyService.update(propertyDto);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<PropertyDto>> getPropertiesByAgent(@PathVariable Long agentId) {
        List<PropertyDto> properties = propertyService.findByAgentId(agentId);
        return ResponseEntity.ok(properties);
    }
}