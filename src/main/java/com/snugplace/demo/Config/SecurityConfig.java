package com.snugplace.demo.Config;

import com.snugplace.demo.Security.JWTAuthenticationEntryPoint;
import com.snugplace.demo.Security.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        // 🔥 PUBLIC ENDPOINTS - AUTENTICACIÓN
                        .requestMatchers("/auth/**").permitAll()

                        // 🔥 PUBLIC ENDPOINTS - USUARIOS (REGISTRO)
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register-with-image").permitAll()

                        // 🔥 PUBLIC ENDPOINTS - IMÁGENES
                        .requestMatchers(HttpMethod.POST, "/images/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/images/**").permitAll()

                        // 🔥 PUBLIC ENDPOINTS - ALOJAMIENTOS (solo lectura)
                        .requestMatchers(HttpMethod.GET, "/accommodations").permitAll()
                        .requestMatchers(HttpMethod.POST, "/accommodations/cards").permitAll()
                        .requestMatchers(HttpMethod.GET, "/accommodations/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/accommodations/search").permitAll()

                        // 🔥 ENDPOINTS PROTEGIDOS POR ROLES - CORREGIDOS
                        .requestMatchers("/accommodations/my-accomodations").hasRole("HOST")
                        .requestMatchers(HttpMethod.POST, "/accommodations/create").hasRole("HOST")
                        .requestMatchers(HttpMethod.PATCH, "/accommodations/edit/**").hasRole("HOST")
                        .requestMatchers(HttpMethod.DELETE, "/accommodations/**").hasRole("HOST")

                        .requestMatchers(HttpMethod.POST, "/metrics/accommodations/{id}").hasRole("HOST")

                        .requestMatchers("/bookings/{id}/detail-user").hasAnyRole("USER", "GUEST")
                        .requestMatchers("/bookings/{id}/detail").hasRole("HOST")
                        .requestMatchers(HttpMethod.POST, "/bookings/**").hasAnyRole("USER", "HOST", "ADMIN", "GUEST")

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new JWTAuthenticationEntryPoint()))
                // ⚠️ EL FILTRO DEBE ESTAR ANTES DE LA AUTENTICACIÓN
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Orígenes permitidos
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "http://localhost:3000",
                "http://localhost:5173",
                "http://127.0.0.1:4200",
                "http://127.0.0.1:3000",
                "https://tudominio.com"
        ));

        // Métodos HTTP permitidos
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Headers permitidos
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        // Headers expuestos
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Disposition"
        ));

        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}