package com.example.realestate.security;

import com.example.realestate.model.Rating;
import com.example.realestate.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("ratingSecurity")
@RequiredArgsConstructor
public class RatingSecurity {

    private final RatingRepository ratingRepository;

    public boolean isRatingOwner(Long ratingId, UserPrincipal userPrincipal) {
        Optional<Rating> rating = ratingRepository.findById(ratingId);
        
        return rating.map(r -> r.getClient().getId().equals(userPrincipal.getId()))
                .orElse(false);
    }
}