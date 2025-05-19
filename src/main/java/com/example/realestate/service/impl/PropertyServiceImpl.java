package com.example.realestate.service.impl;

import com.example.realestate.dto.ImageDto;
import com.example.realestate.dto.PropertyDto;
import com.example.realestate.exception.ResourceNotFoundException;
import com.example.realestate.model.*;
import com.example.realestate.repository.PropertyRepository;
import com.example.realestate.repository.UserRepository;
import com.example.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public PropertyDto findById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property", "id", id));
        return convertToDto(property);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertyDto> findAll(Pageable pageable) {
        return propertyRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertyDto> findProperties(Property.Status status, Long categoryId, String location,
                                          BigDecimal minPrice, BigDecimal maxPrice, Integer bedrooms,
                                          Integer bathrooms, Long amenityId, Pageable pageable) {
        return propertyRepository.findProperties(status, categoryId, location, minPrice, maxPrice,
                bedrooms, bathrooms, amenityId, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyDto> findByAgentId(Long agentId) {
        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", agentId));
        
        return propertyRepository.findByAgent(agent).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PropertyDto save(PropertyDto propertyDto) {
        Property property = convertToEntity(propertyDto);
        Property savedProperty = propertyRepository.save(property);
        return convertToDto(savedProperty);
    }

    @Override
    public PropertyDto update(PropertyDto propertyDto) {
        if (!propertyRepository.existsById(propertyDto.getId())) {
            throw new ResourceNotFoundException("Property", "id", propertyDto.getId());
        }
        
        Property property = convertToEntity(propertyDto);
        Property updatedProperty = propertyRepository.save(property);
        return convertToDto(updatedProperty);
    }

    @Override
    public void delete(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Property", "id", id);
        }
        propertyRepository.deleteById(id);
    }

    private PropertyDto convertToDto(Property property) {
        PropertyDto dto = new PropertyDto();
        dto.setId(property.getId());
        dto.setTitle(property.getTitle());
        dto.setDescription(property.getDescription());
        dto.setPrice(property.getPrice());
        dto.setLocation(property.getLocation());
        dto.setStatus(property.getStatus());
        dto.setBedrooms(property.getBedrooms());
        dto.setBathrooms(property.getBathrooms());
        dto.setArea(property.getArea());
        dto.setCreatedAt(property.getCreatedAt());
        
        if (property.getCategory() != null) {
            dto.setCategoryId(property.getCategory().getId());
            dto.setCategoryName(property.getCategory().getName());
        }
        
        if (property.getAgent() != null) {
            dto.setAgentId(property.getAgent().getId());
            dto.setAgentName(property.getAgent().getFirstName() + " " + property.getAgent().getLastName());
        }
        
        // Calculate average rating
        if (!property.getRatings().isEmpty()) {
            double averageRating = property.getRatings().stream()
                    .mapToDouble(Rating::getScore)
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(averageRating);
        }
        
        // Convert images
        dto.setImages(property.getImages().stream()
                .<ImageDto>map(image -> {
                    ImageDto imageDto = new ImageDto();
                    imageDto.setId(image.getId());
                    imageDto.setUrl(image.getUrl());
                    imageDto.setPrimary(image.isPrimary());
                    return imageDto;
                })
                .collect(Collectors.toList()));
        
        // Convert amenities
        dto.setAmenityIds(property.getAmenities().stream()
                .map(Amenity::getId)
                .collect(Collectors.toSet()));
        
        return dto;
    }

    private Property convertToEntity(PropertyDto dto) {
        Property property = new Property();
        property.setId(dto.getId());
        property.setTitle(dto.getTitle());
        property.setDescription(dto.getDescription());
        property.setPrice(dto.getPrice());
        property.setLocation(dto.getLocation());
        property.setStatus(dto.getStatus());
        property.setBedrooms(dto.getBedrooms());
        property.setBathrooms(dto.getBathrooms());
        property.setArea(dto.getArea());
        
        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            property.setCategory(category);
        }
        
        if (dto.getAgentId() != null) {
            User agent = new User();
            agent.setId(dto.getAgentId());
            property.setAgent(agent);
        }
        
        return property;
    }
} 