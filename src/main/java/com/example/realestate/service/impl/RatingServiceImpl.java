package com.example.realestate.service.impl;

import com.example.realestate.dto.RatingDto;
import com.example.realestate.exception.ResourceNotFoundException;
import com.example.realestate.model.Property;
import com.example.realestate.model.Rating;
import com.example.realestate.model.User;
import com.example.realestate.repository.PropertyRepository;
import com.example.realestate.repository.RatingRepository;
import com.example.realestate.repository.UserRepository;
import com.example.realestate.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public RatingDto findById(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id));
        return convertToDto(rating);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingDto> findByPropertyId(Long propertyId) {
        return ratingRepository.findByPropertyId(propertyId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingDto> findByClientId(Long clientId) {
        return ratingRepository.findByClientId(clientId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRatingForProperty(Long propertyId) {
        return ratingRepository.getAverageRatingForProperty(propertyId);
    }

    @Override
    public RatingDto save(RatingDto ratingDto, Long clientId) {
        if (ratingRepository.existsByPropertyIdAndClientId(ratingDto.getPropertyId(), clientId)) {
            throw new IllegalStateException("Client has already rated this property");
        }

        Property property = propertyRepository.findById(ratingDto.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property", "id", ratingDto.getPropertyId()));
        
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", clientId));

        Rating rating = new Rating();
        rating.setProperty(property);
        rating.setClient(client);
        rating.setScore(ratingDto.getScore());
        rating.setComment(ratingDto.getComment());

        Rating savedRating = ratingRepository.save(rating);
        return convertToDto(savedRating);
    }

    @Override
    public RatingDto update(RatingDto ratingDto) {
        Rating rating = ratingRepository.findById(ratingDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", ratingDto.getId()));

        rating.setScore(ratingDto.getScore());
        rating.setComment(ratingDto.getComment());

        Rating updatedRating = ratingRepository.save(rating);
        return convertToDto(updatedRating);
    }

    @Override
    public void delete(Long id) {
        if (!ratingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rating", "id", id);
        }
        ratingRepository.deleteById(id);
    }

    private RatingDto convertToDto(Rating rating) {
        RatingDto dto = new RatingDto();
        dto.setId(rating.getId());
        dto.setPropertyId(rating.getProperty().getId());
        dto.setClientId(rating.getClient().getId());
        dto.setClientName(rating.getClient().getFirstName() + " " + rating.getClient().getLastName());
        dto.setScore(rating.getScore());
        dto.setComment(rating.getComment());
        dto.setCreatedAt(rating.getCreatedAt());
        return dto;
    }
} 