package com.example.realestate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Size(max = 15)
    private String phone;

    @Size(max = 255)
    private String address;

    @Column(name = "is_verified")
    private boolean isVerified = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
    private List<Property> properties = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Transaction> buyerTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
    private List<Transaction> agentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();
    
    // Helper method to add a property
    public void addProperty(Property property) {
        properties.add(property);
        property.setAgent(this);
    }

    // Helper method to add a transaction as buyer
    public void addBuyerTransaction(Transaction transaction) {
        buyerTransactions.add(transaction);
        transaction.setClient(this);
    }

    // Helper method to add a transaction as agent
    public void addAgentTransaction(Transaction transaction) {
        agentTransactions.add(transaction);
        transaction.setAgent(this);
    }

    public enum Role {
        ADMIN, AGENT, CLIENT
    }
}