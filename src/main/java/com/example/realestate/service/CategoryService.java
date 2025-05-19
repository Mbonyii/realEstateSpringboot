package com.example.realestate.service;

import com.example.realestate.model.Category;

import java.util.List;

public interface CategoryService {
    
    Category findById(Long id);
    
    Category findByName(String name);
    
    List<Category> findAll();
    
    Category save(Category category);
    
    Category update(Category category);
    
    void delete(Long id);
    
    boolean existsByName(String name);
}