package com.example.realestate.controller;

import com.example.realestate.dto.JwtAuthResponse;
import com.example.realestate.dto.LoginRequest;
import com.example.realestate.dto.RegisterRequest;
import com.example.realestate.dto.UserDto;
import com.example.realestate.model.User;
import com.example.realestate.security.JwtTokenProvider;
import com.example.realestate.security.UserPrincipal;
import com.example.realestate.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userService.findById(userPrincipal.getId());
        
        JwtAuthResponse response = JwtAuthResponse.builder()
                .accessToken(jwt)
                .userId(userPrincipal.getId())
                .email(userPrincipal.getUsername())
                .role(user.getRole().name())
                .build();
                
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDto createdUser = userService.registerUser(registerRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}