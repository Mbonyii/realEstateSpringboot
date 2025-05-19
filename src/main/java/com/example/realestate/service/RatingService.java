package com.example.realestate.service;

import com.example.realestate.dto.RatingDto;

import java.util.List;

public interface RatingService {
    
    RatingDto findById(Long id);
    
    List<RatingDto> findByPropertyId(Long propertyId);
    
    List<RatingDto> findByClientId(Long clientId);
    
    Double getAverageRatingForProperty(Long propertyId);
    
    RatingDto save(RatingDto ratingDto, Long clientId);
    
    RatingDto update(RatingDto ratingDto);
    
    void delete(Long id);
}