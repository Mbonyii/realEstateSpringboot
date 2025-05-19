package com.example.realestate.dto;

import com.example.realestate.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private User.Role role;
    private String phone;
    private String address;
    private boolean verified;
    private LocalDateTime createdAt;
    
    // Exclude from serialization for security
    @JsonIgnore
    private String password;
}