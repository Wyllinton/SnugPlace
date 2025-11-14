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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        System.out.println("🔐 === JWT FILTER EXECUTING ===");
        System.out.println("🔐 Request URI: " + request.getRequestURI());
        System.out.println("🔐 Request Method: " + request.getMethod());

        String token = getToken(request);

        if (token == null) {
            System.out.println("❌ No token found, continuing without authentication");
            chain.doFilter(request, response);
            return;
        }

        System.out.println("🔐 Token found: " + token.substring(0, Math.min(20, token.length())) + "...");

        try {
            Jws<Claims> payload = jwtUtil.parseJwt(token);
            Claims claims = payload.getPayload();

            // ⚠️ CAMBIO IMPORTANTE: Usar el email en lugar del ID como principal
            String email = claims.get("email", String.class); // Usar email en lugar de subject
            String role = claims.get("role", String.class);
            String userId = claims.get("userId", String.class);

            System.out.println("🔐 Extracted username: " + email);
            System.out.println("🔐 Extracted role: " + role);

            if (email != null && role != null) {
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                List<GrantedAuthority> authorities = List.of(authority);

                // ⚠️ Usar el email como principal en lugar del ID
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("✅ Authentication SUCCESS for user: " + email);
                System.out.println("✅ Authorities set: " + authorities);
            } else {
                System.out.println("❌ Missing email or role in token");
            }

        } catch (Exception e) {
            System.out.println("❌ Token validation FAILED: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        System.out.println("🔐 Continuing filter chain...");
        chain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}