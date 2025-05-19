package com.example.realestate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {
    
    private String accessToken;
    private String tokenType = "Bearer";
    private Long userId;
    private String email;
    private String role;
}