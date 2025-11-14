package com.snugplace.demo.Security;


import com.snugplace.demo.Model.User;
import com.snugplace.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;

    public Long getAuthenticatedId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("🔐 Authentication: " + authentication);
        System.out.println("🔐 Principal: " + (authentication != null ? authentication.getPrincipal() : "null"));
        System.out.println("🔐 Authenticated: " + (authentication != null ? authentication.isAuthenticated() : "false"));
        System.out.println("🔐 Authorities: " + (authentication != null ? authentication.getAuthorities() : "null"));

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        if ("anonymousUser".equals(authentication.getPrincipal())) {
            System.out.println("❌ Usuario anónimo detectado");
            throw new RuntimeException("Por favor inicia sesión para acceder a esta funcionalidad");
        }

        if (authentication.getPrincipal() instanceof String) {
            String username = (String) authentication.getPrincipal();
            System.out.println("🔍 Buscando usuario con email: " + username);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

            System.out.println("✅ Usuario encontrado: " + user.getId());
            return user.getId();
        }

        throw new RuntimeException("Tipo de principal no soportado: " + authentication.getPrincipal().getClass().getName());
    }
}
