package com.example.realestate.dto;

import lombok.Data;

@Data
public class ImageDto {
    
    private Long id;
    private String url;
    private String description;
    private boolean isPrimary;
    private Long propertyId;
}