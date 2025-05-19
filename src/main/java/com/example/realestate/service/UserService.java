package com.example.realestate.service;

import com.example.realestate.dto.RegisterRequest;
import com.example.realestate.dto.UserDto;
import com.example.realestate.model.User;
import com.example.realestate.model.User.Role;

import java.util.List;

public interface UserService {
    
    User findById(Long id);
    
    UserDto findUserDtoById(Long id);
    
    User findByEmail(String email);
    
    List<User> findAll();
    
    List<UserDto> findAllByRole(Role role);
    
    UserDto registerUser(RegisterRequest registerRequest);
    
    UserDto updateUser(UserDto userDto);
    
    void deleteUser(Long id);
    
    boolean existsByEmail(String email);
}