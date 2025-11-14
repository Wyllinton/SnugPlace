package com.snugplace.demo.Security;


import com.snugplace.demo.Model.User;
import com.snugplace.demo.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    UserRepository userRepository;

    public Long getAuthenticatedId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        if (authentication.getPrincipal() instanceof String) {
            String username = (String) authentication.getPrincipal();
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
            return user.getId();
        }

        throw new RuntimeException("The principal type was not supported"); // Por defecto el 'subject' del JWT
    }
}
