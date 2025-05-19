package com.example.realestate.service;

import com.example.realestate.model.Amenity;

import java.util.List;

public interface AmenityService {
    
    Amenity findById(Long id);
    
    Amenity findByName(String name);
    
    List<Amenity> findAll();
    
    Amenity save(Amenity amenity);
    
    Amenity update(Amenity amenity);
    
    void delete(Long id);
    
    boolean existsByName(String name);
}