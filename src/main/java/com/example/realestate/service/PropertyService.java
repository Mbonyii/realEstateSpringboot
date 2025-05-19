package com.example.realestate.service;

import com.example.realestate.dto.PropertyDto;
import com.example.realestate.model.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface PropertyService {
    
    PropertyDto findById(Long id);
    
    Page<PropertyDto> findAll(Pageable pageable);
    
    Page<PropertyDto> findProperties(Property.Status status, Long categoryId, String location, 
                            BigDecimal minPrice, BigDecimal maxPrice, Integer bedrooms,
                            Integer bathrooms, Long amenityId, Pageable pageable);
    
    List<PropertyDto> findByAgentId(Long agentId);
    
    PropertyDto save(PropertyDto propertyDto);
    
    PropertyDto update(PropertyDto propertyDto);
    
    void delete(Long id);
}