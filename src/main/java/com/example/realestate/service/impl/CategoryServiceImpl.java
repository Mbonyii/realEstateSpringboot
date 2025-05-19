package com.example.realestate.service.impl;

import com.example.realestate.exception.ResourceAlreadyExistsException;
import com.example.realestate.exception.ResourceNotFoundException;
import com.example.realestate.model.Category;
import com.example.realestate.repository.CategoryRepository;
import com.example.realestate.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Category findByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {
        if (existsByName(category.getName())) {
            throw new ResourceAlreadyExistsException("Category", "name", category.getName());
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        if (!categoryRepository.existsById(category.getId())) {
            throw new ResourceNotFoundException("Category", "id", category.getId());
        }
        
        // Check if another category with the same name exists (excluding current category)
        categoryRepository.findByName(category.getName())
                .ifPresent(existingCategory -> {
                    if (!existingCategory.getId().equals(category.getId())) {
                        throw new ResourceAlreadyExistsException("Category", "name", category.getName());
                    }
                });
        
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", "id", id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
} 