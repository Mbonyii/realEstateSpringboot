package com.example.realestate.security;

import com.example.realestate.model.User;
import com.example.realestate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {

    private final UserRepository userRepository;

    public boolean isCurrentUser(Long userId, UserPrincipal userPrincipal) {
        return userPrincipal.getId().equals(userId);
    }

    public boolean canManageUser(Long userId, UserPrincipal userPrincipal) {
        if (userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        
        return isCurrentUser(userId, userPrincipal);
    }
}