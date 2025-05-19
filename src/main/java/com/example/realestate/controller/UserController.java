package com.example.realestate.controller;

import com.example.realestate.dto.UserDto;
import com.example.realestate.model.User;
import com.example.realestate.security.CurrentUser;
import com.example.realestate.security.UserPrincipal;
import com.example.realestate.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserDto userDto = userService.findUserDtoById(currentUser.getId());
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id, principal)")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.findUserDtoById(id);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id, principal)")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto userDto) {
        userDto.setId(id);
        UserDto updatedUser = userService.updateUser(userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/agents")
    public ResponseEntity<List<UserDto>> getAllAgents() {
        List<UserDto> agents = userService.findAllByRole(User.Role.AGENT);
        return ResponseEntity.ok(agents);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}