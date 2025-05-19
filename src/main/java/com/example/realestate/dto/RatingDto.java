package com.example.realestate.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingDto {
    
    private Long id;
    
    @NotNull(message = "Score cannot be null")
    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 5, message = "Score must be at most 5")
    private Integer score;
    
    private String comment;
    
    private LocalDateTime createdAt;
    
    private Long propertyId;
    
    private Long clientId;
    
    private String clientName;
}