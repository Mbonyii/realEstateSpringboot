package com.example.realestate.service.impl;

import com.example.realestate.exception.ResourceNotFoundException;
import com.example.realestate.model.Amenity;
import com.example.realestate.repository.AmenityRepository;
import com.example.realestate.service.AmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepository;

    @Override
    @Transactional(readOnly = true)
    public Amenity findById(Long id) {
        return amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Amenity findByName(String name) {
        return amenityRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity", "name", name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Amenity> findAll() {
        return amenityRepository.findAll();
    }

    @Override
    public Amenity save(Amenity amenity) {
        return amenityRepository.save(amenity);
    }

    @Override
    public Amenity update(Amenity amenity) {
        if (!amenityRepository.existsById(amenity.getId())) {
            throw new ResourceNotFoundException("Amenity", "id", amenity.getId());
        }
        return amenityRepository.save(amenity);
    }

    @Override
    public void delete(Long id) {
        if (!amenityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Amenity", "id", id);
        }
        amenityRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return amenityRepository.existsByName(name);
    }
} 