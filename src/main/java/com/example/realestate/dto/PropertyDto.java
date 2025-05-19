package com.example.realestate.dto;

import com.example.realestate.model.Property;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class PropertyDto {
    
    private Long id;
    
    @NotBlank(message = "Title cannot be blank")
    private String title;
    
    private String description;
    
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @NotBlank(message = "Location cannot be blank")
    private String location;
    
    private Property.Status status;
    
    private int bedrooms;
    
    private int bathrooms;
    
    private double area;
    
    private Long categoryId;
    
    private String categoryName;
    
    private Long agentId;
    
    private String agentName;
    
    private LocalDateTime createdAt;
    
    private List<ImageDto> images = new ArrayList<>();
    
    private Set<Long> amenityIds = new HashSet<>();
    
    private Double averageRating;
}