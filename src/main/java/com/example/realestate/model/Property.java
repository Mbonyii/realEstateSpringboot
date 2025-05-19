package com.example.realestate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotBlank
    private String location;

    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    private int bedrooms;
    
    private int bathrooms;
    
    private double area;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private User agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "property_amenities",
        joinColumns = @JoinColumn(name = "property_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities = new HashSet<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    // Helper methods
    public void addImage(Image image) {
        images.add(image);
        image.setProperty(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setProperty(null);
    }

    public void addAmenity(Amenity amenity) {
        amenities.add(amenity);
        amenity.getProperties().add(this);
    }

    public void removeAmenity(Amenity amenity) {
        amenities.remove(amenity);
        amenity.getProperties().remove(this);
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
        rating.setProperty(this);
    }

    public enum Status {
        AVAILABLE, SOLD, RENTED, PENDING
    }
}