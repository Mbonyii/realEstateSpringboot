package com.example.realestate.security;

import com.example.realestate.model.Property;
import com.example.realestate.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("propertySecurity")
@RequiredArgsConstructor
public class PropertySecurity {

    private final PropertyRepository propertyRepository;

    public boolean isPropertyOwner(Long propertyId, UserPrincipal userPrincipal) {
        Optional<Property> property = propertyRepository.findById(propertyId);
        
        return property.map(p -> p.getAgent().getId().equals(userPrincipal.getId()))
                .orElse(false);
    }

    public boolean canManageProperty(Long propertyId, UserPrincipal userPrincipal) {
        if (userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        
        return isPropertyOwner(propertyId, userPrincipal);
    }
}