package com.snugplace.demo.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // 🔥 TODAS LAS RUTAS PÚBLICAS - NO PASARÁN POR JWT
        return path.startsWith("/auth/") ||
                path.equals("/users/register") ||
                path.startsWith("/images") ||
                (method.equals("GET") && (
                        path.startsWith("/accommodations") ||
                                path.equals("/accommodations") ||
                                path.startsWith("/accommodations/")
                ));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String token = getToken(request);

        if (token == null) {
            // 🔥 SI NO HAY TOKEN, CONTINUAR SIN AUTENTICACIÓN
            chain.doFilter(request, response);
            return;
        }

        try {
            // Solo procesar tokens válidos
            Jws<Claims> payload = jwtUtil.parseJwt(token);
            Claims claims = payload.getPayload();

            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            if (username != null && role != null) {
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                List<GrantedAuthority> authorities = List.of(authority);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            // 🔥 TOKEN INVÁLIDO: Limpiar contexto y continuar
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        return (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;
    }
}