package com.example.realestate.repository;

import com.example.realestate.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    List<Rating> findByPropertyId(Long propertyId);
    
    List<Rating> findByClientId(Long clientId);
    
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.property.id = :propertyId")
    Double getAverageRatingForProperty(@Param("propertyId") Long propertyId);
    
    boolean existsByPropertyIdAndClientId(Long propertyId, Long clientId);
}