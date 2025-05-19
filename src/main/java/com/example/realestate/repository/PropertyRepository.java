package com.example.realestate.repository;

import com.example.realestate.model.Property;
import com.example.realestate.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    
    List<Property> findByAgent(User agent);
    
    List<Property> findByStatus(Property.Status status);
    
    List<Property> findByCategoryId(Long categoryId);
    
    Page<Property> findByStatus(Property.Status status, Pageable pageable);
    
    @Query("SELECT p FROM Property p WHERE " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:location IS NULL OR LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:bedrooms IS NULL OR p.bedrooms >= :bedrooms) AND " +
           "(:bathrooms IS NULL OR p.bathrooms >= :bathrooms) AND " +
           "(:amenityId IS NULL OR EXISTS (SELECT 1 FROM p.amenities a WHERE a.id = :amenityId))")
    Page<Property> findProperties(
            @Param("status") Property.Status status,
            @Param("categoryId") Long categoryId,
            @Param("location") String location,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("bedrooms") Integer bedrooms,
            @Param("bathrooms") Integer bathrooms,
            @Param("amenityId") Long amenityId,
            Pageable pageable);
    
    @Query("SELECT p FROM Property p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Property> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT p FROM Property p WHERE LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Property> findByLocationContaining(@Param("location") String location);
    
    @Query("SELECT p FROM Property p WHERE p.bedrooms >= :bedrooms")
    List<Property> findByBedroomsGreaterThanEqual(@Param("bedrooms") int bedrooms);
    
    @Query("SELECT p FROM Property p WHERE p.bathrooms >= :bathrooms")
    List<Property> findByBathroomsGreaterThanEqual(@Param("bathrooms") int bathrooms);
    
    @Query("SELECT p FROM Property p JOIN p.amenities a WHERE a.id = :amenityId")
    List<Property> findByAmenityId(@Param("amenityId") Long amenityId);
}